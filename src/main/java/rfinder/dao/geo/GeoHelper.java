package rfinder.dao.geo;

import net.postgis.jdbc.PGgeometry;
import net.postgis.jdbc.geometry.*;
import rfinder.structures.common.Location;
import rfinder.structures.links.EdgeLinkage;
import rfinder.structures.nodes.NodeFactory;
import rfinder.structures.nodes.VertexNode;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class GeoHelper {
    @SuppressWarnings("unchecked")
    public static List<Location> geomToShape(Geometry geometry){

        if(geometry.getType() == Geometry.MULTILINESTRING) {
            List<Location> ret = new ArrayList<>();
            Arrays.stream(((MultiLineString) geometry).getLines()).forEach(l -> l.iterator().forEachRemaining(p -> ret.add(Location.fromPoint((Point) p))));
            return ret;
        }

        else if(geometry.getType() == Geometry.LINESTRING) {
            List<Location> ret = new ArrayList<>();
            ((LineString) geometry).iterator().forEachRemaining(p -> ret.add(Location.fromPoint((Point) p)));
            return ret;
        }

        else if(geometry.getType() == Geometry.GEOMETRYCOLLECTION) {
            GeometryCollection collection = (GeometryCollection) geometry;
            ArrayList<Location> ret = new ArrayList<>();

            for (int i = 0; i < collection.getGeometries().length; i++) {
                ret.addAll(geomToShape(collection.getGeometries()[i]));
            }

            return ret;
        }

        else if (geometry.getType() == Geometry.POINT) {
            return List.of(Location.fromPoint((Point) geometry));
        }

        throw new IllegalArgumentException("Unsupported geometry type");
    }

    public static EdgeLinkage extractLink(ResultSet res, NodeFactory factory) throws SQLException {

        Location closestPoint;
        VertexNode source, target;
        double kmSource, kmTarget, kmClosest;

        closestPoint = Location.fromPoint((Point) (new PGgeometry(res.getObject("geom_pt").toString())).getGeometry());
        kmClosest = res.getDouble("distance");

        kmSource = res.getDouble("dist_source");
        source = new VertexNode(Location.fromPoint((Point) (new PGgeometry(res.getObject("geom_source").toString())).getGeometry()), res.getInt("source"));

        kmTarget = res.getDouble("dist_dest");
        target = new VertexNode(Location.fromPoint((Point) (new PGgeometry(res.getObject("geom_dest").toString())).getGeometry()), res.getInt("target"));

        return new EdgeLinkage(factory.create(closestPoint), source, kmSource, target, kmTarget);
    }
}
