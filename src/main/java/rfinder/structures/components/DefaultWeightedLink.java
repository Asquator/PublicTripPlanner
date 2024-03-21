package rfinder.structures.components;

public class DefaultWeightedLink<T> implements WeightedLink<T> {
    private final T target;

    private final double weight;

    public DefaultWeightedLink(T destinationNode, double weight){
        this.target = destinationNode;
        this.weight = weight;
    }

    public T target(){
        return target;
    }

    public double weight(){
        return weight;
    }
}
