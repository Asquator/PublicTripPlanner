package rfinder.dao;

import rfinder.model.network.walking.EdgeLinkage;
import rfinder.structures.common.Location;
import rfinder.structures.common.RouteID;
import rfinder.structures.nodes.StopNode;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface StopDAO {
    Location getStopById (String stopId);

    Location getStopById(int stopId);

    List<Map.Entry<Integer, EdgeLinkage>> getLinkedStops();
}
