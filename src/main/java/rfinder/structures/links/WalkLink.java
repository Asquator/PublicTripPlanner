package rfinder.structures.links;

import rfinder.dynamic.ECriteria;
import rfinder.query.result.NetworkQueryContext;
import rfinder.dynamic.label.DoubleMinLabel;
import rfinder.dynamic.label.Multilabel;
import rfinder.query.result.PathSegment;
import rfinder.query.result.WalkSegment;
import rfinder.structures.common.Location;
import rfinder.structures.nodes.PathNode;

import java.time.Duration;
import java.util.List;

public non-sealed class WalkLink extends LabeledLink {
    public WalkLink(PathNode destinationNode, Multilabel targetLabel) {
        super(destinationNode, targetLabel);
    }

    @Override
    public PathSegment toSegment(PathNode next, Multilabel nextLabel, NetworkQueryContext queryContext) {
        List<Location> shape = queryContext.queryContext().pathFinder().findPath(target(), next).getShape();
        WalkSegment walkSegment = new WalkSegment(shape,
                Duration.between(targetLabel().getArrivalTime(), nextLabel.getArrivalTime()),
                ((DoubleMinLabel) nextLabel.getLabel(ECriteria.WALKING_KM)).getCost() -
                        ((DoubleMinLabel) targetLabel().getLabel(ECriteria.WALKING_KM)).getCost());

        walkSegment.setStartTimeStamp(targetLabel().getArrivalTime());
        walkSegment.setEndTimeStamp(nextLabel.getArrivalTime());
        return walkSegment;
    }

    @Override
    public LabeledLink clone() throws CloneNotSupportedException {
        return (LabeledLink) super.clone();
    }
}
