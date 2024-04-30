package rfinder.dynamic;

import rfinder.dao.ContextRetriever;
import rfinder.dao.FootpathDAO;
import rfinder.dao.RouteDAO;
import rfinder.dynamic.label.*;
import rfinder.pathfinding.QueryFootpathManager;
import rfinder.query.QueryContext;
import rfinder.query.QueryGraphInfo;
import rfinder.query.QueryInfo;
import rfinder.structures.common.RouteID;
import rfinder.structures.links.RideLink;
import rfinder.structures.links.WalkLink;
import rfinder.structures.nodes.PathNode;
import rfinder.structures.nodes.StopNode;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class DynamicContext {
    private Supplier<? extends MultilabelBag> bagSupplier;

    private Set<StopNode> markedStops;
    private Set<StopNode> lastRoundMarkedStops;
    private Set<StopNode> markedIsolatedStops;
    private Set<StopNode> lastRoundIsolatedStops;

    private HashMap<RouteID, List<RoundNodeContext<?>>> routeStops;
    private final Map<PathNode, RoundNodeContext<?>> labelRepo = new HashMap<>();
    private QueryContext queryContext;
    private QueryFootpathManager pathFinder;
    private final RouteDAO routeDAO;
    private final FootpathDAO footPathRepo;
    private final ContextRetriever contextRetriever;

    private int currentRound = 0;

    private class RouteScanner {
        private final List<TripInstance> routeTrips;
        private final RouteID routeID;
        private int stopSequence;
        private final MultilabelBag routeBag = bagSupplier.get();
        private final List<RoundNodeContext<?>> currentRouteStops;

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

                RoundNodeContext<?> stopContext = currentRouteStops.get(stopSequence);
                boolean improved = false;

                for(Multilabel label : routeBag){
                    boolean localImproved;
                    // get current trip for label
                    tripSequence = ((RideLink)label.getBackwardLink()).getTripSequence();

                    // update arrival time for label
                    arrivalTime = routeTrips.get(tripSequence).stopTimes().get(stopSequence);
                    label.setArrivalTime(arrivalTime);

                    Multilabel cloned = new Multilabel(label);

                    final long EXTENSION_DURATION_SEC = 60;

                    cloned.setArrivalTime(arrivalTime.plusSeconds(EXTENSION_DURATION_SEC));

                    localImproved = stopContext.addToCurrentRoundBag(cloned);

                    if(localImproved)
                        ((RideLink)cloned.getBackwardLink()).setDestSequence(stopSequence);

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
           final long LOCAL_PRUNE_SEC = 5000L;

            Stream.concat(context.getLastRoundList().stream(), context.getLastIsolatedList().stream()).forEach(
                label -> {
                    LocalDateTime arrivalTime = label.getArrivalTime();

                    int earliestTrip = findEarliestTripIndex(arrivalTime);

                    if(earliestTrip >= 0) {
                        LocalDateTime departureTime = routeTrips.get(earliestTrip).stopTimes().get(stopSequence);

                        if(departureTime.isAfter(arrivalTime.plusSeconds(LOCAL_PRUNE_SEC)))
                            return;

                        Multilabel newLabel = new Multilabel(label);
                        newLabel.update(new RouteBagUpdate(), departureTime);

                        if(routeBag.addEliminate(newLabel))
                            newLabel.setBackwardLink(new RideLink((StopNode) currentRouteStops.get(stopSequence).getPathNode(), stopSequence, routeID, earliestTrip, label));

                    }
                });
        }

        private int findEarliestTripIndex(LocalDateTime minTime){
            int res = findEarliestTripIndex(minTime, tripLow, tripHigh);

            if(res != -1) {
                return res;
            }

            res = findEarliestTripIndex(minTime, 0, routeTrips.size() - 1);

            if(res != -1) {
                tripLow = Math.min(tripLow, res);
                tripHigh = Math.max(tripHigh, res);
            }

            return res;
        }


        private int findEarliestTripIndex(LocalDateTime minTime, int low, int high){
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


    public <M extends Multilabel, B extends MultilabelBag> void initializeStorage(Class<B> bagClass, QueryContext queryContext)  {
        Supplier<B> bagSupplier = () -> {
            try {
                return (B) bagClass.getConstructor().newInstance();
            } catch (Exception ex) {
                throw new RuntimeException("Couldn't retrieve multilabel bag constructor", ex);
            }
        };

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

        Multilabel trivialLabel = new Multilabel();
        trivialLabel.setArrivalTime(queryInfo.departureTime());

        if(queryGraphInfo.sourceRepr() instanceof StopNode stopNode) {
            lastRoundMarkedStops.add(stopNode);
            context = labelRepo.get(stopNode);
        }

        else {
            context = new RoundNodeContext<B>(queryGraphInfo.sourceRepr(), bagSupplier);
            labelRepo.put(queryGraphInfo.sourceRepr(), context);
        }

        context.addToCurrentRoundBag(trivialLabel);

        if(!queryGraphInfo.destinationRepr().isStop()){

            context = new RoundNodeContext<B>(queryGraphInfo.destinationRepr(), bagSupplier);
            labelRepo.put(queryGraphInfo.destinationRepr(), context);
        }

    }

    private void initNextRound(){
        currentRound++;

        // last round, no storage expansion
        if(currentRound > queryContext.queryInfo().maxTrips())
            return;

        lastRoundMarkedStops = markedStops;
        lastRoundIsolatedStops = markedIsolatedStops;

        markedStops = new HashSet<>();
        markedIsolatedStops = new HashSet<>();

        Stream.concat(lastRoundIsolatedStops.stream(), lastRoundMarkedStops.stream()).forEach(stopNode -> {
            RoundNodeContext<?> roundNodeContext = labelRepo.get(stopNode);
            roundNodeContext.step(bagSupplier);
        });
    }

    public NetworkQueryContext compute(){
        RouteScanner scanner;

        processFootpathsFrom(queryContext.queryGraphInfo().sourceRepr());
        MultilabelBag finalBag;

        initNextRound();

        long time;

        while(currentRound < queryContext.queryInfo().maxTrips() + 1){


            System.out.println("collecting routes");

            time = System.currentTimeMillis();
            Map<RouteID, Integer> relevantRoutes = collectRelevantRoutes(); // <route_id, stop_sequence>
            System.out.println("time: " + (System.currentTimeMillis() - time) + "\n");

            System.out.println(currentRound + " round started, processing routes");
            time = System.currentTimeMillis();
            for(RouteID routeID : relevantRoutes.keySet()){
                scanner = new RouteScanner(routeID, relevantRoutes.get(routeID));
                scanner.processRoute();
            }
            System.out.println("time: " + (System.currentTimeMillis() - time)+ "\n");

            System.out.println("footpaths  " + markedStops.size());

            time = System.currentTimeMillis();
            if(currentRound < queryContext.queryInfo().maxTrips())
                processForwardFootpaths();

            else
                assemblyFootpaths(queryContext.queryGraphInfo().destinationRepr());

            System.out.println("time: " + (System.currentTimeMillis() - time)+ "\n");

            initNextRound();
        }

        finalBag = labelRepo.get(queryContext.queryGraphInfo().destinationRepr()).getBestBag();
        return new NetworkQueryContext(queryContext, routeDAO, finalBag);
    }



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

    private void assemblyFootpaths(PathNode to){

        RoundNodeContext<?> targetContext = labelRepo.get(to);

        pathFinder.getFootpaths(to).forEach(stop -> {

            RoundNodeContext<?> sourceStop = labelRepo.getOrDefault(stop.target(), null);

            if(sourceStop == null)
                return;

            for(Multilabel multilabel : sourceStop.getCurrentRoundList()){
                
                Multilabel cloned = new Multilabel(multilabel);
                cloned.setBackwardLink(new WalkLink(stop.target(), multilabel));

                // update arrival time and walking distance
                cloned.update(new FootpathUpdatePolicy(), stop.weight());

                targetContext.addToCurrentIsolatedBag(cloned);
            }
        });
    }

    private void processForwardFootpaths(){
        for (StopNode markedStop : markedStops) {
            processFootpathsFrom(markedStop);
        }
    }

    private void processFootpathsFrom(PathNode from) {
        pathFinder.getFootpaths(from).forEach(stop -> {
            boolean improved = false;
            RoundNodeContext<?> targetStop = labelRepo.getOrDefault(stop.target(), null);

            if(targetStop == null)
                return;

            for(Multilabel multilabel : labelRepo.get(from).getCurrentRoundList()){

                // backward link to source node
                Multilabel cloned = new Multilabel(multilabel);
                WalkLink backwardLink = new WalkLink(from, multilabel);

                // update arrival time and walking distance
                cloned.update(new FootpathUpdatePolicy(), stop.weight());

                cloned.setBackwardLink(backwardLink);

                improved = targetStop.addToCurrentIsolatedBag(cloned) || improved;
            }

            if(improved)
                markedIsolatedStops.add((StopNode) stop.target());
        });

    }


}
