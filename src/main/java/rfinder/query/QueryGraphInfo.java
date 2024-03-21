package rfinder.query;

import rfinder.model.network.walking.EdgeLinkage;
import rfinder.structures.nodes.PathNode;

public record QueryGraphInfo(EdgeLinkage sourceLinkage, EdgeLinkage destinationLinkage, int maxTrips, double walkRadius) {
    public QueryGraphInfo(QueryInfo info, EdgeLinkageResolver resolver){
        this(resolver.resolve(info.source()), resolver.resolve(info.destination()), info.maxTrips(), info.walkRadius());
    }
}
