package tests;

import rfinder.dao.PostgisRouteDAO;
import rfinder.model.network.transport.PostgisTransportGraph;
import rfinder.structures.common.Location;
import rfinder.structures.nodes.StopNode;
import rfinder.structures.segments.RideSegment;

import java.util.Set;

public class TransportNetworkTest {
    public static void main(String[] args) {
        PostgisTransportGraph graph = new PostgisTransportGraph();
        PostgisRouteDAO dao = new PostgisRouteDAO();

        Location loc = dao.getStopLocation("104");
        Set<RideSegment> links = graph.getLinks(new StopNode(loc, "104"));

        links.forEach((RideSegment rs) -> System.out.println(rs));

        Set<StopNode> sr = dao.getStopsInRadius(loc, 1);
        sr.forEach((node) -> System.out.println(node));
    }
}
