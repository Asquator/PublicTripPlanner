package rfinder.genetic.algorithm;

import rfinder.model.RouteDAO;
import rfinder.genetic.structures.PopulationTraits;
import rfinder.structures.nodes.PathNode;

public class RoutePopulationInitializer implements PopulationInitializer {

    RouteDAO dao;
    PopulationTraits traits;
    public RoutePopulationInitializer(RouteDAO dao, PopulationTraits traits){
        this.dao = dao;
        this.traits = traits;
    }

    @Override
    public RoutePopulation initializePopulation(PathNode source, PathNode destination) {
        int transfers = 0;
        return null;
    }
}
