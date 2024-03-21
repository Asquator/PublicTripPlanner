package rfinder.pathfinding;

import rfinder.structures.nodes.PathNode;

import java.util.List;

public class FootPath extends Path<PathNode> {

    public FootPath(List<? extends PathNode> path, double length) {
        super(path, length);
    }

    @Override
    public List<? extends PathNode> getPath() {
        return super.getPath();
    }
}

