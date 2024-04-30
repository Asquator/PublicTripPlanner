package rfinder.query;

import java.time.LocalDateTime;

public record QueryInfo(QueryPoint source, QueryPoint destination, LocalDateTime departureTime, int maxTrips) {
    public QueryInfo {
        if(maxTrips > 5)
            throw new IllegalArgumentException("too many transfers allowed");
    }
}
