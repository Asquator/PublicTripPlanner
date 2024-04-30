package rfinder.pathfinding;

import rfinder.structures.graph.GraphNode;
import rfinder.structures.nodes.PathNode;
import rfinder.structures.nodes.StopNode;

import java.util.Iterator;
import java.util.List;

public interface FootpathManager {

    List<PathRecord<PathNode>> getFootpaths(PathNode source);

    List<PathRecord<PathNode>> getFootpaths(StopNode source);
}
