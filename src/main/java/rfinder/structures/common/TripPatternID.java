package rfinder.structures.common;

import java.util.Objects;

public record TripPatternID(String route_id, String shape_id) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TripPatternID that = (TripPatternID) o;
        return Objects.equals(route_id, that.route_id) && Objects.equals(shape_id, that.shape_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(route_id, shape_id);
    }

    @Override
    public String toString() {
        return "[route id " + route_id + " shape " + shape_id + "]";
    }
}
