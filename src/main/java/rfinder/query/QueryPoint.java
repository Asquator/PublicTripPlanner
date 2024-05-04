package rfinder.query;

import rfinder.service.EdgeLinkageResolver;
import rfinder.structures.links.EdgeLinkage;

public sealed interface QueryPoint permits LocationPoint, StopPoint {
    EdgeLinkage resolve(EdgeLinkageResolver resolver);
}
