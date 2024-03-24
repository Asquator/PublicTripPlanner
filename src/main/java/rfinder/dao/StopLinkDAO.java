package rfinder.dao;

import net.postgis.jdbc.PGgeometry;
import net.postgis.jdbc.geometry.Point;
import rfinder.pathfinding.EdgeLinkage;
import rfinder.query.result.StopView;
import rfinder.structures.common.Location;
import rfinder.structures.nodes.NodeFactory;
import rfinder.structures.nodes.StopNode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StopLinkDAO implements StopDAO {

    private final Connection connection = DBManager.newConnection();

    private static final String STOP_BY_ID = "select * from stops where stop_id = ?";

    private static final String LINK_STOPS = "select * from stop_linkage";

    @Override
    public Location locById(String stopId) {

        ResultSet res;
        // get stop location as a point
        try (PreparedStatement statement = connection.prepareStatement(STOP_BY_ID)){
            statement.setString(1, stopId);

            res = statement.executeQuery();

            //if the location has been found, return a proxy object
            if(res.next()) {
                Point point = (Point)(new PGgeometry(res.getObject("stop_loc").toString())).getGeometry();
                return Location.fromPoint(point);
            }
            else
                throw new RuntimeException("Couldn't find stop");
        }
        catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Location locById(int stopId) {
        return locById(Integer.toString(stopId));
    }

    @Override
    public StopView viewById(int stopId) {

        ResultSet res;
        // get stop location as a point
        try (PreparedStatement statement = connection.prepareStatement(STOP_BY_ID)){
            statement.setString(1, String.valueOf(stopId));

            res = statement.executeQuery();

            //if the location has been found, return a proxy object
            if(res.next()) {
                Point point = (Point)(new PGgeometry(res.getObject("stop_loc").toString())).getGeometry();
                return new StopView(stopId, Location.fromPoint(point), res.getString("stop_name"));
            }
            else
                throw new RuntimeException("Couldn't find stop");
        }
        catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Map.Entry<Integer, EdgeLinkage>> getLinkedStops() {

        ResultSet res;
        // get stop location as a point
        try (PreparedStatement statement = connection.prepareStatement(LINK_STOPS)){
            res = statement.executeQuery();

            List<Map.Entry<Integer, EdgeLinkage>> stops = new ArrayList<>();

            while(res.next()) {
                EdgeLinkage linkage;
                int id = Integer.parseInt(res.getString("stop_id"));

                stops.add(Map.entry(id, Extractors.extractLink(res, new NodeFactory<StopNode>() {
                    @Override
                    public StopNode create(Location location) {
                        return new StopNode(location, id);
                    }
                })));
            }
            return stops;
        }
        catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }
}
