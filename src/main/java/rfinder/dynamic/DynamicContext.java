package rfinder.dynamic;

import javafx.scene.paint.Stop;
import rfinder.dao.*;
import rfinder.model.network.walking.ExtendedQueryGraph;
import rfinder.pathfinding.QueryPathFinder;
import rfinder.query.NodeLinkageResolver;
import rfinder.query.QueryGraphInfo;
import rfinder.query.QueryInfo;
import rfinder.structures.common.RouteID;
import rfinder.structures.components.PathLink;
import rfinder.structures.components.WalkLink;
import rfinder.structures.graph.RoutableGraph;
import rfinder.structures.nodes.PathNode;
import rfinder.structures.nodes.StopNode;

import java.io.IOException;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.*;

public class DynamicContext {
    private Set<StopNode> markedStops;
    private Set<StopNode> lastRoundMarkedStops;
    private HashMap<RouteID, List<RoundStopContext>> routeStops;
    private final Map<Integer, RoundStopContext> labelRepo = new HashMap<>();
    private QueryPathFinder pathFinder;
    private final RouteDAO routeDAO;
    private final FootpathDAO footPathRepo;
    private final ContextRetriever contextRetriever;

    private final TripRepository tripRepository = new TripRepository();
    private QueryInfo queryInfo;
    private int currentRound = 0;

    private class RouteScanner {
        private final List<TripInstance> routeTrips;

        private final RouteID routeID;

        private int stopSequence;

        private RouteMultilabelBag routeBag = new RouteMultilabelBag();

        private List<RoundStopContext> currentRouteStops;

        private int tripLow, tripHigh;

        public RouteScanner(RouteID routeID, int initStopSequence){
            this.stopSequence = initStopSequence;
            this.routeID = routeID;

            currentRouteStops = routeStops.get(routeID);
            routeTrips = tripRepository.getRelevantTrips(routeID);

            tripLow = routeTrips.size() - 1;
            tripHigh = 0;
        }

        public void processRoute() {
            MultilabelBag stopBag;
            OffsetDateTime arrivalTime;
            int tripSequence;

            while (stopSequence < currentRouteStops.size()) {

                RoundStopContext stopContext = currentRouteStops.get(stopSequence);
                stopBag = stopContext.getRoundLabels()[currentRound];

                for(RouteMultilabel label : routeBag){

                    // get current trip for label
                    tripSequence = label.getTripSequence();

                    // update arrival time for label
                    arrivalTime = routeTrips.get(tripSequence).stopTimes().get(stopSequence);
                    label.setArrivalTime(arrivalTime);
                }

                // merge the route bag into the current stop bag
                boolean improved = stopBag.mergeRouteBag(routeBag);

                if(improved)
                    markedStops.add(stopContext.getStopNode());

                // merge the previous round bag into the sliding route bag
                mergeIntoRouteBag(stopContext.getRoundLabels()[currentRound - 1]);

                stopSequence++;
            }
        }

        private void mergeIntoRouteBag(MultilabelBag bag){
            int earliestTripSequence;

            for(Multilabel label : bag){
                OffsetDateTime arrivalTime = label.getArrivalTime();

                earliestTripSequence = findEarliestTripIndex(arrivalTime);

                if(earliestTripSequence >= 0) {
                    // add waiting duration to the corresponding label
                    OffsetDateTime departureTime = routeTrips.get(earliestTripSequence).stopTimes().get(stopSequence);
                    RouteMultilabel newLabel = new RouteMultilabel(label, currentRouteStops.get(stopSequence).getStopNode(), earliestTripSequence);
                    DurationMinLabel durationLabel = (DurationMinLabel) newLabel.getLabel(ECriteria.WAITING_TIME);
                    durationLabel.addDuration(Duration.between(arrivalTime, departureTime));
                    routeBag.add(newLabel);
                }
            }
        }

        private int findEarliestTripIndex(OffsetDateTime minTime){
            int res = findEarliestTripIndex(minTime, tripLow, tripHigh);

            if(res != -1) {
                return res;
            }

            res = findEarliestTripIndex(minTime, 0, routeTrips.size() - 1);

            tripLow = Math.min(tripLow, res);
            tripHigh = Math.max(tripHigh, res);

            return res;
        }


