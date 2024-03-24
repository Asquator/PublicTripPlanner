package rfinder.query;

import rfinder.pathfinding.EdgeLinkage;

public interface EdgeLinkageResolver {
    EdgeLinkage resolve(QueryPoint point);
}
