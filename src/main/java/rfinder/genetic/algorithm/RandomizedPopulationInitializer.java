package rfinder.genetic.algorithm;

import rfinder.genetic.structures.RouteChromosome;
import rfinder.dao.RouteDAO;
import rfinder.genetic.structures.PopulationTraits;
import rfinder.structures.common.TripPatternID;
import rfinder.structures.nodes.PathNode;
import rfinder.structures.nodes.StopNode;
import rfinder.structures.segments.*;

import java.util.*;

public class RandomizedPopulationInitializer implements PopulationInitializer {

    private final RouteDAO dao;
    private final PopulationTraits traits;
    private final Random rnd = new Random();

    private HashSet<StopNode> usedStops;
    private HashSet<TripPatternID> usedTrips;

    public enum Mode {
        RIDE, TRANSFER
    }

    public RandomizedPopulationInitializer(RouteDAO dao, PopulationTraits traits){
        this.dao = dao;
        this.traits = traits;
    }

    @Override
    public RoutePopulation initializePopulation(PathNode source, PathNode destination) {
        RoutePopulation population = new RoutePopulation();
        RouteChromosome solution;

        int counter = 0;

        while(counter < traits.maxSize()){
            solution = generateNewSolution(source, destination);
            population.add(solution);
            counter++;
        }

        return population;
    }

    private void resetContext() {
        usedTrips = new HashSet<>();
        usedStops = new HashSet<>();
    }

    // generate a single solution
    private RouteChromosome generateNewSolution(PathNode source, PathNode destination){
        RouteChromosome solution = new RouteChromosome();
        StopNode stop, sourceStop;
        Set<StopNode> adjacentStops;
        Mode nextMode;

        int transfers = traits.maxTransfers();
        transfers = Integer.min(traits.maxTransfers(), rnd.nextInt(transfers + 1));

        int transferCnt = 0;

        if(source instanceof StopNode)
            sourceStop = (StopNode) source;

        else {
            adjacentStops = dao.getStopsInRadius(source.getLocation(), traits.transferRadius());
            sourceStop = adjacentStops.stream().skip(rnd.nextInt(adjacentStops.size())).findFirst().orElse(null);
        }

        if(sourceStop == null)
            return null;

        resetContext();

        stop = sourceStop;

        usedStops.add(stop);
        nextMode = Mode.RIDE;


        NetworkTripSegment segment = null;

        do {
            switch (nextMode){
                case RIDE -> {
                    segment = getRideComponent(stop, transferCnt != transfers);
                    solution.add(segment);
                }

                case TRANSFER -> {
                    segment = getTransferComponent(stop, transferCnt != transfers);
                    solution.add(segment);
                    transferCnt++;
                }
            }

            if(segment == null)
                return null;

            stop = segment.getDestination();
            nextMode = drawMode();

        } while (transferCnt < transfers);

        if(destination instanceof StopNode && stop.equals(destination))
            return solution;

        solution.add(new WalkSegment(stop, destination));
        return solution;
    }

    private RideSegment getRideComponent(StopNode stop, boolean continued){
        Set<RideSegment> links = dao.getTransportLinks(stop, continued);

        links.removeIf(segment -> usedTrips.contains(segment.getTripPatternID()) ||
                usedStops.contains(segment.getDestination()));

        if (links.isEmpty())
            return null;

        List<RideSegment> linksList = new ArrayList<>(links);

        return linksList.get(rnd.nextInt(linksList.size()));
    }

    private TransferWalkSegment getTransferComponent(StopNode stop, boolean continued) {
        Set<StopNode> availableStops = dao.getStopsInRadius(stop.getLocation(), traits.transferRadius());

        availableStops.removeIf(stopNode -> usedStops.contains(stopNode));

        List<StopNode> stopList = new ArrayList<>(availableStops);

        StopNode nextStop = stopList.get(rnd.nextInt(stopList.size()));

        return new TransferWalkSegment(stop, nextStop);
    }

    private Mode drawMode(){
        return Mode.values()[rnd.nextInt(Mode.values().length)];
    }
}

