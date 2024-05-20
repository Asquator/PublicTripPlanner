package rfinder.pathfinding;

import rfinder.structures.nodes.PathNode;
import rfinder.structures.nodes.StopNode;

import java.util.List;

public interface FootpathManager {

    List<PathRecord<? extends PathNode>> getFootpaths(PathNode source);

}
