package rfinder.dao;

import rfinder.pathfinding.PathRecord;
import rfinder.structures.nodes.PathNode;
import rfinder.structures.nodes.StopNode;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DefaultTransferDAO extends DBUser implements TransferDAO {

    private static final String TRANSFERS_BY_STOP = "select * from transfers where stop_from = ?";

    private static final String INSERT = "insert into transfers values(?, ?, ?)";

    DefaultStopDAO stopDAO = new DefaultStopDAO();

    @Override
    public List<PathRecord<? extends PathNode>> getByStop(StopNode stop) {
        ResultSet res;

        try (PreparedStatement statement = connection.prepareStatement(TRANSFERS_BY_STOP)) {
            statement.setString(1, String.valueOf(stop.id()));

            res = statement.executeQuery();

            List<PathRecord<? extends PathNode>> transfers = new ArrayList<>();

            while (res.next()) {
                StopNode stopNode = stopDAO.nodeById(res.getString("stop_to"));
                transfers.add(new PathRecord<>(stopNode, res.getDouble("cost")));
            }

            return transfers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void insertAllByStop(StopNode stop, List<PathRecord<StopNode>> transfers) {
        try (PreparedStatement statement = connection.prepareStatement(INSERT)) {
            for (PathRecord<StopNode> transfer : transfers) {
                statement.setString(1, String.valueOf(stop.id()));
                statement.setString(2, String.valueOf(transfer.target().id()));
                statement.setDouble(3, transfer.weight());

                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized void closeConnection() {
        super.closeConnection();
        stopDAO.closeConnection();
    }
}
