package rfinder.structures.nodes;


import rfinder.dao.StopDAO;
import rfinder.dao.StopLinkDAO;
import rfinder.query.result.NominalPathElement;
import rfinder.query.result.PathElement;
import rfinder.structures.common.Location;
import rfinder.structures.graph.GraphNode;

public final class StopNode extends PathNode implements GraphNode<Integer> {

    private static final StopDAO dao = new StopLinkDAO();

    public StopNode(Location location, int stopId){
        super(location, stopId);
    }

    public StopNode(Location location, String stopId){
        super(location, Integer.parseInt(stopId));
    }

    @Override
    public NominalPathElement toElement() {
        return dao.viewById(id());
    }
}
