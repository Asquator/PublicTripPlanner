package rfinder.query.result;

import rfinder.structures.common.Location;

import java.time.Duration;
import java.util.List;

public class NominalPathElement implements PathElement {

    private final Location shape;
    public NominalPathElement(Location location){
        shape = location;
    }

    @Override
    public List<Location> getShape() {
        return List.of(shape);
    }
}
