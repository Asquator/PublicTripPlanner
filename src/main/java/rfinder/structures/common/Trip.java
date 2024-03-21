package rfinder.structures.common;

import java.time.LocalDate;
import java.util.Objects;

public record Trip(String uniqueTripID, LocalDate date) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trip trip = (Trip) o;
        return Objects.equals(uniqueTripID, trip.uniqueTripID) && Objects.equals(date, trip.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uniqueTripID);
    }
}
