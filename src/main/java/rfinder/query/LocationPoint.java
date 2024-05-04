package rfinder.query;

import rfinder.service.EdgeLinkageResolver;
import rfinder.structures.common.Location;
import rfinder.structures.links.EdgeLinkage;

public record LocationPoint(Location location) implements QueryPoint {

    @Override
    public EdgeLinkage resolve(EdgeLinkageResolver resolver) {
        return resolver.resolve(this);
    }
}
