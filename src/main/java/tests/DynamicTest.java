package tests;

import rfinder.dao.*;
import rfinder.dynamic.DynamicContext;
import rfinder.query.QueryInfo;
import rfinder.model.network.walking.InMemoryNetworkGraph;
import rfinder.query.StopPoint;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DynamicTest {
    public static void main(String[] args) {

        InMemoryNetworkGraph graph = new InMemoryNetworkGraph(new DefaultDAO());
        StopDAO stopDAO = new StopLinkDAO();

        OffsetDateTime odt = OffsetDateTime.now();
        QueryInfo qi = new QueryInfo(new StopPoint(134), new StopPoint(1926), odt, 3, 0.5);


        DynamicContext context = new DynamicContext(new DefaultContextRetriever(), new DefaultRouteDAO(), new DefaultFootpathDAO());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("UTC"));
        System.out.println(formatter.format(odt));
        context.initializeStorage(graph, qi);

        long startTime = System.currentTimeMillis();
        context.compute();
        long finishTime = System.currentTimeMillis();

        /*

                context.getLabelRepo().forEach((k, v) -> {
                    if(!(v.getRoundLabels()[2].isEmpty()))
                        System.out.println(v.getStopNode() + " " + v.getRoundLabels()[2].size() + " " + v.getRoundLabels()[2]);
                });
        */

        System.out.println("Time: " + (finishTime - startTime));


    }
}
