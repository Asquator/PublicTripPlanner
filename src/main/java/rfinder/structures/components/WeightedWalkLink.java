package rfinder.structures.components;

import rfinder.structures.nodes.PathNode;

public class WeightedWalkLink extends DefaultWeightedLink<PathNode> {

    public WeightedWalkLink(PathNode destinationNode, double weight) {
        super(destinationNode, weight);
    }
}
