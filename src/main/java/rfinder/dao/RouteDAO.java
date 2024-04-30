package rfinder.dao;

import rfinder.structures.common.Location;
import rfinder.structures.common.RouteID;
import rfinder.structures.nodes.StopNode;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface RouteDAO {

    List<StopNode> getStops(RouteID routeID);

    List<RouteID> selectAll();

    List<Map.Entry<RouteID, Integer>> getRoutes(StopNode stop) ;

    List<Location> getShapeAlongRoute(RouteID routeID, Location l1, Location l2);
}
