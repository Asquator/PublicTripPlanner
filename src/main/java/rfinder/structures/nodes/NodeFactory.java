package rfinder.structures.nodes;

import rfinder.structures.common.Location;

public interface NodeFactory <T extends PathNode> {
    T create(Location location);
}
