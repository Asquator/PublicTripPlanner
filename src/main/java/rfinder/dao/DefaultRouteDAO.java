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

    private static final String ROUTES_BY_STOP = "select * from unique_routes where stop_id = ?";

    private static final String ALL_ROUTES = "select * from unique_routes";
    private static final String ROUTE_SHAPE = "select * from route_shape(?, ?, ?, ?)";

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
    public List<RouteID> selectAll() {
        ResultSet res;
        List<RouteID> ret = new ArrayList<>();

        try(PreparedStatement statement = connection.prepareStatement(ALL_ROUTES)){
            res = statement.executeQuery();

            while(res.next()){
                String routeId = res.getString("route_id");
                byte direction = res.getByte("direction_id");
                ret.add(new RouteID(routeId, direction));
            }
        }
        catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        return ret;
    }

    @Override
    public List<Map.Entry<RouteID, Integer>> getRoutes(StopNode stop)  {
        ResultSet res;
        List<Map.Entry<RouteID, Integer>> ret = new LinkedList<>();

        try(PreparedStatement statement = connection.prepareStatement(ROUTES_BY_STOP)){
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

    @Override
    public List<Location> getShapeAlongRoute(RouteID routeID, Location l1, Location l2) {
       List<Location> ret = new ArrayList<>();
       ResultSet res;

       try(PreparedStatement statement = connection.prepareStatement(ROUTE_SHAPE)) {
           statement.setString(1, routeID.routeId());
           statement.setByte(2, routeID.direction());
           statement.setObject(3, new PGgeometry(l1.toPoint()));
           statement.setObject(4, new PGgeometry(l2.toPoint()));

           res = statement.executeQuery();

           while (res.next()) {
               Point point = (Point)(new PGgeometry(res.getObject(1).toString())).getGeometry();
               ret.add(Location.fromPoint(point));
           }

       } catch (SQLException e) {
           throw new RuntimeException(e);
       }

       return ret;
    }
}
