package rfinder.dynamic;

import rfinder.structures.components.RideLink;

public class RouteMultilabel extends Multilabel{
    public RouteMultilabel(Multilabel other, RideLink link){
        super(other);
        setBackwardLink(link);
    }

    public int getTripSequence() {
        return ((RideLink) getBackwardLink()).getTripSequence();
    }
}
