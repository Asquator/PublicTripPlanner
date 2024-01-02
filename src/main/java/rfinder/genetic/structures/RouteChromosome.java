package rfinder.genetic.structures;

import genetic.composite.Evaluable;
import rfinder.structures.nodes.PathNode;
import rfinder.structures.segments.*;

import java.util.ArrayList;
import java.util.Iterator;

public class RouteChromosome implements Evaluable, Iterable<PathLink> {

    private final PathNode startNode;
    private static final String separator = " | ";

    private final ArrayList<PathLink> links = new ArrayList<>();

    public RouteChromosome(PathNode startNode){
        this.startNode = startNode;
    }

    public void add(PathLink link){
        links.add(link);
    }

    @Override
    public Iterator<PathLink> iterator() {
        return links.iterator();
    }

    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(startNode);

        stringBuilder.append(separator);
        for(PathLink link : links){
            if(link instanceof RideLink rideLink)
                stringBuilder.append(rideLink.getTripPatternID()).append(separator)
                        .append(rideLink.getDestination());

            else
                stringBuilder.append(link);

            stringBuilder.append(separator);
        }

        return stringBuilder.toString();
    }

    @Override
    public double evaluate() {
        return 0;
    }
}
