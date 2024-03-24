package rfinder.dao;

public final class PostgisQuery {
    public static final String LOC_BY_VERTEX = "select geom from vertex_map where id = ?";



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


    public static final String TRANSPORT_LINKS = "select * from transport_network where source_stop = ?";

    public static final String TRANSPORT_BACKWARDS = "select * from transport_network where dest_stop = ?";

    public static final String TRANSPORT_LINKS_CONT = "select * from transport_network tn1 where source_stop=?\n" +
            "and exists(select 1\n" +
            "\t\t   from transport_network tn2 \n" +
            "\t\t   where tn2.source_stop = tn1.dest_stop and (tn2.routeId, tn2.direction) <> (tn1.routeId, tn1.direction))";

    public static final String TRANSPORT_LINKS_BACKWARDS_CONT = "select * from transport_network tn1 where dest_stop=?\n" +
            "and exists(select 1\n" +
            "\t\t   from transport_network tn2 \n" +
            "\t\t   where tn2.dest_stop = tn1.source_stop and (tn2.routeId, tn2.direction) <> (tn1.routeId, tn1.direction))";

}

