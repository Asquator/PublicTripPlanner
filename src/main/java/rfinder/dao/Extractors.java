package rfinder.dao;

import net.postgis.jdbc.PGgeometry;
import net.postgis.jdbc.geometry.Point;
import rfinder.pathfinding.EdgeLinkage;
import rfinder.structures.common.Location;
import rfinder.structures.nodes.NodeFactory;
import rfinder.structures.nodes.VertexNode;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class Extractors {
    public static EdgeLinkage extractLink(ResultSet res, NodeFactory factory) throws SQLException {

        Location closestPoint;
        VertexNode source, dest;
        double kmSource, kmTarget, kmClosest;

        closestPoint = Location.fromPoint((Point) (new PGgeometry(res.getObject("geom_pt").toString())).getGeometry());
        kmClosest = res.getDouble("distance");

        kmSource = res.getDouble("dist_source");
        source = new VertexNode(Location.fromPoint((Point) (new PGgeometry(res.getObject("geom_source").toString())).getGeometry()), res.getInt("source"));

        kmTarget = res.getDouble("dist_dest");
        dest = new VertexNode(Location.fromPoint((Point) (new PGgeometry(res.getObject("geom_dest").toString())).getGeometry()), res.getInt("target"));

        return new EdgeLinkage(factory.create(closestPoint), kmClosest, source, kmSource, dest, kmTarget);
    }

  /*  public static EdgeLinkage extractLink(ResultSet res, int knownId) throws SQLException {
        return extractLink(res, new NodeFactory() {
            @Override
            public PathNode create(Location location) {
                return new PathNode(location, knownId);
            }
        });
    }*/
}
