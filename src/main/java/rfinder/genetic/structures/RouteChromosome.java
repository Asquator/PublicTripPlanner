package rfinder.genetic.structures;

import genetic.composite.Evaluable;
import rfinder.structures.segments.PathSegment;

import java.util.ArrayList;

public class RouteChromosome implements Evaluable {

    private ArrayList<PathSegment> segments = new ArrayList<>();



    @Override
    public double evaluate() {
        return 0;
    }
}
