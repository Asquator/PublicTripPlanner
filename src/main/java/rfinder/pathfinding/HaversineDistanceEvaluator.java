package rfinder.pathfinding;

import rfinder.structures.common.Location;
import rfinder.structures.nodes.PathNode;

public class HaversineDistanceEvaluator implements HeuristicEvaluator<PathNode> {
    @Override
    public double evaluateHeuristic(PathNode from, PathNode to) {
        Location loc1 = from.getLocation();
        Location loc2 = to.getLocation();
        return haversine(loc1.latitude(), loc1.longitude(), loc2.latitude(), loc2.longitude());
    }

    private static double haversine(double lat1, double lon1,
                                    double lat2, double lon2)
    {
        // distance between latitudes and longitudes
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        // convert to radians
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // apply formulae
        double a = Math.pow(Math.sin(dLat / 2), 2) +
                Math.pow(Math.sin(dLon / 2), 2) *
                        Math.cos(lat1) *
                        Math.cos(lat2);

        // earth radius in km
        double rad = 6371;
        double c = 2 * Math.asin(Math.sqrt(a));
        return rad * c;
    }
}
