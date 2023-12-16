package rfinder.structures.segments;

import rfinder.structures.nodes.StopNode;

public class RideSegment extends PathSegment{

    private int pattern_id;

    public RideSegment(StopNode source, StopNode destination){
        super(source, destination);
    }

    @Override
    public double evaluateFitness() {
        return 0;
    }
}
