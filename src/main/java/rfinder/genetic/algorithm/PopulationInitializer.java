package rfinder.genetic.algorithm;

import rfinder.structures.nodes.PathNode;

public interface PopulationInitializer {
    RoutePopulation initializePopulation(PathNode source, PathNode destination);
}
