package rfinder.structures.links;

import rfinder.dynamic.label.Multilabel;
import rfinder.query.result.NetworkQueryContext;
import rfinder.query.result.PathSegment;
import rfinder.structures.nodes.PathNode;

public abstract sealed class LabeledLink extends PathLink implements Cloneable permits RideLink, WalkLink {

    private final Multilabel targetLabel;
    public LabeledLink(PathNode node, Multilabel multilabel) {
        super(node);
        this.targetLabel = multilabel;
    }

    public Multilabel targetLabel(){
        return targetLabel;
    }

    @Override
    public LabeledLink clone() throws CloneNotSupportedException {
        return (LabeledLink) super.clone();
    }

    public abstract PathSegment toSegment(PathNode next, Multilabel nextLabel, NetworkQueryContext queryContext);

}
