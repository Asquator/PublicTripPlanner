package rfinder.service;

import rfinder.query.LocationPoint;
import rfinder.query.StopPoint;
import rfinder.structures.links.EdgeLinkage;

public interface EdgeLinkageResolver {
    EdgeLinkage resolve(LocationPoint point);
    EdgeLinkage resolve(StopPoint point);
}
