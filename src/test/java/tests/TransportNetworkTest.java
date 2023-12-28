package tests;

import rfinder.db.PostgisRouteDAO;
import rfinder.model.RouteDAO;
import rfinder.structures.common.Location;
import rfinder.structures.nodes.StopNode;
import rfinder.structures.segments.RideSegment;

import java.util.Set;

public class TransportNetworkTest {
    public static void main(String[] args) {
        RouteDAO dao = new PostgisRouteDAO();

        Location loc = dao.getStopLocation("104");
        Set<RideSegment> links = dao.getTransportLinks(new StopNode(loc, "104"));

        links.forEach((RideSegment rs) -> System.out.println(rs));
    }
}
