package rfinder.query;

import rfinder.dynamic.ECriteria;

import java.time.LocalDateTime;
import java.util.EnumSet;

public record QueryInfo(QueryPoint source, QueryPoint destination, LocalDateTime departureTime, EnumSet<ECriteria> criteria, int maxTrips, int dominating) {
    public QueryInfo {
        if(maxTrips > 5 || maxTrips < 1)
            throw new IllegalArgumentException("too many transfers allowed");

        if(dominating > 5 || dominating < 1)
            throw new IllegalArgumentException("too many non-dominating labels allowed");
    }

}
