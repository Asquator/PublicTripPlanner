package rfinder.structures.links;

import rfinder.structures.common.Location;
import rfinder.structures.graph.RouteLink;
import rfinder.structures.nodes.PathNode;

import java.util.List;

public class ShapedLink extends RouteLink<PathNode> {

    private final List<Location> shape;

    public ShapedLink(PathNode destination, double weight, List<Location> shape) {
        super(destination, weight);
        this.shape = shape;
    }

    public List<Location> getShape() {
        return shape;
    }

    public ShapedLink reversed(PathNode source){
        return new ShapedLink(source, weight(), shape.reversed());
    }
}
