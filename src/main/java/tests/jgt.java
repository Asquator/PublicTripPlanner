package tests;

import rfinder.dao.DefaultDAO;
import rfinder.dao.RoadDAO;
import rfinder.model.network.walking.InMemoryNetworkGraph;
import rfinder.pathfinding.CachedAsPathFinder;
import rfinder.pathfinding.GraphPathFinder;
import rfinder.pathfinding.HaversineDistanceEvaluator;
import rfinder.pathfinding.Path;
import rfinder.structures.graph.RoutableGraph;
import rfinder.structures.nodes.PathNode;
import rfinder.structures.nodes.VertexNode;

public class jgt {
    public static void main(String[] args) {
        InMemoryNetworkGraph graph = new InMemoryNetworkGraph(new DefaultDAO());
        GraphPathFinder<PathNode> graphPathFinder = new CachedAsPathFinder<>(graph, new HaversineDistanceEvaluator(), 60003);



        RoadDAO rdao = new DefaultDAO();


        VertexNode source = rdao.getVertexByID(16562);
        VertexNode dest = rdao.getVertexByID(1093);

        System.out.println(graphPathFinder.findPath(source, dest));
    }
}
