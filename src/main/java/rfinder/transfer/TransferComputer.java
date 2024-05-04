package rfinder.transfer;

import rfinder.dao.*;
import rfinder.pathfinding.AsSourceContext;
import rfinder.pathfinding.InMemoryNetworkGraph;
import rfinder.pathfinding.PathRecord;
import rfinder.pathfinding.PrecomputedAsContext;
import rfinder.pathfinding.heuristic.EuclideanDistanceEvaluator;
import rfinder.structures.graph.RoutableGraph;
import rfinder.structures.graph.RouteLink;
import rfinder.structures.nodes.PathNode;
import rfinder.structures.nodes.StopNode;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class TransferComputer {
    // Default radius for footpaths
    private final double DEFAULT_RADIUS = 1.5;

    // DAO objects for transfer, stop, graph, and footpath
    private final TransferDAO transferDAO = new DefaultTransferDAO();
    private final StopDAO stopDAO = new DefaultStopDAO();
    private final GraphDAO graphDAO = new DefaultGraphDAO();
    private final FootpathDAO footpathDAO = new DefaultFootpathDAO();

    // Graph object for pathfinding
    private final RoutableGraph<PathNode, ? extends RouteLink<PathNode>> graph = graphDAO.getNetworkGraph();

    /**
     * Updates transfers based on the given radius.
     *
     * @param radius The radius for footpaths.
     */
    @SuppressWarnings("unchecked")
    public void updateTransfers(double radius) {
        // Get all stop nodes
        List<StopNode> stops = stopDAO.selectNodes();

        // Iterate over each stop node
        for (StopNode source : stops) {
            // Create a precomputed context for the source node
            PrecomputedAsContext<PathNode> context = new PrecomputedAsContext<>(graph, source, new EuclideanDistanceEvaluator());

            // Get the set of stop nodes within the given radius
            Set<StopNode> transfersStops = source.getFootpathsWith(footpathDAO, radius);

            // Compute transfers to all the transfer stops
            context.tryComputeToAll(transfersStops);

            // Get the computed transfers
            List<PathRecord<? extends PathNode>> computedTransfers = context.getAllComputed();

            // Insert the computed transfers into the transfer DAO
            transferDAO.insertAllByStop(source, computedTransfers.stream().map((r) -> (PathRecord<StopNode>) r).toList());
        }
    }

    /**
     * Updates transfers using the default radius.
     */
    public void updateTransfers() {
        updateTransfers(DEFAULT_RADIUS);
    }

    /**
     * Main method to update transfers.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        TransferComputer computer = new TransferComputer();
        computer.updateTransfers();
    }
}