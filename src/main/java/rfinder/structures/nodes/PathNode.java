package rfinder.structures.nodes;

import rfinder.structures.common.Location;

public class PathNode {
    private Location location;
    public PathNode (Location location){
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return location.toString();
    }

}
