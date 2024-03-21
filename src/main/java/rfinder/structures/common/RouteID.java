package rfinder.structures.common;

import java.util.Objects;

public record RouteID(String routeId, byte direction) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RouteID that = (RouteID) o;
        return Objects.equals(routeId, that.routeId) && Objects.equals(direction, that.direction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(routeId, direction);
    }

    @Override
    public String toString() {
        return "[" + routeId + " : " + direction + "]";
    }
}
