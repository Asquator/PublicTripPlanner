package rfinder.genetic.structures;

import genetic.composite.Evaluable;
import rfinder.structures.segments.PathSegment;

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
    public double evaluate() {
        return 0;
    }
}
