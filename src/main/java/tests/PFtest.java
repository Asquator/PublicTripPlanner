/*
package tests;

import rfinder.dao.DefaultDAO;
import rfinder.pathfinding.ExtendedQueryGraph;
import rfinder.pathfinding.InMemoryNetworkGraph;
import rfinder.pathfinding.QueryPathFinder;
import rfinder.query.*;
import rfinder.structures.graph.RoutableGraph;
import rfinder.structures.nodes.PathNode;

import java.time.OffsetDateTime;

public class PFtest {
    public static void main(String[] args) {
        DefaultDAO dao = new DefaultDAO();
        RoutableGraph<PathNode> graph = new InMemoryNetworkGraph(new DefaultDAO());
        QueryInfo info1 = new QueryInfo(new StopPoint(2046), new StopPoint(1926), OffsetDateTime.now(), 4, 55);

        QueryPathFinder pathFinder = new QueryPathFinder(graph, info1);

        PathNode source = pathFinder.getSourceRepr();
        PathNode destination = pathFinder.getDestinationRepr();

        System.out.println(pathFinder.findPath(source, destination));

    }
}
*/
