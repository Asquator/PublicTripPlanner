package rfinder.dynamic;

import rfinder.dao.ContextRetriever;
import rfinder.dao.FootpathDAO;
import rfinder.dao.RouteDAO;
import rfinder.dynamic.label.*;
import rfinder.pathfinding.QueryFootpathFinder;
import rfinder.query.QueryContext;
import rfinder.query.QueryGraphInfo;
import rfinder.query.QueryInfo;
import rfinder.query.result.NetworkQueryContext;
import rfinder.structures.common.RouteID;
import rfinder.structures.common.TripInstance;
import rfinder.structures.links.RideLink;
import rfinder.structures.links.WalkLink;
import rfinder.structures.nodes.PathNode;
import rfinder.structures.nodes.StopNode;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class DynamicContext {
    private Supplier<? extends MultilabelBag> bagSupplier;

    // collections of marked stops
    private Set<StopNode> markedStops;
    private Set<StopNode> lastRoundMarkedStops;
    private Set<StopNode> markedIsolatedStops;
    private Set<StopNode> lastRoundIsolatedStops;

    // list of bag contexts for each route's stops
    private HashMap<RouteID, List<RoundNodeContext<?>>> routeStops;

    // round contexts storage
    private final Map<PathNode, RoundNodeContext<?>> labelRepo = new HashMap<>();
    private QueryContext queryContext;
    private QueryFootpathFinder pathFinder;
    private final RouteDAO routeDAO;
    private final FootpathDAO footPathRepo;
    private final ContextRetriever contextRetriever;

    // current execution round
    private int currentRound = 0;

    private class RouteScanner {
        private final List<TripInstance> routeTrips;
        private final RouteID routeID;
        private int stopSequence;

        // route bag of labels
        private final MultilabelBag routeBag = bagSupplier.get();
        private final List<RoundNodeContext<?>> currentRouteStops;

        // trip sequence bounds
        private int tripLow, tripHigh;

        public RouteScanner(RouteID routeID, int initStopSequence){
            this.stopSequence = initStopSequence;
            this.routeID = routeID;

            currentRouteStops = routeStops.get(routeID);
            routeTrips = queryContext.tripRepository().getRelevantTrips(routeID);

            tripLow = routeTrips.size() - 1;
            tripHigh = 0;
        }


        public void processRoute() {
            LocalDateTime arrivalTime;
            int tripSequence;

            if(routeTrips.isEmpty())
                return;

            while (stopSequence < currentRouteStops.size()) {

                // retrieve stop context
                RoundNodeContext<?> stopContext = currentRouteStops.get(stopSequence);
                boolean improved = false;

                // for each label in the route bag
                for(Multilabel label : routeBag){
                    boolean localImproved;
                    // get current trip for label
                    tripSequence = ((RideLink)label.getBackwardLink()).getTripSequence();

                    // update arrival time for label
                    arrivalTime = routeTrips.get(tripSequence).stopTimes().get(stopSequence);
                    label.setArrivalTime(arrivalTime);

                    // get copy of the label
                    Multilabel cloned = new Multilabel(label);

                    // try to add label into the bag and check if succeeded
                    localImproved = stopContext.addToCurrentRoundBag(cloned);

                    // on success, update the backward link
                    if(localImproved)
                        ((RideLink)cloned.getBackwardLink()).setDestSequence(stopSequence);

                    // accumulate the improvement
                    improved = improved || localImproved;
                }

                if(improved)
                    markedStops.add((StopNode) stopContext.getPathNode());

                // merge the previous round bag into the sliding route bag
                mergeIntoRouteBag(stopContext);

                stopSequence++;
            }
        }

        private void mergeIntoRouteBag(RoundNodeContext<?> context){

            // for each label improved in the previous round
            Stream.concat(context.getLastRoundList().stream(), context.getLastIsolatedList().stream()).forEach(
                label -> {

                    // get earliest trip departing after label's arrival time
                    int earliestTrip = findEarliestTripIndex(label.getArrivalTime());

                    // if the trip was found
                    if(earliestTrip >= 0) {
                        // get trip's departure time
                        LocalDateTime departureTime = routeTrips.get(earliestTrip).stopTimes().get(stopSequence);

                        // make copy of the label
                        Multilabel newLabel = new Multilabel(label);

                        // update the parameters
                        if(queryContext.prunePolicy().updatePruneMergeRoute(newLabel, departureTime))
                            return;

                        // add the label into the route bag
                        if(routeBag.addEliminate(newLabel))
                            newLabel.setBackwardLink(new RideLink((StopNode) currentRouteStops.get(stopSequence).getPathNode(), stopSequence, routeID, earliestTrip, label));
                    }
                });
        }

        private int findEarliestTripIndex(LocalDateTime minTime){
            // try to find using pruned values
            int res = findEarliestTripIndex(minTime, tripLow, tripHigh);

            // if found, return
            if(res != -1) {
                return res;
            }

            // otherwise, search over the whole trip range
            res = findEarliestTripIndex(minTime, 0, routeTrips.size() - 1);

            if(res != -1) {
                tripLow = Math.min(tripLow, res);
                tripHigh = Math.max(tripHigh, res);
            }

            return res;
        }


        // binary search for earliest trip for the given stop across columns of stopTimes
        private int findEarliestTripIndex(LocalDateTime minTime, int low, int high){
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


    public <M extends Multilabel, B extends MultilabelBag> void initializeStorage(Supplier<B> bagSupplier, QueryContext queryContext)  {
        this.bagSupplier = bagSupplier;
        this.pathFinder = queryContext.pathFinder();
        this.queryContext = queryContext;

        routeStops = new HashMap<>();
        markedStops = new HashSet<>();
        lastRoundMarkedStops = new HashSet<>();
        markedIsolatedStops = new HashSet<>();
        lastRoundIsolatedStops = new HashSet<>();


        QueryInfo queryInfo = queryContext.queryInfo();
        QueryGraphInfo queryGraphInfo = queryContext.queryGraphInfo();

        Set<RouteID> relevantRoutes = contextRetriever.getRelevantRoutes(queryInfo);

        // create a context repository and organize contexts into lists by RouteID
        for(RouteID routeID : relevantRoutes){
            List<StopNode> stopList = routeDAO.getStops(routeID);
            List<RoundNodeContext<?>> roundNodeContextList = routeStops.getOrDefault(routeID, new ArrayList<>());

            for(StopNode stop : stopList){
                // retrieve stop if already exists
                RoundNodeContext<?> roundNodeContext = labelRepo.getOrDefault(stop, new RoundNodeContext<B>(stop, bagSupplier));

                roundNodeContextList.add(roundNodeContext);

                // save new stop if needed
                labelRepo.put(stop, roundNodeContext);
            }

            routeStops.put(routeID, roundNodeContextList);
        }

        RoundNodeContext<?> context;

        if(queryGraphInfo.sourceRepr() instanceof StopNode stopNode) {
            lastRoundMarkedStops.add(stopNode);
            context = labelRepo.get(stopNode);
        }

        else {
            // create contexts for source
            context = new RoundNodeContext<B>(queryGraphInfo.sourceRepr(), bagSupplier);
            labelRepo.put(queryGraphInfo.sourceRepr(), context);
        }

        // add trivial label to the source bag
        Multilabel trivialLabel = new Multilabel();
        trivialLabel.setArrivalTime(queryInfo.departureTime());
        context.addToCurrentRoundBag(trivialLabel);

        if(!queryGraphInfo.destinationRepr().isStop()){
            // create context for destination
            context = new RoundNodeContext<B>(queryGraphInfo.destinationRepr(), bagSupplier);
            labelRepo.put(queryGraphInfo.destinationRepr(), context);
        }

    }

    // initialize next round
    private void initNextRound(){
        currentRound++;

        // last round, no storage expansion
        if(currentRound > queryContext.queryInfo().maxTrips())
            return;

        lastRoundMarkedStops = markedStops;
        lastRoundIsolatedStops = markedIsolatedStops;

        markedStops = new HashSet<>();
        markedIsolatedStops = new HashSet<>();

        // prepare structures for the next round for those contexts that were improved
        Stream.concat(lastRoundIsolatedStops.stream(), lastRoundMarkedStops.stream()).forEach(stopNode -> {
            RoundNodeContext<?> roundNodeContext = labelRepo.get(stopNode);
            roundNodeContext.step(bagSupplier);
        });
    }

    public NetworkQueryContext compute(){
        RouteScanner scanner;

        tryDirectCast();

        // process footpaths from source
        processFootpathsFrom(queryContext.queryGraphInfo().sourceRepr());
        MultilabelBag finalBag;

        initNextRound();

        // main computation loop
        while(currentRound < queryContext.queryInfo().maxTrips() + 1){

            // collect improved roots and process
            Map<RouteID, Integer> relevantRoutes = collectRelevantRoutes(); // <route_id, stop_sequence>

            for(RouteID routeID : relevantRoutes.keySet()){
                scanner = new RouteScanner(routeID, relevantRoutes.get(routeID));
                scanner.processRoute();
            }

            // decided whether to process forward footpaths or assembly to the destination
            if(currentRound < queryContext.queryInfo().maxTrips())
                processForwardFootpaths();

            else
                assemblyFootpaths(queryContext.queryGraphInfo().destinationRepr());

            initNextRound();
        }

        // get final bag
        finalBag = labelRepo.get(queryContext.queryGraphInfo().destinationRepr()).getBestBag();
        return new NetworkQueryContext(queryContext, routeDAO, finalBag);
    }


    private void tryDirectCast() {
        PathNode source = queryContext.queryGraphInfo().sourceRepr();
        OptionalDouble pathCost = pathFinder.directPathCost();

        if(pathCost.isEmpty())
            return;

        for(Multilabel multilabel : labelRepo.get(queryContext.queryGraphInfo().sourceRepr()).getCurrentRoundList()){

            // backward link to source node
            Multilabel cloned = new Multilabel(multilabel);
            WalkLink backwardLink = new WalkLink(source, multilabel);

            // update arrival time and walking distance
            queryContext.prunePolicy().updateFootpaths(cloned, pathCost.getAsDouble());

            cloned.setBackwardLink(backwardLink);
            labelRepo.get(queryContext.queryGraphInfo().destinationRepr()).addToCurrentIsolatedBag(cloned);
        }

    }


    // return roots to scan in the next round, along with starting stop sequences
    private Map<RouteID, Integer> collectRelevantRoutes() {
        Map<RouteID, Integer> relevantRoutes = new HashMap<>();

        Stream.concat(lastRoundIsolatedStops.stream(), lastRoundMarkedStops.stream()).forEach(stopNode -> {
            for (Map.Entry<RouteID, Integer> stopInRoute : queryContext.stopStorage().getRoutes(stopNode)) {

                // if stopNode comes earlier than current stop in the given route, save the earliest stop in the route
                int seq = relevantRoutes.getOrDefault(stopInRoute.getKey(), Integer.MAX_VALUE);

                if(stopInRoute.getValue() < seq)
                    relevantRoutes.put(stopInRoute.getKey(), stopInRoute.getValue());
            }
        });

        return relevantRoutes;
    }


    // transfer footpaths from best bags to the destination
    private void assemblyFootpaths(PathNode to){

        RoundNodeContext<?> targetContext = labelRepo.get(to);

        pathFinder.getFootpaths(to).forEach(stop -> {

            RoundNodeContext<?> sourceStop = labelRepo.getOrDefault(stop.target(), null);

            if(sourceStop == null)
                return;

            // for each multilabel which is not an isolated label (by checking if the backward link is a walk link)
            sourceStop.getBestBag().stream().filter(multilabel -> !(multilabel.getBackwardLink() instanceof WalkLink)).forEach((
                    multilabel -> {
                        // make copy of the label
                        Multilabel cloned = new Multilabel(multilabel);
                        cloned.setBackwardLink(new WalkLink(stop.target(), multilabel));

                        // update arrival time and walking distance
                        queryContext.prunePolicy().updateFootpaths(cloned, stop.weight());

                        targetContext.addToCurrentIsolatedBag(cloned);
                    })

            );

        });
    }


    // convenience method to process footpaths
    private void processForwardFootpaths(){
        for (StopNode markedStop : markedStops) {
            processFootpathsFrom(markedStop);
        }
    }

    // process footpaths from a given stop
    private void processFootpathsFrom(PathNode from) {
        pathFinder.getFootpaths(from).forEach(stop -> {
            boolean improved = false;
            RoundNodeContext<?> targetStop = labelRepo.getOrDefault(stop.target(), null);

            if(targetStop == null)
                return;

            // only for improved multilabels which are not isolated
            for(Multilabel multilabel : labelRepo.get(from).getCurrentRoundList()){

                // backward link to source node
                Multilabel cloned = new Multilabel(multilabel);
                WalkLink backwardLink = new WalkLink(from, multilabel);

                // update arrival time and walking distance
                queryContext.prunePolicy().updateFootpaths(cloned, stop.weight());

                cloned.setBackwardLink(backwardLink);

                improved = targetStop.addToCurrentIsolatedBag(cloned) || improved;
            }

            if(improved)
                markedIsolatedStops.add((StopNode) stop.target());
        });

    }
}
