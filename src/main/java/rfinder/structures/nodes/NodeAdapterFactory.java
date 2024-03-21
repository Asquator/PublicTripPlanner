package rfinder.structures.nodes;

import rfinder.structures.common.Location;

public class NodeAdapterFactory implements NodeFactory<GraphNodeAdapter> {
    private int counter;

    public NodeAdapterFactory() {
        counter = 0;
    }

    @Override
    public GraphNodeAdapter create(Location location) {
        counter = Math.subtractExact(counter, 1);
        return new GraphNodeAdapter(counter, location);
    }
}
