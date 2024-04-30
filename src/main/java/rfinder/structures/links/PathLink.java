package rfinder.structures.links;

import rfinder.structures.nodes.PathNode;

public abstract sealed class PathLink implements Link<PathNode> permits LabeledLink {

    private final PathNode target;


    public PathLink(PathNode target){
        this.target = target;
    }

    @Override
    public PathNode target() {
        return target;
    }

    @Override
    public String toString() {
        return target.toString();
    }
}
