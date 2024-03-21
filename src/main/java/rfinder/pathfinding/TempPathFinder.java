package rfinder.pathfinding;

import rfinder.dao.DefaultFootpathDAO;
import rfinder.dao.FootpathDAO;
import rfinder.structures.nodes.PathNode;
import rfinder.structures.nodes.VertexNode;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

public class TempPathFinder implements GraphPathFinder<VertexNode> {

    private final GraphPathFinder<VertexNode> graphPathFinder;
    private final FootpathDAO footpathDAO = new DefaultFootpathDAO();

    public TempPathFinder(GraphPathFinder<VertexNode> graphPathFinder){
        this.graphPathFinder = graphPathFinder;
    }

    @Override
    public GraphPath<VertexNode> findPath(VertexNode from, VertexNode to) {

        GraphPath<VertexNode> graphPath =
                graphPathFinder.findPath(footpathDAO.getClosestVertex(from.getLocation()), footpathDAO.getClosestVertex(to.getLocation()));

        if(graphPath == null) {
            System.out.println("unroutable path: " + footpathDAO.getClosestVertex(from.getLocation()) + " -> " + footpathDAO.getClosestVertex(to.getLocation()));
            return null;

        }

        List<? extends PathNode> path = graphPath.getPath();
        return new GraphPath(path, graphPath.getLength());
    }

    @Override
    public OptionalDouble pathCost(VertexNode from, VertexNode to) {
        OptionalDouble ret =
                graphPathFinder.pathCost(footpathDAO.getClosestVertex(from.getLocation()), footpathDAO.getClosestVertex(to.getLocation()));

        if(ret.isPresent())
            return ret;

        System.out.println("unroutable path: " + footpathDAO.getClosestVertex(from.getLocation()) + " -> " + footpathDAO.getClosestVertex(to.getLocation()));
        return OptionalDouble.empty();
    }
}
