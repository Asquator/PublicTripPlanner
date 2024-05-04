package rfinder.dynamic.label;


import java.util.Objects;

public class DoubleMinLabel extends Label{

    private double cost = 0;

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    @Override
    public int compareTo(Label label) {
        if (getClass() != label.getClass())
            throw new RuntimeException("illegal comparison: " + getClass() + " and " + label.getClass());

        // lowest cost = highest fitness
        return -Double.compare(cost, ((DoubleMinLabel) label).cost);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoubleMinLabel that = (DoubleMinLabel) o;
        return Double.compare(cost, that.cost) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cost);
    }

    @Override
    public String toString() {
        return String.format("%.2f", cost);
    }

}

