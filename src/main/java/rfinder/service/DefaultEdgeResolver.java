package rfinder.service;

import rfinder.dao.DefaultGraphDAO;
import rfinder.dao.DefaultStopDAO;
import rfinder.dao.GraphDAO;
import rfinder.dao.StopDAO;

import rfinder.query.LocationPoint;
import rfinder.query.StopPoint;
import rfinder.structures.links.EdgeLinkage;
import rfinder.structures.nodes.NodeAdapterFactory;
import rfinder.structures.nodes.NodeFactory;
import rfinder.structures.nodes.StopNode;

public class DefaultEdgeResolver implements EdgeLinkageResolver {

    private final StopDAO stopDAO = new DefaultStopDAO();

    GraphDAO dao = new DefaultGraphDAO();

    private final NodeAdapterFactory factory = new NodeAdapterFactory();

    @Override
    public EdgeLinkage resolve(LocationPoint point) {
        return dao.getEdgeLinkage(((LocationPoint) point).location(), factory);
    }

    @Override
    public EdgeLinkage resolve(StopPoint sPoint) {
        return dao.getEdgeLinkage(stopDAO.locById(sPoint.stopId()), (NodeFactory<StopNode>) location1 -> new StopNode(location1, sPoint.stopId()));
    }
}

