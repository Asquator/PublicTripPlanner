package rfinder.dao;

import net.postgis.jdbc.PGgeometry;
import net.postgis.jdbc.geometry.Point;
import rfinder.dao.geo.GeoHelper;
import rfinder.pathfinding.EdgeCut;
import rfinder.pathfinding.InMemoryNetworkGraph;
import rfinder.structures.common.UnorderedPair;
import rfinder.structures.links.EdgeData;
import rfinder.structures.links.EdgeLinkage;
import rfinder.structures.links.ShapedLink;
import rfinder.structures.common.Location;
import rfinder.structures.nodes.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DefaultGraphDAO extends DBUser implements GraphDAO {

    public static final int SRID = 4326;
    private final DefaultStopDAO stopDAO = new DefaultStopDAO();

    public static final String ALL_EDGES = "select source, target, km, geom_source, geom_dest, geom_way from edge_map";

    private static final String CLOSEST_EDGE = "select * from get_closest_edge(?)";

    private static final String EDGE_CUT = "select * from edge_cut(?, ?, ?, ?)";


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
                shape = GeoHelper.geomToShape(new PGgeometry(res.getObject("geom_way").toString()).getGeometry());

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
    };

    public EdgeCut getEdgeCut(PathNode source, PathNode target, Location loc1, Location loc2){
        try(PreparedStatement statement = connection.prepareStatement(EDGE_CUT)){
            statement.setInt(1, source.id());
            statement.setInt(2, target.id());
            statement.setObject(3, new PGgeometry(loc1.toPoint()));
            statement.setObject(4, new PGgeometry(loc2.toPoint()));

            ResultSet res = statement.executeQuery();

            if(res.next()) {
                double km = res.getDouble("km");
                List<Location> shape = GeoHelper.geomToShape(new PGgeometry(res.getObject("geom").toString()).getGeometry());

                if(km < 0) {
                    shape = shape.reversed();
                    km = -km;
                }

                return new EdgeCut(shape, km);

            } else return null;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public InMemoryNetworkGraph getNetworkGraph(){

        final double EXTENSION_FACTOR = 0.0001;
        List<Location> sourceShape, targetShape;

        HashMap<PathNode, Set<ShapedLink>> graphConnnections = getFullRoadGraph();

        Map<UnorderedPair<PathNode>, EdgeData<PathNode>> edges = new HashMap<>();

        List<Map.Entry<Integer, EdgeLinkage>> linkedStops = stopDAO.getLinkedStops();

        for (Map.Entry<Integer, EdgeLinkage> stopEntry : linkedStops) {
            EdgeLinkage linkage = stopEntry.getValue();

            // closest stop is the one representing this stop, assume it lies on the edge
            StopNode stopNode = (StopNode) linkage.closest();

            // set of graph links
            Set<ShapedLink> stopConnections = new HashSet<>();

            graphConnnections.put(stopNode, stopConnections);

            // create an unordered pair representing the edge, (first, second) = (source, target)
            UnorderedPair<PathNode> edgeId = new UnorderedPair<>(linkage.source(), linkage.target());
            EdgeData<PathNode> edgeData = edges.getOrDefault(edgeId, new EdgeData<>());
            edges.put(edgeId, edgeData);

            edgeData.addLinkage(linkage.source());
            edgeData.addLinkage(linkage.target());

            // link back and forth for each already linked node. Extract and save the appropriate edge cuts
            edgeData.linkIterator().forEachRemaining(otherNode -> {
                EdgeCut edgeCut = getEdgeCut(linkage.source(), linkage.target(), stopNode.getLocation(), otherNode.getLocation());
                stopConnections.add(new ShapedLink(otherNode, edgeCut.km() + EXTENSION_FACTOR, edgeCut.shape()));
                graphConnnections.getOrDefault(otherNode, new HashSet<>()).add(
                        new ShapedLink(stopNode, edgeCut.km() + EXTENSION_FACTOR, edgeCut.shape().reversed()));
            });

            edgeData.addLinkage(stopNode);
        }

        return new InMemoryNetworkGraph(graphConnnections, edges);
    }


    @Override
    public EdgeLinkage getEdgeLinkage(Location location, NodeFactory nodeFactory) {
        ResultSet res;

        try (PreparedStatement statement = connection.prepareStatement(CLOSEST_EDGE)) {
            Point point = location.toPoint();
            point.setSrid(SRID);
            statement.setObject(1, new PGgeometry(point));
            res = statement.executeQuery();

            //if the location has been found, return the linkage
            if (res.next()) {
                return GeoHelper.extractLink(res, nodeFactory);
            } else
                throw new RuntimeException("Couldn't find vertex");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void closeConnection() {
        super.closeConnection();
        stopDAO.closeConnection();
    }
}
