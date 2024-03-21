package rfinder.dao;

import net.postgis.jdbc.PGgeometry;
import net.postgis.jdbc.geometry.Geometry;
import net.postgis.jdbc.geometry.Point;
import rfinder.structures.common.Location;
import rfinder.structures.nodes.StopNode;
import rfinder.structures.nodes.VertexNode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class DefaultFootpathDAO implements FootpathDAO {

    private final Connection connection = DBManager.newConnection();

    private static final String CLOSEST_STOPS_IN_RADIUS = "select stop_id, stop_loc\n" +
            "from stops\n" +
            "where ST_Distance(?, stop_loc::geography) / 1000 < ?";


    public static final String CLOSEST_VERTEX_BY_LOC = "select id, geom\n" +
            "from vertices\n" +
            "order by geom <-> ?\n" +
            "limit 1";

    public Set<StopNode> getFootPaths(Location location, double radius) {
        ResultSet res;
        Set<StopNode> ret = new HashSet<>();

        try (PreparedStatement statement = connection.prepareStatement(CLOSEST_STOPS_IN_RADIUS)){
            statement.setObject(1, new PGgeometry((Geometry) location.toPoint()));
            statement.setDouble(2, radius);

            res = statement.executeQuery();

            while (res.next()){
                Point point = (Point)((new PGgeometry(res.getObject("stop_loc").toString())).getGeometry());
                int id = Integer.parseInt(res.getString("stop_id"));
                ret.add(new StopNode(Location.fromPoint(point), id));
            }
        }
        catch (SQLException ex){
            throw new RuntimeException(ex);
        }

        return ret;
    }

    @Override
    public Set<StopNode> getFootPaths(StopNode stopNode, double radius) {
        Set<StopNode> ret = getFootPaths(stopNode.getLocation(), radius);

        // remove source node
        ret.remove(stopNode);
        return ret;
    }

    @Override
    public VertexNode getClosestVertex(Location location) {
        ResultSet res;
        // get stop location as a point
        try (PreparedStatement statement = connection.prepareStatement(CLOSEST_VERTEX_BY_LOC)){
            Point point = location.toPoint();
            statement.setObject(1, new PGgeometry(point));

            res = statement.executeQuery();

            //if the location has been found, return a proxy object
            if(res.next()) {
                int id = res.getInt("id");
                Point closest = (Point)(new PGgeometry(res.getObject("geom").toString())).getGeometry();
                return new VertexNode(Location.fromPoint(closest), id);
            }
            else
                throw new RuntimeException("Couldn't find vertex");
        }
        catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }

}
