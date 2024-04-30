package rfinder.structures.links;

import rfinder.dynamic.label.Multilabel;
import rfinder.dynamic.NetworkQueryContext;
import rfinder.query.result.PathSegment;
import rfinder.query.result.RideSegment;
import rfinder.structures.common.Location;
import rfinder.structures.common.RouteID;
import rfinder.structures.nodes.PathNode;
import rfinder.structures.nodes.StopNode;

import java.util.List;

public non-sealed class RideLink extends LabeledLink {
    private final RouteID routeID;
    private final int tripSequence;
    private final int sourceSequence;
    private int destSequence;

    public RideLink(StopNode sourceNode, int sourceSequence, RouteID routeID, int tripSequence, Multilabel label) {
        super(sourceNode, label);
        this.routeID = routeID;
        this.tripSequence = tripSequence;
        this.sourceSequence = sourceSequence;
    }

    public void setDestSequence(int destSequence) {
        this.destSequence = destSequence;
    }

    public int getTripSequence() {
        return tripSequence;
    }

    public RouteID getRouteID() {
        return routeID;
    }

    public int getSourceSequence() {
        return sourceSequence;
    }

    public int getDestSequence() {
        return destSequence;
    }

    @Override
    public boolean correspondsToRoute(RouteID routeID) {
        return this.routeID.routeId().equals(routeID.routeId());
    }

    @Override
    public PathSegment toSegment(PathNode next, Multilabel nextLabel, NetworkQueryContext queryContext) {
        List<Location> shape = queryContext.routeDAO().getShapeAlongRoute(routeID, target().getLocation(), next.getLocation());
        return new RideSegment(routeID, sourceSequence, destSequence, shape);
    }

    @Override
    public String toString() {
        return super.toString() +"RideLink{" +
                "tripSequence=" + tripSequence +
                '}';
    }



}
