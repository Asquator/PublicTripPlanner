package rfinder.genetic.algorithm;

import rfinder.genetic.structures.RouteChromosome;
import rfinder.dao.RouteDAO;
import rfinder.genetic.structures.PopulationTraits;
import rfinder.structures.common.Location;
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

    private static class NotFoundException extends Exception {
        public NotFoundException(Location location) {
            super("No continuation found from " + location);
        }
    }

    public enum TransferMode {
        DIRECT, WALK
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
            if(solution == null)
                continue;

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
        StopNode stop, sourceStop;
        NetworkTripLink link;
        TransferMode nextMode;

        int rides = rnd.nextInt(traits.maxTransfers()) + 1;
        int ridesCnt = 0;

        resetContext();

        nextMode = source instanceof StopNode ? drawMode() : TransferMode.WALK;

        try {
            sourceStop = switch (nextMode){
                case DIRECT ->  (StopNode) source;
                case WALK -> getRandomAdjacentStop(source.getLocation());
            };
        }
        catch (NotFoundException ex) {
            return null;
        }

        RouteChromosome solution = new RouteChromosome(source);

        if(nextMode == TransferMode.WALK)
            solution.add(new WalkLink(sourceStop));

        stop = sourceStop;
        usedStops.add(stop);

        do {
            ridesCnt++;

            // draw next transfer mode
            nextMode = drawMode();

            try {
                link = getRideComponent(stop, nextMode == TransferMode.DIRECT &&
                        ridesCnt < rides);
            } catch (NotFoundException ex){ // no continuation from here
                return null;
            }

            usedTrips.add(((RideLink)link).getTripPatternID());
            usedStops.add(link.getDestination());
            solution.add(link);
            stop = link.getDestination();

            if(ridesCnt < rides && nextMode == TransferMode.WALK)
                try {
                    link = getTransferComponent(stop);
                    solution.add(link);
                    stop = link.getDestination();
                    usedStops.add(stop);
                }
                catch (NotFoundException ex){
                    return null;
                }
        } while (ridesCnt < rides);

        if(destination instanceof StopNode && stop.equals(destination))
            return solution;

        solution.add(new WalkLink(destination));
        return solution;
    }

    private RideLink getRideComponent(StopNode stop, boolean continued) throws NotFoundException {
        Set<RideLink> links = dao.getTransportLinks(stop, continued);

        links.removeIf(link -> usedTrips.contains(link.getTripPatternID()) ||
                usedStops.contains(link.getDestination()));

        if (links.isEmpty())
            throw new NotFoundException(stop.getLocation());

        List<RideLink> linksList = new ArrayList<>(links);

        return linksList.get(rnd.nextInt(linksList.size()));
    }


    private TransferWalkLink getTransferComponent(StopNode stop) throws NotFoundException {
        StopNode nextStop = getRandomAdjacentStop(stop.getLocation());
        return new TransferWalkLink(nextStop);
    }


    private StopNode getRandomAdjacentStop(Location location) throws NotFoundException {
        Set<StopNode> availableStops = dao.getStopsInRadius(location, traits.transferRadius());

        availableStops.removeIf(stopNode -> usedStops.contains(stopNode));

        List<StopNode> stopList = new ArrayList<>(availableStops);
        if(stopList.isEmpty())
            throw new NotFoundException(location);

        return stopList.get(rnd.nextInt(stopList.size()));
    }

    private TransferMode drawMode(){
        return TransferMode.values()[rnd.nextInt(TransferMode.values().length)];
    }
}

