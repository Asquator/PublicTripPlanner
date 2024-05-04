package rfinder.service;

import rfinder.client.QuerySubmitter;
import rfinder.dao.*;
import rfinder.dynamic.DynamicContext;
import rfinder.dao.InMemoryTripStorage;
import rfinder.query.result.NetworkQueryContext;
import rfinder.dao.TripRepository;
import rfinder.dynamic.label.AllComputePolicy;
import rfinder.dynamic.label.RelaxedParetoBag;
import rfinder.pathfinding.*;
import rfinder.query.QueryContext;
import rfinder.query.QueryGraphInfo;
import rfinder.query.QueryInfo;
import rfinder.query.result.FullBagExtractor;
import rfinder.query.result.QuerySolution;
import rfinder.query.result.ResultExtractor;
import rfinder.structures.links.ShapedLink;
import rfinder.structures.nodes.PathNode;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Test level local in-memory query submitter
 */
public class LocalQuerySubmitter implements QuerySubmitter {

    private final ExecutorService executor = Executors.newCachedThreadPool();

    private ExternalLinkableGraph<PathNode, ShapedLink> baseRoadGraph;

    private final DefaultEdgeResolver linkageResolver = new DefaultEdgeResolver();

    private final InMemoryTripStorage tripStorage = new InMemoryTripStorage("5m", "3h");

    private final StopStorage stopStorage = new StopStorage(new DefaultStopDAO(), new DefaultRouteDAO());

    private FootpathCache footpathCache;

    private static final double WALK_RADIUS = 1.3;

    public LocalQuerySubmitter() {
        initializeStorage();
    }

    public void initializeStorage(){
        baseRoadGraph = new DefaultGraphDAO().getNetworkGraph();
        footpathCache = new FootpathCache(new DefaultFootpathDAO(), new DefaultTransferDAO(), 1.5 * WALK_RADIUS);

        stopStorage.getStops().forEach(stop -> footpathCache.get(stop, baseRoadGraph));
    }


    @Override
    public CompletableFuture<List<QuerySolution>> submit(QueryInfo queryInfo) {
        ResultExtractor extractor = new FullBagExtractor();

        // DB connections for the query
        RouteDAO routeDAO = new DefaultRouteDAO();
        FootpathDAO footpathDAO = new DefaultFootpathDAO();

        // dynamic context for query execution
        DynamicContext context = new DynamicContext(new DefaultContextRetriever(), routeDAO, footpathDAO);

        // graph linkage info for the query
        QueryGraphInfo graphInfo = new QueryGraphInfo(new DefaultGraphDAO(), queryInfo, linkageResolver);

        // query graph
        ExtendedQueryGraph qgraph = new ExtendedQueryGraph(baseRoadGraph, graphInfo);

        // path finer
        TotalCachedSourcePathFinder<PathNode, ShapedLink> graphPathFinder =
                new CachedFootpathFinder(qgraph, footpathCache);

        // footpath manager
        QueryFootpathManager qPathFinder = new QueryFootpathManager(qgraph, graphPathFinder);

        // relevant trips
        TripRepository tripRepo = tripStorage.createTripRepo(queryInfo);

        context.initializeStorage(() -> new RelaxedParetoBag(queryInfo.criteria(), queryInfo.nonDominating()),
                new QueryContext(queryInfo, graphInfo, qPathFinder, new AllComputePolicy(), tripRepo, stopStorage));

        // the user decides what to do on completion
        return CompletableFuture.supplyAsync(() ->
        {
            NetworkQueryContext nContext = context.compute();
            List<QuerySolution> res = extractor.extract(nContext);
            footpathCache.remove(graphInfo.sourceRepr());
            footpathCache.remove(graphInfo.destinationRepr());

            return res;
        }, executor);
    }

}
