package rfinder.dynamic;

import rfinder.structures.common.Trip;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

public record TripInstance(Trip trip, List<LocalDateTime> stopTimes) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TripInstance that = (TripInstance) o;
        return Objects.equals(trip, that.trip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trip);
    }
}
