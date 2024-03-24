package rfinder.structures.components;

import rfinder.query.result.PathElement;
import rfinder.structures.nodes.StopNode;

public non-sealed class NetworkTripLink extends PathLink {
    public NetworkTripLink(StopNode target) {
        super(target);
    }

    @Override
    public StopNode target() {
        return (StopNode)super.target();
    }
}
