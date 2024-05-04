package rfinder.query;

import rfinder.dynamic.ECriteria;

import java.time.LocalDateTime;
import java.util.EnumSet;

public record QueryInfo(QueryPoint source, QueryPoint destination, LocalDateTime departureTime, EnumSet<ECriteria> criteria, int maxTrips, int nonDominating) {
    public QueryInfo {
        if(maxTrips > 5)
            throw new IllegalArgumentException("too many transfers allowed");

        if(nonDominating > 5)
            throw new IllegalArgumentException("too many non-dominating labels allowed");
    }

}
