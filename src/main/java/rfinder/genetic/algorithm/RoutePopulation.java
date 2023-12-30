package rfinder.genetic.algorithm;

import rfinder.genetic.structures.RouteChromosome;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class RoutePopulation implements Iterable<RouteChromosome>{
    private final Set<RouteChromosome> individuals = new HashSet<>();

    public void add(RouteChromosome solution){
        individuals.add(solution);
    }

    @Override
    public Iterator<RouteChromosome> iterator() {
        return individuals.iterator();
    }
}
