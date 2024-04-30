package rfinder.dao;

import net.postgis.jdbc.PGgeometry;
import net.postgis.jdbc.geometry.MultiLineString;
import net.postgis.jdbc.geometry.Point;
import rfinder.pathfinding.EdgeLinkage;
import rfinder.structures.links.ShapedLink;
import rfinder.structures.common.Location;
import rfinder.structures.nodes.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DefaultGraphDAO implements GraphDAO {
    private Connection connection;

    private final StopDAO stopDAO = new DefaultStopDAO();


    public static final String ALL_EDGES = "select source, target, km, geom_source, geom_dest, geom_way from edge_map";

    private static final String CLOSEST_EDGE = "select * from get_closest_edge(?)";

    public DefaultGraphDAO(){
        connection = DBManager.newConnection();
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
            VertexNode sourceNode, targetNode;
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
                PGgeometry geom_target = new PGgeometry(res.getObject("geom_dest").toString());

                location = Location.fromPoint((Point)geom_target.getGeometry());
                targetNode = nodes.getOrDefault(targetId, new VertexNode(location, targetId));
                nodes.put(targetId, targetNode);

                // retrieve shape
                shape = geomToShape((MultiLineString) new PGgeometry(res.getObject("geom_way").toString()).getGeometry());

                // update links set in graph
                connections = graph.getOrDefault(sourceNode, new HashSet<>());
                connections.add(new ShapedLink(targetNode, km, shape));
                graph.put(sourceNode, connections);

                connections = graph.getOrDefault(targetNode, new HashSet<>());
                connections.add(new ShapedLink(sourceNode, km, shape.reversed()));
                graph.put(targetNode, connections);

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
            sourceShape = List.of(stopNode.getLocation(), linkage.source().getLocation());
            targetShape = List.of(stopNode.getLocation(), linkage.target().getLocation());

            //creating two edges from a stop to the closest edge's vertices
            stopConnections.add(new ShapedLink(linkage.source(), linkage.kmSource() + EXTENSION_FACTOR, sourceShape));
            stopConnections.add(new ShapedLink(linkage.target(), linkage.kmTarget() + EXTENSION_FACTOR, targetShape));

            // creating two edges pointing from the closest edge's vertices to the stop
            fullGraph.getOrDefault(linkage.source(), new HashSet<>()).add(new ShapedLink(stopNode, linkage.kmSource() + EXTENSION_FACTOR, sourceShape.reversed()));
            fullGraph.getOrDefault(linkage.target(), new HashSet<>()).add(new ShapedLink(stopNode, linkage.kmTarget() + EXTENSION_FACTOR, targetShape.reversed()));
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

}
