package rfinder.structures.nodes;


import rfinder.dao.FootpathDAO;
import rfinder.dao.StopDAO;
import rfinder.dao.DefaultStopDAO;
import rfinder.query.result.TerminalPathElement;
import rfinder.structures.common.Location;
import rfinder.structures.graph.GraphNode;

import java.util.Set;

public final class StopNode extends PathNode implements GraphNode<Integer> {

    private static final StopDAO linkDAO = new DefaultStopDAO();

    public StopNode(Location location, int stopId){
        super(location, stopId);
    }

    public StopNode(Location location, String stopId){
        super(location, Integer.parseInt(stopId));
    }

    @Override
    public Set<StopNode> getFootpathsWith(FootpathDAO dao, double radius) {
        return dao.getFootPaths(this, radius);
    }

    @Override
    public TerminalPathElement toElement() {
        return linkDAO.viewById(id());
    }
}
