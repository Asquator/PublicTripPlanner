package rfinder.dynamic;


class DoubleMinLabel extends Label {
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
    public String toString() {
        return "DoubleMinLabel{" +
                "cost=" + cost +
                '}';
    }

}

