package rfinder.query;

import rfinder.dao.DefaultGraphDAO;
import rfinder.dao.DefaultStopDAO;
import rfinder.dao.GraphDAO;
import rfinder.dao.StopDAO;

import rfinder.pathfinding.EdgeLinkage;
import rfinder.structures.common.Location;
import rfinder.structures.nodes.NodeAdapterFactory;
import rfinder.structures.nodes.NodeFactory;
import rfinder.structures.nodes.StopNode;

public class NodeLinkageResolver implements EdgeLinkageResolver {

    private final StopDAO stopDAO = new DefaultStopDAO();

    GraphDAO dao = new DefaultGraphDAO();

    private final NodeAdapterFactory factory = new NodeAdapterFactory();

    @Override
    public EdgeLinkage resolve(QueryPoint point) {

        Location location = null;

        if(point instanceof StopPoint sPoint)
            return dao.getLinkage(stopDAO.locById(sPoint.stopId()), new NodeFactory<StopNode>() {
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

