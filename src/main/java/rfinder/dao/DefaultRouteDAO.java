package rfinder.dao;

import net.postgis.jdbc.PGgeometry;
import net.postgis.jdbc.geometry.Point;
import rfinder.structures.common.Location;
import rfinder.structures.common.RouteID;
import rfinder.structures.nodes.StopNode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DefaultRouteDAO implements RouteDAO{

    Connection connection = DBManager.newConnection();

    private static final String STOPS_BY_ROUTE = "select * from unique_routes where route_id=? and direction_id=?";

    private static final String ALL_ROUTES = "select * from unique_routes where stop_id = ?";

    @Override
    public List<StopNode> getStops(RouteID routeID) {
        ResultSet res;
        List<StopNode> ret = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(STOPS_BY_ROUTE)){
            statement.setString(1, routeID.routeId());
            statement.setByte(2, routeID.direction());

            res = statement.executeQuery();

            while (res.next()){
                Point point = (Point)(new PGgeometry(res.getObject("stop_loc").toString())).getGeometry();
                String id = res.getString("stop_id");
                ret.add(new StopNode(Location.fromPoint(point), id));
            }
        }
        catch (SQLException ex){
            throw new RuntimeException(ex);
        }

        return ret;
    }

    @Override
    public List<Map.Entry<RouteID, Integer>> getRoutes(StopNode stop)  {
        ResultSet res;
        List<Map.Entry<RouteID, Integer>> ret = new LinkedList<>();

        try(PreparedStatement statement = connection.prepareStatement(ALL_ROUTES)){
            statement.setString(1, String.valueOf(stop.id()));

            res = statement.executeQuery();

            while (res.next()){
                String routeId = res.getString("route_id");
                byte direction = res.getByte("direction_id");
                int stop_sequence = res.getInt("stop_sequence_consec");
                ret.add(Map.entry(new RouteID(routeId, direction), stop_sequence));
            }
        }

        catch (SQLException ex){
            throw new RuntimeException(ex);
        }

        return ret;
    }
}
