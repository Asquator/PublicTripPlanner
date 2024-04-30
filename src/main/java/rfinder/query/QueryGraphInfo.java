package rfinder.query;

import rfinder.pathfinding.EdgeLinkage;
import rfinder.structures.nodes.PathNode;

public record QueryGraphInfo(EdgeLinkage sourceLinkage, EdgeLinkage destinationLinkage, int maxTrips) {
    public QueryGraphInfo(QueryInfo info, EdgeLinkageResolver resolver){
        this(resolver.resolve(info.source()), resolver.resolve(info.destination()), info.maxTrips());
    }

    public PathNode sourceRepr(){
        return sourceLinkage().closest();
    }

    public PathNode destinationRepr(){
        return destinationLinkage().closest();
    }
}
