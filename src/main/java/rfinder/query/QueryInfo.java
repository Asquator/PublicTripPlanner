package rfinder.query;

import java.time.OffsetDateTime;

public record QueryInfo(QueryPoint source, QueryPoint destination, OffsetDateTime departureTime, int maxTrips, double walkRadius) {
    public QueryInfo {
        if(maxTrips > 5)
            throw new IllegalArgumentException("too many transfers allowed");
    }
}
