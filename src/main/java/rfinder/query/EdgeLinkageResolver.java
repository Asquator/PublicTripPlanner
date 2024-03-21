package rfinder.query;

import rfinder.model.network.walking.EdgeLinkage;

public interface EdgeLinkageResolver {
    EdgeLinkage resolve(QueryPoint point);
}
