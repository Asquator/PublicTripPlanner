package rfinder.dao;

import net.postgis.jdbc.PGgeometry;
import net.postgis.jdbc.geometry.Point;
import rfinder.structures.general.Location;
import rfinder.structures.nodes.StopNode;
import rfinder.structures.nodes.VertexNode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PostgisRouteDAO implements RouteDAO{
    private Connection connection;

    public PostgisRouteDAO(){
        connection = DBManager.newConnection();
    }

    @Override
    public Location getStopLocation(String stopId) {

        ResultSet res;
        // get stop location as a point
        try (PreparedStatement statement = connection.prepareStatement(PostgisQuery.LOC_BY_STOP)){
            statement.setString(1, stopId);

            res = statement.executeQuery();

            //if the location has been found, return a proxy object
            if(res.next()) {
                Point point = (Point)((PGgeometry) res.getObject(1)).getGeometry();
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
    public Location getVertexLocation(int vertexId) {

        ResultSet res;
        // get stop location as a point
        try (PreparedStatement statement = connection.prepareStatement(PostgisQuery.LOC_BY_VERTEX)){
            statement.setInt(1, vertexId);

            res = statement.executeQuery();

            //if the location has been found, return a proxy object
            if(res.next()) {
                Point point = (Point)((PGgeometry) res.getObject(1)).getGeometry();
                return Location.fromPoint(point);
            }
            else
                throw new RuntimeException("Couldn't find vertex");
        }
        catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }


    @Override
    public VertexNode getClosestVertex(Location location) {
        ResultSet res;
        // get stop location as a point
        try (PreparedStatement statement = connection.prepareStatement(PostgisQuery.CLOSEST_VERTEX_BY_LOC)){
            Point point = location.toPoint();
            statement.setObject(1, new PGgeometry(point));

            res = statement.executeQuery();

            //if the location has been found, return a proxy object
            if(res.next()) {
                int id = res.getInt("id");
                Point closest = (Point)((PGgeometry) res.getObject("geom")).getGeometry();
                return new VertexNode(Location.fromPoint(closest), id);
            }
            else
                throw new RuntimeException("Couldn't find vertex");
        }
        catch (SQLException ex){
            throw new RuntimeException(ex);
        }

    }

    @Override
    public VertexNode getClosestVertex(StopNode stopNode) {
        return getClosestVertex(stopNode.getLocation());
    }


}
