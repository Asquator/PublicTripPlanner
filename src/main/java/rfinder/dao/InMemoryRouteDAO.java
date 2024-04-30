package rfinder.dao;

import rfinder.structures.common.RouteID;
import rfinder.structures.nodes.StopNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryRouteDAO extends DefaultRouteDAO {

    private Map<RouteID, List<StopNode>> routeStops;

    public void updateData(){
        routeStops = new HashMap<>();
        selectAll().forEach(routeID -> routeStops.put(routeID, getStops(routeID)));
    }

    @Override
    public List<Map.Entry<RouteID, Integer>> getRoutes(StopNode stop) {
        return super.getRoutes(stop);
    }

    @Override
    public List<StopNode> getStops(RouteID routeID) {
        return routeStops.get(routeID);
    }

}
