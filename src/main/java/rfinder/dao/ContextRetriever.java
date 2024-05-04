package rfinder.dao;

import rfinder.query.QueryInfo;
import rfinder.structures.common.RouteID;

import java.util.Set;

public interface ContextRetriever {
    Set<RouteID> getRelevantRoutes(QueryInfo context);
}
