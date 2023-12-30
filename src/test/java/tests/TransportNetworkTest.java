package tests;

import rfinder.dao.PostgisDAO;
import rfinder.model.network.transport.PostgisTransportGraph;
import rfinder.structures.common.Location;
import rfinder.structures.nodes.StopNode;
import rfinder.structures.segments.RideSegment;

import java.util.Set;

public class TransportNetworkTest {
    public static void main(String[] args) {
        PostgisTransportGraph graph = new PostgisTransportGraph();
        PostgisDAO dao = new PostgisDAO();

        Location loc = dao.getStopLocation("123");
        Set<RideSegment> links = graph.getLinks(new StopNode(loc, "123"));

        links.forEach((RideSegment rs) -> System.out.println(rs));

        Set<StopNode> sr = dao.getStopsInRadius(loc, 1);
    }
}
