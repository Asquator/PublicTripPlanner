package rfinder.dao;

import rfinder.pathfinding.EdgeLinkage;
import rfinder.query.result.StopView;
import rfinder.structures.common.Location;
import rfinder.structures.nodes.StopNode;

import java.util.List;
import java.util.Map;

public interface StopDAO {
    Location locById(String stopId);

    Location locById(int stopId);

    StopView viewById(int stopId);

    List<StopNode> selectNodes();

    List<Map.Entry<Integer, EdgeLinkage>> getLinkedStops();

}
