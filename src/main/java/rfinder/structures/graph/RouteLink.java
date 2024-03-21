package rfinder.structures.graph;

import rfinder.structures.components.WeightedLink;

import java.util.Objects;

public record RouteLink<T extends GraphNode>(T destination, double weight) implements WeightedLink<T> {

    @Override
    public String toString() {
        return "[target: " + destination + "][weight: " + weight + "]";
    }


    // compare by destination only
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RouteLink<?> routeLink = (RouteLink<?>) o;
        return Objects.equals(destination, routeLink.destination);
    }

    @Override
    public int hashCode() {
        return Objects.hash(destination);
    }

    @Override
    public T target() {
        return destination;
    }
}