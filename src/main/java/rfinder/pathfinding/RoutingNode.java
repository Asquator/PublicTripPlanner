package rfinder.pathfinding;

import rfinder.structures.graph.GraphNode;

import java.util.Objects;

public class RoutingNode<T extends GraphNode> implements Comparable<RoutingNode> {
    private final T current;
    private T previous;

    private double routeScore;
    private double estimatedScore;

    RoutingNode(T current) {
        this(current, null, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    RoutingNode(T current, T previous, double routeScore, double estimatedScore) {
        this.current = current;
        this.previous = previous;
        this.routeScore = routeScore;
        this.estimatedScore = estimatedScore;
    }

    public T getCurrent() {
        return current;
    }

    public T getPrevious() {
        return previous;
    }

    public double getRouteScore() {
        return routeScore;
    }

    public double getEstimatedScore() {
        return estimatedScore;
    }

    public void setRouteScore(double routeScore) {
        this.routeScore = routeScore;
    }

    public void setEstimatedScore(double estimatedScore) {
        this.estimatedScore = estimatedScore;
    }

    public void setPrevious(T previous) {
        this.previous = previous;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoutingNode<?> that = (RoutingNode<?>) o;
        return Objects.equals(current, that.current);
    }

    @Override
    public String toString() {
        return "RoutingNode{" +
                "current=" + current +
                ", previous=" + previous
                + '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(current);
    }

    @Override
    public int compareTo(RoutingNode other) {
        if (this.estimatedScore > other.estimatedScore) {
            return 1;
        } else if (this.estimatedScore < other.estimatedScore) {
            return -1;
        } else {
            return 0;
        }
    }
}
