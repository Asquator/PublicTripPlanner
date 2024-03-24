
package rfinder.dao;

import rfinder.structures.common.Location;
import rfinder.structures.nodes.StopNode;
import rfinder.structures.nodes.VertexNode;

import java.util.Set;

public interface FootpathDAO {
    Set<StopNode> getFootPaths(Location location, double radius);

    Set<StopNode> getFootPaths(StopNode stopNode, double radius);

}
