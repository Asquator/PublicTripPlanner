package rfinder.dao;

import rfinder.structures.common.RouteID;
import rfinder.structures.nodes.StopNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StopStorage {
    private Map<StopNode, List<Map.Entry<RouteID, Integer>>> stopRoutes;

    public StopStorage(StopDAO stopDAO, RouteDAO routeDAO){
        stopRoutes = new HashMap<>();
        List<StopNode> stops = stopDAO.selectNodes();
        for(StopNode stop: stops){
            stopRoutes.put(stop, routeDAO.getRoutes(stop));
        }
    }

    public List<Map.Entry<RouteID, Integer>> getRoutes(StopNode stop) {
        return stopRoutes.get(stop);
    }

}
