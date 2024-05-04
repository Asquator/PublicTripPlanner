package rfinder.query;

import rfinder.dao.GraphDAO;
import rfinder.service.EdgeLinkageResolver;
import rfinder.structures.links.EdgeLinkage;
import rfinder.structures.nodes.PathNode;

public record QueryGraphInfo(GraphDAO graphDAO, EdgeLinkage sourceLinkage, EdgeLinkage destinationLinkage, int maxTrips) {
    public QueryGraphInfo(GraphDAO graphDAO, QueryInfo info, EdgeLinkageResolver resolver){
        this(graphDAO, info.source().resolve(resolver), info.destination().resolve(resolver), info.maxTrips());
    }

    public PathNode sourceRepr(){
        return sourceLinkage().closest();
    }

    public PathNode destinationRepr(){
        return destinationLinkage().closest();
    }
}
