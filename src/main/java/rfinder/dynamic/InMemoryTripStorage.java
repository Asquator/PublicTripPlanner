package rfinder.dynamic;

import javafx.util.Duration;
import rfinder.dao.DBManager;
import rfinder.query.QueryInfo;
import rfinder.structures.common.RouteID;
import rfinder.structures.common.Trip;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.temporal.TemporalAmount;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public class InMemoryTripStorage implements TripStorage {

    private static final String STOP_TIMES = "select * from get_current_trips_format()";

    private final HashMap<RouteID, List<TripInstance>> allTrips = new HashMap<>();

    private record RouteContext(RouteID routeID, List<TripInstance> trips){}

    private Duration retrievalBackDuration;
    private Duration retrievalForwardDuration;


    public InMemoryTripStorage(Duration retrievalBackDuration, Duration retrievalForwardDuration) {
        this.retrievalBackDuration = retrievalBackDuration;
        this.retrievalForwardDuration = retrievalForwardDuration;

        try {
            updateStopTimes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public InMemoryTripStorage(String retrievalBackDuration, String retrievalForwardDuration) {
        this(Duration.valueOf(retrievalBackDuration), Duration.valueOf(retrievalForwardDuration));
    }


    private static <T, V> int findLeastElement(List<T> list, Function<T,V> getter, V threshold, BiFunction<V, V, Boolean> lessThan, int low, int high){
        // binary search for earliest trip for the given route
        int mid;
        int result = -1;

        while (low <= high) {
            mid = (low + high) / 2;
            //TripInstance tripInstance = list.get(mid);threshold.isBefore(tripInstance.stopTimes().getFirst())

            if (lessThan.apply(threshold, getter.apply(list.get(mid)))) {
                result = mid;
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }

        return result;
    }


    public class TripRepositoryView implements TripRepository{

        private final Map<RouteID, List<TripInstance>> tripView = new HashMap<>();

        public TripRepositoryView(LocalDateTime minTime, LocalDateTime maxTime) {
            for(Map.Entry<RouteID, List<TripInstance>> entry : allTrips.entrySet()){
                List<TripInstance> trips = entry.getValue();
                int least = findLeastElement(trips, (TripInstance instance) -> instance.stopTimes().getFirst(), minTime, LocalDateTime::isBefore, 0, trips.size() - 1);
                int greatest = findLeastElement(trips, (TripInstance instance) -> instance.stopTimes().getFirst(), maxTime, LocalDateTime::isBefore, 0, trips.size() - 1);

                if(least >= 0 && greatest >= 0)
                    tripView.put(entry.getKey(), trips.subList(least, greatest + 1));
            }

        }

        @Override
        public List<TripInstance> getRelevantTrips(RouteID routeID) {
            return tripView.getOrDefault(routeID, Collections.emptyList());
        }
    }

    @Override
    public TripRepository createTripRepo(QueryInfo queryInfo){
        return new TripRepositoryView(queryInfo.departureTime().minusSeconds((long)retrievalBackDuration.toSeconds()), queryInfo.departureTime().plusSeconds((long)retrievalForwardDuration.toSeconds()));
    }

    public void updateStopTimes() throws IOException {
        allTrips.clear();
        boolean finished;

        try(Connection connection = DBManager.newConnection()) {
            Statement stat = connection.createStatement();
            ResultSet res = stat.executeQuery(STOP_TIMES);

            finished = !res.isBeforeFirst();

            if(!res.next())
                return;

            while(!finished){
                RouteContext parsedRoute = parseRoute(res);

                // append trips if the route already exists
                if(allTrips.containsKey(parsedRoute.routeID))
                    allTrips.get(parsedRoute.routeID).addAll(parsedRoute.trips);

                // otherwise add new route
                else
                    allTrips.put(parsedRoute.routeID, parsedRoute.trips);

                finished = res.isAfterLast();
            }
        }
        catch (SQLException ex){
            throw new IOException(ex);
        }
    }

    private TripInstance parseTrip(ResultSet res) throws SQLException{

        String tripID, lastTripID = res.getString("trip_id");
        LocalDate date, lastDate = res.getObject("date", LocalDate.class);

        List<LocalDateTime> stopTimes = new ArrayList<>();

        do {
            tripID = res.getString("trip_id");
            date = res.getObject("date", LocalDate.class);

            if(!tripID.equals(lastTripID) || !date.equals(lastDate))
                break;

            LocalDateTime stopTime = res.getObject("t_departure", OffsetDateTime.class).toLocalDateTime();
            stopTimes.add(stopTime);

        } while (res.next());

        return new TripInstance(new Trip(lastTripID, lastDate), stopTimes);
    }



    private RouteContext parseRoute(ResultSet res) throws SQLException {
        boolean finished = false;

        String routeID, lastRouteID = res.getString("route_id");
        byte direction, lastDirection = res.getByte("direction_id");

        List<TripInstance> trips = new ArrayList<>();

        do {
            routeID = res.getString("route_id");
            direction = res.getByte("direction_id");
            if(!routeID.equals(lastRouteID) || direction != lastDirection)
                break;

            trips.add(parseTrip(res));
            finished = res.isAfterLast();
        } while(!finished);

        return new RouteContext(new RouteID(lastRouteID, lastDirection), trips);
    }



}
