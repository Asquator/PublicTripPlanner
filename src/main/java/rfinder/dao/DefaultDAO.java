package rfinder.dao;

import net.postgis.jdbc.PGgeometry;
import net.postgis.jdbc.geometry.MultiLineString;
import net.postgis.jdbc.geometry.Point;
import rfinder.pathfinding.EdgeLinkage;
import rfinder.pathfinding.ShapedLink;
import rfinder.structures.graph.RouteLink;
import rfinder.structures.common.Location;
import rfinder.structures.nodes.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DefaultDAO implements RoadDAO {
    private Connection connection;

    private final StopDAO stopDAO = new StopLinkDAO();


    public static final String ALL_EDGES = "select source, target, km, geom_source, geom_dest, geom_way from edge_map";

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

    @SuppressWarnings("unchecked")
    private static List<Location> geomToShape(MultiLineString lineString){
        List<Location> ret = new ArrayList<>();
        Arrays.stream(lineString.getLines()).forEach(l -> l.iterator().forEachRemaining(p -> ret.add(Location.fromPoint((Point)p))));

        return ret;
    }

    public HashMap<PathNode, Set<ShapedLink>> getFullRoadGraph(){
        ResultSet res;
        // Get stop location as a point
        try (PreparedStatement statement = connection.prepareStatement(ALL_EDGES)){
            res = statement.executeQuery();
            HashMap<PathNode, Set<ShapedLink>> graph = new HashMap<>();

            Map<Integer, VertexNode> nodes = new HashMap<>();
            VertexNode sourceNode, destNode;
            Location location;
            List<Location> shape;
            Set<ShapedLink> connections;

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

                // retrieve shape
                shape = geomToShape((MultiLineString) new PGgeometry(res.getObject("geom_way").toString()).getGeometry());

                // update links set in graph
                connections = graph.getOrDefault(sourceNode, new HashSet<>());
                connections.add(new ShapedLink(destNode, km, shape));
                graph.put(sourceNode, connections);

                connections = graph.getOrDefault(destNode, new HashSet<>());
                connections.add(new ShapedLink(sourceNode, km, shape));
                graph.put(destNode, connections);

            }

            return graph;
        }
        catch (SQLException ex){
            throw new RuntimeException(ex);
        }
    }

    public HashMap<PathNode, Set<ShapedLink>> getFullNetworkGraph(){

        final double EXTENSION_FACTOR = 0.0005;
        List<Location> sourceShape, targetShape;

        HashMap<PathNode, Set<ShapedLink>> fullGraph = getFullRoadGraph();


        List<Map.Entry<Integer, EdgeLinkage>> linkedStops = stopDAO.getLinkedStops();

        for (Map.Entry<Integer, EdgeLinkage> stopEntry : linkedStops) {
            EdgeLinkage linkage = stopEntry.getValue();

            // closest stop is the one representing this stop, assume it lies on the edge
            StopNode stopNode = (StopNode) linkage.closest();

            // set of graph links
            Set<ShapedLink> stopConnections = new HashSet<>();

            fullGraph.put(stopNode, stopConnections);
            sourceShape = List.of(stopEntry.getValue().closest().getLocation(), linkage.source().getLocation());
            targetShape = List.of(stopEntry.getValue().closest().getLocation(), linkage.target().getLocation());

            //creating two edges from a stop to the closest edge's vertices
            stopConnections.add(new ShapedLink(linkage.source(), linkage.kmSource() + EXTENSION_FACTOR, sourceShape));
            stopConnections.add(new ShapedLink(linkage.target(), linkage.kmTarget() + EXTENSION_FACTOR, sourceShape));

            // creating two edges pointing from the closest edge's vertices to the stop
            fullGraph.getOrDefault(linkage.source(), new HashSet<>()).add(new ShapedLink(stopNode, linkage.kmSource() + EXTENSION_FACTOR, targetShape));
            fullGraph.getOrDefault(linkage.target(), new HashSet<>()).add(new ShapedLink(stopNode, linkage.kmTarget() + EXTENSION_FACTOR, targetShape));
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
