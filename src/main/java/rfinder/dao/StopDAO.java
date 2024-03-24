package rfinder.dao;

import rfinder.model.network.walking.EdgeLinkage;
import rfinder.structures.common.Location;

import java.util.List;
import java.util.Map;

public interface StopBasicDAO {
    Location locById(String stopId);

    Location locById(int stopId);

    List<Map.Entry<Integer, EdgeLinkage>> getLinkedStops();
}
