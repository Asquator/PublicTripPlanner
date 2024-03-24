package rfinder.dao;

import rfinder.query.QueryInfo;
import rfinder.structures.common.RouteID;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DefaultContextRetriever implements ContextRetriever {

    private static final String TRIP_PATTERNS = "select * from trip_patterns";
    private static final String TRIPS_IN_RANGE = "";

    private final Connection connection = DBManager.newConnection();

    @Override
    public Set<RouteID> getRelevantRoutes(QueryInfo context) {
        Set<RouteID> ret = new HashSet<>();

        ResultSet res;

        try (PreparedStatement statement = connection.prepareStatement(TRIP_PATTERNS)){
            res = statement.executeQuery();

            while(res.next()){
                RouteID routeID = new RouteID(res.getString("route_id"), res.getByte("direction_id"));
                ret.add(routeID);
            }
        }
        catch (SQLException ex){
            throw new RuntimeException(ex);
        }

        return ret;
    }
}
