package rfinder.dynamic;

import rfinder.structures.components.TripSequentialLink;
import rfinder.structures.nodes.StopNode;

public class RouteMultilabel extends Multilabel{
    public RouteMultilabel(Multilabel other, StopNode sourceStop, int tripSequence){
        super(other);
        setBackwardLink(new TripSequentialLink(sourceStop, tripSequence));
    }

    public int getTripSequence() {
        return ((TripSequentialLink) getBackwardLink()).getTripSequence();
    }
}
