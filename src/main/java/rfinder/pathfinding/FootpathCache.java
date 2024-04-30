package rfinder.pathfinding;

import rfinder.cache.FactoryCache;
import rfinder.dao.FootpathDAO;
import rfinder.structures.nodes.PathNode;

public class FootpathCache extends FactoryCache<PathNode, TotalSharedSourcePathContext<PathNode>> {

    public FootpathCache(FootpathDAO dao, double walkRadius) {
        super(new TotalFootpathSourceContextFactory(dao, walkRadius));
    }

}
