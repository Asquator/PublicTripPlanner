package rfinder.structures.links;

import rfinder.dynamic.label.Multilabel;
import rfinder.dynamic.NetworkQueryContext;
import rfinder.query.result.PathSegment;
import rfinder.structures.common.RouteID;
import rfinder.structures.nodes.PathNode;

public abstract sealed class LabeledLink extends PathLink permits RideLink, WalkLink {

    private final Multilabel targetLabel;
    public LabeledLink(PathNode node, Multilabel multilabel) {
        super(node);
        this.targetLabel = multilabel;
    }

    public Multilabel targetLabel(){
        return targetLabel;
    }

    public abstract PathSegment toSegment(PathNode next, Multilabel nextLabel, NetworkQueryContext queryContext);

    public abstract boolean correspondsToRoute(RouteID routeID);
}
