package tests;

public class DynamicTest {
    public static void main(String[] args) {
/*

        InMemoryNetworkGraph graph = new InMemoryNetworkGraph(new DefaultDAO());
        StopDAO stopDAO = new StopLinkDAO();

        LocalDateTime odt = LocalDateTime.now();

        QueryInfo qi = new QueryInfo(new StopPoint(717), new StopPoint(984), odt, 2, 1);


        DynamicContext context = new DynamicContext(new DefaultContextRetriever(), new DefaultRouteDAO(), new DefaultFootpathDAO());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("UTC"));
        System.out.println(formatter.format(odt));
        context.initializeStorage(graph, qi);

        long startTime = System.currentTimeMillis();
        NetworkQueryContext result = context.compute();
        long finishTime = System.currentTimeMillis();

        */
/*

                context.getLabelRepo().forEach((k, v) -> {
                    if(!(v.getRoundLabels()[2].isEmpty()))
                        System.out.println(v.getStopNode() + " " + v.getRoundLabels()[2].size() + " " + v.getRoundLabels()[2]);
                });
        *//*


        System.out.println("Time: " + (finishTime - startTime));

        ResultExtractor extractor = new SingleExtremumExtractor(new DefaultRouteDAO(), ECriteria.ARRIVAL_TIME);
        QuerySolution qres = extractor.extract(result);

        System.out.println(qres);
*/

    }
}
