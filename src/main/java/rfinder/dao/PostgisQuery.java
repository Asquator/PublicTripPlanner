package rfinder.dao;

public final class PostgisQuery {
    public static final String LOC_BY_STOP = "select stop_loc from stops where stop_id = ?";

    public static final String LOC_BY_VERTEX = "select geom from vertex_map where id = ?";

    public static final String CLOSEST_VERTEX_BY_LOC = "select id, geom\n" +
            "from vertex_map\n" +
            "order by geom <-> ?\n" +
            "limit 1";
}
