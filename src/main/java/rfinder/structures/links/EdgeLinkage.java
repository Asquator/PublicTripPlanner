package rfinder.structures.links;

import rfinder.structures.common.Location;
import rfinder.structures.common.UnorderedPair;
import rfinder.structures.nodes.PathNode;
import rfinder.structures.nodes.VertexNode;

import java.util.List;
import java.util.Objects;

public class EdgeLinkage {
    private final PathNode closest;
    private final PathNode source;
    private final PathNode target;

    private final double kmSource;
    private final double kmTarget;
    public EdgeLinkage(PathNode closest,
                       PathNode source,
                       double kmSource,
                       PathNode target,
                       double kmTarget) {
        this.closest = closest;
        this.source = source;
        this.target = target;

        this.kmSource = kmSource;
        this.kmTarget = kmTarget;
    }

    public UnorderedPair<PathNode> edgeId() {
        return new UnorderedPair<>(source, target);
    }

    public PathNode closest() {
        return closest;
    }


    public PathNode source() {
        return source;
    }


    public PathNode target() {
        return target;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (EdgeLinkage) obj;
        return Objects.equals(this.closest, that.closest) &&
                Objects.equals(this.source, that.source) &&
                Objects.equals(this.target, that.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(closest, source, target);
    }

}
