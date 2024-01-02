package rfinder.genetic.structures;

import genetic.composite.Evaluable;
import rfinder.structures.nodes.PathNode;
import rfinder.structures.segments.PathSegment;
import rfinder.structures.segments.RideSegment;
import rfinder.structures.segments.TransferWalkSegment;
import rfinder.structures.segments.WalkSegment;

import java.util.ArrayList;
import java.util.Iterator;

public class RouteChromosome implements Evaluable, Iterable<PathSegment> {

    private ArrayList<PathSegment> segments = new ArrayList<>();

    public void add(PathSegment segment){
        segments.add(segment);
    }

    @Override
    public Iterator<PathSegment> iterator() {
        return segments.iterator();
    }

    @Override
    public String toString() {
        PathSegment segment;

        StringBuilder stringBuilder = new StringBuilder();
        Iterator<PathSegment> iterator = segments.iterator();
        segment = iterator.next();
        stringBuilder.append(segment.getSource());

    do{
        stringBuilder.append(segment.getSource());

        if (segment instanceof WalkSegment)
            stringBuilder.append("-walk->");

        if (segment instanceof RideSegment)
            stringBuilder.append(((RideSegment) (segment)).getTripPatternID());

        segment = iterator.next();
    } while (iterator.hasNext());

        stringBuilder.append(segments.get(segments.size() - 1));

        return stringBuilder.toString();
    }

    @Override
    public double evaluate() {
        return 0;
    }
}
