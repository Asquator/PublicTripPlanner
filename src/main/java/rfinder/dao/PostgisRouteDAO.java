package rfinder.dao;

import net.postgis.jdbc.PGgeometry;
import net.postgis.jdbc.geometry.Geometry;
import net.postgis.jdbc.geometry.Point;
import rfinder.structures.graph.RouteLink;
import rfinder.structures.common.Location;
import rfinder.structures.common.TripPatternID;
import rfinder.structures.nodes.StopNode;
import rfinder.structures.nodes.VertexNode;
import rfinder.structures.segments.RideSegment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PostgisRouteDAO implements RouteDAO {
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

    @Override
    public VertexNode getVertexByID(int vertexId) {
        ResultSet res;
        // get stop location as a point
        try (PreparedStatement statement = connection.prepareStatement(PostgisQuery.GEOM_BY_VERTEX)){
            statement.setInt(1, vertexId);
            res = statement.executeQuery();

            //if the location has been found, return a proxy object
            if(res.next()) {
                Point point = (Point)((PGgeometry) res.getObject("geom")).getGeometry();
                return new VertexNode(Location.fromPoint(point), vertexId);
            }
            else
                throw new RuntimeException("Couldn't find vertex");
        }
        catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    @Override
    public double getEuclideanDistance(VertexNode node1, VertexNode node2) {
        ResultSet res;
        // Get stop location as a point
        try (PreparedStatement statement = connection.prepareStatement(PostgisQuery.VERTEX_EUCLIDEAN_DISTANCE)){
            int id1 = node1.getId();
            int id2 = node2.getId();
            statement.setInt(1, id1);
            statement.setInt(2, id2);

            res = statement.executeQuery();
            if(res.next())
                return res.getDouble(1);

            else
                throw new RuntimeException("Could not compute distance");
        }
        catch (SQLException ex){
            throw new RuntimeException(ex);
        }

    }

    @Override
    public Set<RouteLink<VertexNode>> getRoadLinks(VertexNode vertexNode) {
        return getRoadLinks(vertexNode.getId());
    }


    public Set<RouteLink<VertexNode>> getRoadLinks(int vertexId) {
        ResultSet res;
        // Get stop location as a point
        try (PreparedStatement statement = connection.prepareStatement(PostgisQuery.CONNECTIONS_OF_VERTEX)){
            statement.setInt(1, vertexId);
            statement.setInt(2, vertexId);

            res = statement.executeQuery();
            Set<RouteLink<VertexNode>> ret = new HashSet<>();

            // If the location has been found, return a proxy object
            while(res.next()) {
                // Create a vertex node from point location and id
                PGgeometry geom = (PGgeometry) res.getObject("geom");
                int targetId = res.getInt("target");
                double km = res.getDouble("km");
                VertexNode dest = new VertexNode(Location.fromPoint((Point)geom.getGeometry()), targetId);
                RouteLink<VertexNode> link = new RouteLink<>(dest, km);
                ret.add(link);
            }

            return ret;
        }
        catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    public HashMap<VertexNode, Set<RouteLink<VertexNode>>> getFullGraph(){
        ResultSet res;
        // Get stop location as a point
        try (PreparedStatement statement = connection.prepareStatement(PostgisQuery.ALL_EDGES)){
            res = statement.executeQuery();
            HashMap<VertexNode, Set<RouteLink<VertexNode>>> graph = new HashMap<>();

            Map<Integer, VertexNode> nodes = new HashMap<>();
            VertexNode sourceNode, destNode;
            Location location;

            while(res.next()) {
                double km = res.getDouble("km");

                // retrieve source
                int sourceId = res.getInt("source");
                PGgeometry geom_source = (PGgeometry) res.getObject("geom_source");

                location = Location.fromPoint((Point)geom_source.getGeometry());
                sourceNode = nodes.getOrDefault(sourceId, new VertexNode(location, sourceId));
                nodes.put(sourceId, sourceNode);

                // retrieve target
                int targetId = res.getInt("target");
                PGgeometry geom_dest = (PGgeometry) res.getObject("geom_dest");

                location = Location.fromPoint((Point)geom_dest.getGeometry());
                destNode = nodes.getOrDefault(targetId, new VertexNode(location, targetId));
                nodes.put(targetId, destNode);

                Set<RouteLink<VertexNode>> connections = graph.getOrDefault(sourceNode, new HashSet<>());
                connections.add(new RouteLink<>(destNode, km));

                // update link set in graph
                graph.put(sourceNode, connections);
            }

            return graph;
        }
        catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Set<RideSegment> getTransportLinks(StopNode stopNode) {
        ResultSet res;
        Set<RideSegment> links = new HashSet<>();

        try (PreparedStatement statement = connection.prepareStatement(PostgisQuery.TRANSPORT_LINKS)){
            statement.setString(1, stopNode.getStopId());

            res = statement.executeQuery();

            //if the location has been found, return a proxy object

            while(res.next()) {
                String routeId = res.getString("route_id");
                String shapeId = res.getString("shape_id");
                String destStop = res.getString("dest_stop");
                int sourceSequence = res.getInt("source_seq");
                int destSequence = res.getInt("dest_seq");
                double distance = res.getDouble("shape_distance");
                PGgeometry geom_dest = (PGgeometry) res.getObject("dest_loc");
                Location destLocation = Location.fromPoint((Point)geom_dest.getGeometry());

                StopNode destNode = new StopNode(destLocation, destStop);

                RideSegment segment = new RideSegment(stopNode, destNode, new TripPatternID(routeId, shapeId));
                segment.setSourceSequence(sourceSequence);
                segment.setDestinationSequence(destSequence);

                links.add(segment);
            }
        }
        catch (SQLException ex){
            throw new RuntimeException(ex);
        }

        return links;
    }

    @Override
    public Set<StopNode> getStopsInRadius(Location location, double radius) {
        ResultSet res;
        Set<StopNode> ret = new HashSet<>();

        try (PreparedStatement statement = connection.prepareStatement(PostgisQuery.CLOSEST_STOPS_IN_RADIUS)){
            statement.setObject(1, new PGgeometry((Geometry) location.toPoint()));
            statement.setDouble(2, radius);

            res = statement.executeQuery();

            while (res.next()){
                Point point = (Point)((PGgeometry) res.getObject("stop_loc")).getGeometry();
                String id = res.getString("stop_id");
                ret.add(new StopNode(Location.fromPoint(point), id));
            }
        }
        catch (SQLException ex){
            throw new RuntimeException(ex);
        }

        return ret;
    }

}
