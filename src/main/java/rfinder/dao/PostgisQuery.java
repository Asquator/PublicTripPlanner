package rfinder.dao;

public final class PostgisQuery {
    public static final String LOC_BY_STOP = "select stop_loc from stops where stop_id = ?";

    public static final String LOC_BY_VERTEX = "select geom from vertex_map where id = ?";

    public static final String CLOSEST_VERTEX_BY_LOC = "select id, geom\n" +
            "from vertex_map\n" +
            "order by geom <-> ?\n" +
            "limit 1";

    public static final String CONNECTIONS_OF_VERTEX = "select target, geom_dest as geom, km\n" +
            "from edge_map\n" +
            "where source = ?\n" +
            "union\n" +
            "select source, geom_source as geom, km\n" +
            "from edge_map\n" +
            "where target = ?";

    public static final String GEOM_BY_VERTEX = "select geom from vertices where id = ?";

    public static final String VERTEX_EUCLIDEAN_DISTANCE = "select ST_DistanceSphere(v1.geom, v2.geom) / 1000 " +
            "from vertices v1, vertices v2 " +
            "where v1.id = ? and v2.id = ?";

    public static final String ALL_EDGES = "select source, target, km, geom_source, geom_dest from edge_map " +
            "union " +
            "select target as source, source as target, km, geom_dest as geom_source, geom_source as geom_dest from edge_map";

    public static final String CLOSEST_STOPS_IN_RADIUS = "select stop_id, stop_loc\n" +
            "from stops\n" +
            "where ST_Distance(?, stop_loc::geography) / 1000 < ?";

    public static final String TRANSPORT_LINKS = "select * from transport_network where source_stop = ?";

    public static final String TRANSPORT_LINKS_CONT = "select * from transport_network tn1 where source_stop=?\n" +
            "and exists(select 1\n" +
            "\t\t   from transport_network tn2 \n" +
            "\t\t   where tn2.source_stop = tn1.dest_stop and (tn2.route_id, tn2.shape_id) <> (tn1.route_id, tn1.shape_id))";
}

