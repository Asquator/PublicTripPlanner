package rfinder.dao;

import rfinder.pathfinding.PathRecord;
import rfinder.structures.nodes.PathNode;
import rfinder.structures.nodes.StopNode;

import java.util.List;

public interface TransferDAO {
    List<PathRecord<? extends PathNode>> getByStop(StopNode stop);
    void insertAllByStop(StopNode stop, List<PathRecord<StopNode>> transfers);
}