        private int findEarliestTripIndex(OffsetDateTime minTime, int low, int high){
            // binary search for earliest trip for the given stop across columns of stopTimes
             int mid;
             int result = -1;


             while (low <= high) {
                 mid = (low + high) / 2;
                 TripInstance tripInstance = routeTrips.get(mid);

                 if (minTime.isBefore(tripInstance.stopTimes().get(stopSequence))) {
                     result = mid;
                     high = mid - 1;
                 } else {
                     low = mid + 1;
                 }
             }

             return result;
         }
    }

    public DynamicContext(ContextRetriever contextRetriever, RouteDAO routeDAO, FootpathDAO footpathDAO){

        this.contextRetriever = contextRetriever;
        this.routeDAO = routeDAO;
        this.footPathRepo = footpathDAO;
    }

    public HashMap<RouteID, List<RoundStopContext>> getRouteStops() {
        return routeStops;
    }

    public Map<Integer, RoundStopContext> getLabelRepo() {
        return labelRepo;
    }

    public void initializeStorage(RoutableGraph<PathNode> baseGraph, QueryInfo queryInfo){
        this.queryInfo = queryInfo;

        routeStops = new HashMap<>();
        markedStops = new HashSet<>();
        lastRoundMarkedStops = new HashSet<>();

        Set<RouteID> relevantRoutes = contextRetriever.getRelevantRoutes(queryInfo);
        ExtendedQueryGraph qgraph = new ExtendedQueryGraph(baseGraph, new QueryGraphInfo(queryInfo, new NodeLinkageResolver()));

        for(RouteID routeID : relevantRoutes){
            List<StopNode> stopList = routeDAO.getStops(routeID);
            List<RoundStopContext> roundStopContextList = routeStops.getOrDefault(routeID, new ArrayList<>());

            for(StopNode stop : stopList){
                // retrieve stop if already exists
                RoundStopContext roundStopContext = labelRepo.getOrDefault(stop.id(), new RoundStopContext(stop, queryInfo.maxTrips() + 1));

                // initialize first iteration bag
                roundStopContext.getRoundLabels()[currentRound] = new MultilabelBag();

                roundStopContextList.add(roundStopContext);

                // save new stop if needed
                labelRepo.put(stop.id(), roundStopContext);
            }

            routeStops.put(routeID, roundStopContextList);
        }

        pathFinder = new QueryPathFinder(qgraph, queryInfo);

        try {
            tripRepository.updateStopTimes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if(pathFinder.getSourceRepr() instanceof StopNode stopNode)
            lastRoundMarkedStops.add(stopNode);
    }

    private void initNextRound(){
        currentRound++;

        // last round, no storage expansion
        if(currentRound > queryInfo.maxTrips())
            return;

        lastRoundMarkedStops = markedStops;
        markedStops = new HashSet<>();

        for(RoundStopContext roundStopContext : labelRepo.values()){
            MultilabelBag[] roundLabels = roundStopContext.getRoundLabels();
            roundLabels[currentRound] = new MultilabelBag(roundLabels[currentRound - 1]);
        }
    }

    public void compute(){
        RouteScanner scanner;
        MultilabelBag initialBag, finalBag;

        if(pathFinder.getSourceRepr() instanceof StopNode stopNode) {
            initialBag = labelRepo.get(stopNode.id()).getRoundLabels()[0];
            initialBag.add(new Multilabel());
        }

        else
            initialBag = new MultilabelBag();

        if(pathFinder.getDestinationRepr() instanceof StopNode stopNode)
            finalBag = labelRepo.get(stopNode.id()).getRoundLabels()[0];

        else
            finalBag = new MultilabelBag();

        processFootpathsFrom(pathFinder.getSourceRepr(), initialBag, 0);

        initNextRound();

        while(currentRound < queryInfo.maxTrips() + 1){
            Map<RouteID, Integer> relevantRoutes = collectRelevantRoutes(); // <route_id, stop_sequence>

//          System.out.println(currentRound + " trip");
            for(RouteID routeID : relevantRoutes.keySet()){
                scanner = new RouteScanner(routeID, relevantRoutes.get(routeID));
                scanner.processRoute();
            }

            if(currentRound < queryInfo.maxTrips())
                processForwardFootpaths();

            else
                System.out.println(assemblyFootpaths(pathFinder.getDestinationRepr(), finalBag, currentRound));

            initNextRound();
        }

    }



    private Map<RouteID, Integer> collectRelevantRoutes() {
        Map<RouteID, Integer> relevantRoutes = new HashMap<>();

        for (StopNode stopNode : lastRoundMarkedStops) {
            for (Map.Entry<RouteID, Integer> stopInRoute : routeDAO.getRoutes(stopNode)) {

                // if stopNode comes earlier than current stop in the given route, save the earliest stop in the route
                int seq = relevantRoutes.getOrDefault(stopInRoute.getKey(), Integer.MAX_VALUE);

                if(stopInRoute.getValue() < seq)
                    relevantRoutes.put(stopInRoute.getKey(), stopInRoute.getValue());
            }
        }

        return relevantRoutes;
    }

    Set<StopNode> getFootPaths(PathNode center){
        if(center instanceof StopNode stopnode)
            return footPathRepo.getFootPaths(stopnode, queryInfo.walkRadius());

        else
            return footPathRepo.getFootPaths(center.getLocation(), queryInfo.walkRadius());
    }

    private MultilabelBag assemblyFootpaths(PathNode to, MultilabelBag targetBag, int inputRound){
        Set<StopNode> footPaths = getFootPaths(to);

        for(StopNode stop : footPaths){

            RoundStopContext sourceStop = labelRepo.getOrDefault(stop.id(), null);
            if(sourceStop == null)
                continue;

            OptionalDouble pathCost = pathFinder.pathCost(to, stop);

            if(pathCost.isEmpty()){
                continue;
            }

            // copy the source bag
            MultilabelBag sourceBag = new MultilabelBag(sourceStop.getRoundLabels()[inputRound]);

            // update arrival time and walking distance
            for (Multilabel multilabel : sourceBag) {
                TimeMinLabel timeLabel = (TimeMinLabel) multilabel.getLabel(ECriteria.ARRIVAL_TIME);
                timeLabel.setTimestamp(timeLabel.getTimestamp().plus(Converters.kmToDuration(pathCost.getAsDouble())));

                DoubleMinLabel walkLabel = (DoubleMinLabel) multilabel.getLabel(ECriteria.WALKING_KM);
                walkLabel.setCost(walkLabel.getCost() + pathCost.getAsDouble());
                multilabel.setBackwardLink(new WalkLink(stop));
            }

            targetBag.addAll(sourceBag);
        }

        return targetBag;
    }

    private void processForwardFootpaths(){
        for (StopNode markedStop : lastRoundMarkedStops) {
            processFootpathsFrom(markedStop, labelRepo.get(markedStop.id()).getRoundLabels()[currentRound - 1], currentRound);
        }
    }

    private void processFootpathsFrom(PathNode from, MultilabelBag sourceBag, int outputRound) {
        Set<StopNode> footPaths = getFootPaths(from);

        // backward link to source node
        WalkLink backwardLink = new WalkLink(from);

        // iterate over all footpaths
        for(StopNode stop : footPaths){
            RoundStopContext targetStop = labelRepo.getOrDefault(stop.id(), null);
            if(targetStop == null)
                continue;

            OptionalDouble pathCost = pathFinder.pathCost(from, stop);

            if(pathCost.isEmpty()){
                continue;
            }

            // copy the source bag
            MultilabelBag tempBag = new MultilabelBag(sourceBag);

            // update arrival time and walking distance
            for (Multilabel multilabel : tempBag) {
                TimeMinLabel timeLabel = (TimeMinLabel) multilabel.getLabel(ECriteria.ARRIVAL_TIME);
                timeLabel.setTimestamp(timeLabel.getTimestamp().plus(Converters.kmToDuration(pathCost.getAsDouble())));

                DoubleMinLabel walkLabel = (DoubleMinLabel) multilabel.getLabel(ECriteria.WALKING_KM);
                walkLabel.setCost(walkLabel.getCost() + pathCost.getAsDouble());
                multilabel.setBackwardLink(backwardLink);
            }

            // merge the temporary bag into the existing one
            MultilabelBag roundBag = targetStop.getRoundLabels()[outputRound];
            boolean improved = roundBag.addAll(tempBag);

            // if the bag has been improved, mark the stop
            int oldSize = roundBag.size();
            if(roundBag.size() == oldSize || improved)
                markedStops.add(stop);

        }

    }

    @Override
    public String toString() {
        return "DynamicContext{" +
                "markedStops=" + markedStops +
                ", routeStops=" + routeStops + '}';
    }

}
