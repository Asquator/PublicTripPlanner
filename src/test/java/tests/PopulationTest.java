package tests;

import rfinder.dao.PostgisDAO;
import rfinder.genetic.algorithm.RandomizedPopulationInitializer;
import rfinder.genetic.algorithm.RoutePopulation;
import rfinder.genetic.structures.PopulationTraits;
import rfinder.genetic.structures.RouteChromosome;
import rfinder.structures.nodes.PathNode;
import rfinder.structures.nodes.StopNode;
import rfinder.structures.segments.PathSegment;
import rfinder.structures.segments.RideSegment;

public class PopulationTest {
    public static void main(String[] args) {
        PostgisDAO dao = new PostgisDAO();
        PopulationTraits traits = new PopulationTraits(10, 4, 0.5f, 1);
        RandomizedPopulationInitializer initializer = new RandomizedPopulationInitializer(dao, traits);

        StopNode source = new StopNode(dao.getStopLocation("1965"), "1965");
        StopNode destination = new StopNode(dao.getStopLocation("606"), "606");

        RoutePopulation population = initializer.initializePopulation(source, destination);
        int counter = 1;


        for(RouteChromosome solution : population){
            System.out.println("\n\nSolution number " + counter++);
            System.out.println(solution);
        }

        System.out.println("\n\n");

        for(RouteChromosome solution : population){
            for(PathSegment segment : solution)
                System.out.println(segment);

            System.out.println("\n");
        }
    }
}
