package rfinder.query.result;

import rfinder.structures.common.Location;

import java.util.List;

public interface PathElement {
    List<Location> getShape();
}