package tests;

import rfinder.dao.PostgisDAO;
import rfinder.dao.RoadDAO;
import rfinder.dao.RouteDAO;
import rfinder.structures.graph.Graph;
import rfinder.pathfinding.graph.RouteFinder;
import rfinder.pathfinding.impl.HaversineDistanceEvaluator;
import rfinder.model.network.walking.InMemoryPostgisVertexGraph;
import rfinder.model.network.walking.PostgisVertexGraph;
import rfinder.structures.nodes.VertexNode;

import java.util.List;

public class RouteFinderTest {
    public static void main(String[] args) throws Exception {
        RoadDAO dao = new PostgisDAO();

        InMemoryPostgisVertexGraph inMemoryGraph = new InMemoryPostgisVertexGraph((PostgisDAO) dao);

        Graph<VertexNode> graph = new PostgisVertexGraph();
        RouteFinder rf = new RouteFinder(inMemoryGraph, new HaversineDistanceEvaluator());

        VertexNode node1 = dao.getVertexByID(13194);
        VertexNode node2 = dao.getVertexByID(10446);

        long startTime = System.nanoTime();
        List<VertexNode> path = rf.computeAStar(node1, node2);
        long stopTime = System.nanoTime();
        System.out.println(stopTime - startTime);

        for(VertexNode node : path)
            System.out.println("Next " + node);


        System.out.println(path.size());

        node1 = dao.getVertexByID(3);
        node2 = dao.getVertexByID(10446);

        startTime = System.nanoTime();
        path = rf.computeAStar(node1, node2);
        stopTime = System.nanoTime();
        System.out.println(stopTime - startTime);

        for(VertexNode node : path)
            System.out.println("Next " + node);


        System.out.println(path.size());

        node1 = dao.getVertexByID(425);
        node2 = dao.getVertexByID(1044);

        startTime = System.nanoTime();
        path = rf.computeAStar(node1, node2);
        stopTime = System.nanoTime();
        System.out.println(stopTime - startTime);


        for(VertexNode node : path)
            System.out.println("Next " + node);

        System.out.println(path.size());
    }
}
