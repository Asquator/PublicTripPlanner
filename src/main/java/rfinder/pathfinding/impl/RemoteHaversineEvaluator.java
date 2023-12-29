package rfinder.pathfinding.impl;

import rfinder.dao.PostgisRouteDAO;
import rfinder.dao.RouteDAO;
import rfinder.pathfinding.graph.HeuristicEvaluator;
import rfinder.structures.nodes.VertexNode;

public class RemoteHaversineEvaluator implements HeuristicEvaluator<VertexNode> {

    private final RouteDAO dao = new PostgisRouteDAO();

    @Override
    public double evaluateHeuristic(VertexNode from, VertexNode to) {
        return dao.getEuclideanDistance(from, to);
    }
}
