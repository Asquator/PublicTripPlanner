package rfinder.pathfinding;

import rfinder.cache.FactoryCache;
import rfinder.dao.FootpathDAO;
import rfinder.dao.TransferDAO;
import rfinder.structures.nodes.PathNode;

public class FootpathCache extends FactoryCache<PathNode, TotalSourcePathContext<PathNode>> {

    final static long TTL = 60 * 1000;

    public FootpathCache(FootpathDAO footpathDAO, TransferDAO transferDAO, double walkRadius) {
        super(new TotalFootpathSourceContextFactory(footpathDAO, transferDAO, walkRadius), TTL, TTL / 10);
    }

    @Override
    public void remove(PathNode key) {
        get(key).clearContext();
    }
}
