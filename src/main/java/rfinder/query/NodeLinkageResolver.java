package rfinder.query;

import rfinder.dao.DefaultDAO;
import rfinder.dao.StopLinkDAO;
import rfinder.dao.RoadDAO;
import rfinder.dao.StopDAO;

import rfinder.model.network.walking.EdgeLinkage;
import rfinder.structures.common.Location;
import rfinder.structures.nodes.NodeAdapterFactory;
import rfinder.structures.nodes.NodeFactory;
import rfinder.structures.nodes.PathNode;
import rfinder.structures.nodes.StopNode;

public class NodeLinkageResolver implements EdgeLinkageResolver {

    private final StopDAO stopDAO = new StopLinkDAO();

    // get linkages to closest edges
    RoadDAO dao = new DefaultDAO();

    private final NodeAdapterFactory factory = new NodeAdapterFactory();

    @Override
    public EdgeLinkage resolve(QueryPoint point) {

        Location location = null;

        if(point instanceof StopPoint sPoint)
            return dao.getLinkage(stopDAO.getStopById(sPoint.stopId()), new NodeFactory<StopNode>() {
                @Override
                public StopNode create(Location location) {
                    return new StopNode(location, sPoint.stopId());
                }
            });

        else if(point instanceof LocationPoint) {
            return dao.getLinkage(((LocationPoint) point).location(), factory);
        }

        else
            throw new RuntimeException("illegal query point: " + point.getClass());

    }

}

