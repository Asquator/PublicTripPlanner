package rfinder.genetic.algorithm;

import rfinder.genetic.structures.RouteChromosome;
import rfinder.dao.RouteDAO;
import rfinder.genetic.structures.PopulationTraits;
import rfinder.structures.nodes.PathNode;
import rfinder.structures.nodes.StopNode;

import java.util.Random;

public class RandomizedPopulationInitializer implements PopulationInitializer {

    private final RouteDAO dao;
    private final PopulationTraits traits;
    private final Random rnd = new Random();

    public RandomizedPopulationInitializer(RouteDAO dao, PopulationTraits traits){
        this.dao = dao;
        this.traits = traits;
    }

    @Override
    public RoutePopulation initializePopulation(PathNode source, PathNode destination) {
        RoutePopulation population = new RoutePopulation();
        RouteChromosome solution;

        int counter = 0;

        while(counter < traits.maxSize()){
            solution = generateNewSolution(source, destination);
            population.add(solution);
            counter++;
        }

        return population;
    }

    private RouteChromosome generateNewSolution(PathNode source, PathNode destination){
        RouteChromosome solution = new RouteChromosome();
        StopNode stop, sourceStop;
        int maxTransfers = traits.maxTransfers();

        maxTransfers = Integer.min(traits.maxTransfers(), rnd.nextInt(maxTransfers + 1));
        int transfers = 0;

        sourceStop = source instanceof StopNode ? (StopNode) source : null;

        do {

        } while (transfers < maxTransfers);

        return solution;
    }


}
