package rfinder.dynamic;

import rfinder.query.QueryInfo;

public interface TripStorage {
    TripRepository createTripRepo(QueryInfo queryInfo);
}
