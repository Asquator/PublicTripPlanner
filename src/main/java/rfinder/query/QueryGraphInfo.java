package rfinder.query;

import rfinder.dao.GraphDAO;
import rfinder.service.EdgeLinkageResolver;
import rfinder.structures.links.EdgeLinkage;
import rfinder.structures.nodes.PathNode;

public record QueryGraphInfo(GraphDAO graphDAO, EdgeLinkage sourceLinkage, EdgeLinkage destinationLinkage, int maxTrips, double walkRadius) {
    public QueryGraphInfo(GraphDAO graphDAO, QueryInfo info, EdgeLinkageResolver resolver, double walkRadius) {
        this(graphDAO, info.source().resolve(resolver), info.destination().resolve(resolver), info.maxTrips(), walkRadius);
    }

    public PathNode sourceRepr(){
        return sourceLinkage().closest();
    }

    public PathNode destinationRepr(){
        return destinationLinkage().closest();
    }
}
