package rfinder.structures.nodes;

import rfinder.structures.common.Location;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NodeAdapterFactory implements NodeFactory<GraphNodeAdapter> {
    private int counter;

    private final Lock lock = new ReentrantLock();

    public NodeAdapterFactory() {
        counter = 0;
    }

    @Override
    public GraphNodeAdapter create(Location location) {
        lock.lock();
        try {
            counter = Math.subtractExact(counter, 1);
        } catch (ArithmeticException e) {
            counter = -1;
        }

        return new GraphNodeAdapter(counter, location);
    }
}
