/*
package tests;

import rfinder.dao.DefaultDAO;
import rfinder.dao.RoadDAO;
import rfinder.dynamic.TripRepository;
import rfinder.structures.common.Location;
import rfinder.structures.common.RouteID;
import rfinder.structures.nodes.PathNode;

import java.io.IOException;

public class TripTest
{
    public static void main(String[] args) throws IOException {
        RoadDAO dao = new DefaultDAO();
        System.out.println(dao.getLinkage(new PathNode(Location.fromValues( 38.18575527072693, 13.341093075069733))));

        TripRepository tripRepository = new TripRepository();
        tripRepository.updateStopTimes();
        var trips = tripRepository.getAllTrips();

        for(var v : trips.get(new RouteID("N12", (byte)0)))
            System.out.println(v.stopTimes().size());

    }
}
*/
