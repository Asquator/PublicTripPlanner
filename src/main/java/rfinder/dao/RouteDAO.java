package rfinder.dao;

import rfinder.structures.common.RouteID;
import rfinder.structures.nodes.StopNode;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface RouteDAO {

    List<StopNode> getStops(RouteID routeID);


    List<Map.Entry<RouteID, Integer>> getRoutes(StopNode stop) ;

}
