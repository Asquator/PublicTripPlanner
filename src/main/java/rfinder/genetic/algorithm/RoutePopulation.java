package rfinder.genetic.algorithm;

import rfinder.genetic.structures.RouteChromosome;

import java.util.Set;
import java.util.TreeSet;

public class RoutePopulation {
    private int size;
    private final Set<RouteChromosome> individuals = new TreeSet<>();

    public void add(RouteChromosome solution){
        individuals.add(solution);
    }


}
