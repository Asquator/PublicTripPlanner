package rfinder.structures.graph;

import java.util.Objects;

public final class RouteLink<T extends GraphNode> implements WeightedLink<T> {
    private final T destination;
    private final double weight;

    public RouteLink(T destination, double weight) {
        this.destination = destination;
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "[destination: " + destination + "][weight: " + weight + "]";
    }

    @Override
    public T getDestination() {
        return destination;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (RouteLink) obj;
        return Objects.equals(this.destination, that.destination) &&
                Double.doubleToLongBits(this.weight) == Double.doubleToLongBits(that.weight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(destination, weight);
    }

}