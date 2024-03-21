package rfinder.dao;

import net.postgis.jdbc.PGgeometry;
import net.postgis.jdbc.geometry.Point;
import rfinder.model.network.walking.EdgeLinkage;
import rfinder.structures.graph.RouteLink;
import rfinder.structures.common.Location;
import rfinder.structures.common.RouteID;
import rfinder.structures.nodes.*;
import rfinder.structures.components.RideLink;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DefaultDAO implements RoadDAO {
    private Connection connection;

    private final StopDAO stopDAO = new StopLinkDAO();

    private static final String CLOSEST_EDGE = "select * from get_closest_edge(?)";

    public DefaultDAO(){
        connection = DBManager.newConnection();
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
        return null;
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
                Point point = (Point)(new PGgeometry(res.getObject("geom").toString())).getGeometry();
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
    public Set<RouteLink<VertexNode>> getRoadLinks(VertexNode vertexNode) {
        return getRoadLinks(vertexNode.id());
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

    public HashMap<PathNode, Set<RouteLink<PathNode>>> getFullRoadGraph(){
        ResultSet res;
        // Get stop location as a point
        try (PreparedStatement statement = connection.prepareStatement(PostgisQuery.ALL_EDGES)){
            res = statement.executeQuery();
            HashMap<PathNode, Set<RouteLink<PathNode>>> graph = new HashMap<>();

            Map<Integer, VertexNode> nodes = new HashMap<>();
            VertexNode sourceNode, destNode;
            Location location;
            Set<RouteLink<PathNode>> connections;

            while(res.next()) {
                double km = res.getDouble("km");

                // retrieve source
                int sourceId = res.getInt("source");
                PGgeometry geom_source = new PGgeometry(res.getObject("geom_source").toString());

                location = Location.fromPoint((Point)geom_source.getGeometry());
                sourceNode = nodes.getOrDefault(sourceId, new VertexNode(location, sourceId));
                nodes.put(sourceId, sourceNode);

                // retrieve target
                int targetId = res.getInt("target");
                PGgeometry geom_dest = new PGgeometry(res.getObject("geom_dest").toString());

                location = Location.fromPoint((Point)geom_dest.getGeometry());
                destNode = nodes.getOrDefault(targetId, new VertexNode(location, targetId));
                nodes.put(targetId, destNode);

                // update links set in graph
                connections = graph.getOrDefault(sourceNode, new HashSet<>());
                connections.add(new RouteLink<>(destNode, km));
                graph.put(sourceNode, connections);

                connections = graph.getOrDefault(destNode, new HashSet<>());
                connections.add(new RouteLink<>(sourceNode, km));
                graph.put(destNode, connections);

            }

            return graph;
        }
        catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    public HashMap<PathNode, Set<RouteLink<PathNode>>> getFullNetworkGraph(){
        HashMap<PathNode, Set<RouteLink<PathNode>>> fullGraph = getFullRoadGraph();

        final double EXTENSION_FACTOR = 0.0005;

        List<Map.Entry<Integer, EdgeLinkage>> linkedStops = stopDAO.getLinkedStops();

        for (Map.Entry<Integer, EdgeLinkage> stopEntry : linkedStops) {
            EdgeLinkage linkage = stopEntry.getValue();

            // closest stop is the one representing this stop, assume it lies on the edge
            StopNode stopNode = (StopNode) linkage.closest();

            Set<RouteLink<PathNode>> stopConnections = new HashSet<>();

            fullGraph.put(stopNode, stopConnections);

            //creating two edges from a stop to the closest edge's vertices
            stopConnections.add(new RouteLink<>(linkage.source(), linkage.kmSource() + EXTENSION_FACTOR));
            stopConnections.add(new RouteLink<>(linkage.target(), linkage.kmTarget() + EXTENSION_FACTOR));

            // creating two edges pointing from the closest edge's vertices to the stop
            fullGraph.getOrDefault(linkage.source(), new HashSet<>()).add(new RouteLink<>(stopNode, linkage.kmSource() + EXTENSION_FACTOR));
            fullGraph.getOrDefault(linkage.target(), new HashSet<>()).add(new RouteLink<>(stopNode, linkage.kmTarget() + EXTENSION_FACTOR));
        }

        return fullGraph;
    }


    @Override
    public EdgeLinkage getLinkage(Location location, NodeFactory nodeFactory) {
        ResultSet res;

        try (PreparedStatement statement = connection.prepareStatement(CLOSEST_EDGE)) {
            Point point = location.toPoint();
            point.setSrid(4326);
            statement.setObject(1, new PGgeometry(point));
            res = statement.executeQuery();

            //if the location has been found, return the linkage
            if (res.next()) {
                return Extractors.extractLink(res, nodeFactory);
            } else
                throw new RuntimeException("Couldn't find vertex");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public EdgeLinkage getLinkage(Location location, int knownId) {
        return getLinkage(location, new NodeFactory<PathNode>() {
            @Override
            public PathNode create(Location location) {
                return new PathNode(location, knownId);
            }
        });
    }
/*
    public Set<RideLink> getTransportLinks(StopNode stopNode, boolean continued) {
        ResultSet res;
        Set<RideLink> links = new HashSet<>();

        try (PreparedStatement statement = connection.prepareStatement(continued ?
                PostgisQuery.TRANSPORT_LINKS_CONT : PostgisQuery.TRANSPORT_LINKS)){
            statement.setString(1, stopNode.stringId());

            res = statement.executeQuery();

            //if the location has been found, return a proxy object

            while(res.next()) {
                String routeId = res.getString("routeId");
                byte direction = res.getByte("direction_id");
                String destStop = res.getString("dest_stop");
                int sourceSequence = res.getInt("source_seq");
                int destSequence = res.getInt("dest_seq");
                double distance = res.getDouble("shape_distance");
                PGgeometry geom_dest = (PGgeometry) res.getObject("dest_loc");
                Location destLocation = Location.fromPoint((Point)geom_dest.getGeometry());

                StopNode destNode = new StopNode(destLocation, destStop);

                RideLink link = new RideLink(destNode, new RouteID(routeId, direction),
                        sourceSequence, destSequence, distance);
                links.add(link);
            }
        }
        catch (SQLException ex){
            throw new RuntimeException(ex);
        }

        return links;
    }*/
}
