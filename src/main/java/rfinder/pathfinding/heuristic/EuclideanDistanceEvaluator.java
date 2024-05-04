package rfinder.pathfinding.heuristic;

import rfinder.pathfinding.HeuristicEvaluator;
import rfinder.structures.common.Location;
import rfinder.structures.nodes.PathNode;

public class EuclideanDistanceEvaluator implements HeuristicEvaluator<PathNode> {

    private static double euclideanDistance(double lat1, double lon1, double lat2, double lon2) {
        // Convert latitude and longitude from degrees to radians
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        // Calculate the differences in latitude and longitude
        double deltaLat = lat2Rad - lat1Rad;
        double deltaLon = lon2Rad - lon1Rad;

        // Use the Euclidean distance formula

        // Return the result in kilometers
        return 6371 * Math.sqrt(Math.pow(deltaLat, 2) + Math.pow(deltaLon, 2));
    }

    @Override
    public double evaluateHeuristic(PathNode from, PathNode to) {
        Location loc1 = from.getLocation();
        Location loc2 = to.getLocation();
        return euclideanDistance(loc1.latitude(), loc1.longitude(), loc2.latitude(), loc2.longitude());
    }
}
