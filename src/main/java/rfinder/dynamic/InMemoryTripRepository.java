package rfinder.dynamic;

import rfinder.dao.DBManager;
import rfinder.structures.common.RouteID;
import rfinder.structures.common.Trip;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.*;

public class TripRepository implements TripRepositoryi {

    private static final String STOP_TIMES = "select * from get_current_trips_format();";
    private static final String TRIP_PATTERNS = "select * from trip_patterns;";

    private final HashMap<RouteID, List<TripInstance>> allTrips = new HashMap<>();

    public HashMap<RouteID, List<TripInstance>> getAllTrips() {
        return allTrips;
    }

    private record RouteContext(RouteID routeID, List<TripInstance> trips){}

    @Override
    public List<TripInstance> getRelevantTrips(RouteID routeID){
        return Collections.unmodifiableList(allTrips.getOrDefault(routeID, Collections.emptyList()));
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
        LocalDate initDate = lastDate;

        List<OffsetDateTime> stopTimes = new ArrayList<>();

        do {
            tripID = res.getString("trip_id");
            date = res.getObject("date", LocalDate.class);

            if(!tripID.equals(lastTripID) || !date.equals(lastDate))
                break;

            OffsetDateTime stopTime = res.getObject("t_departure", OffsetDateTime.class);
            stopTimes.add(stopTime);

        } while (res.next());

        return new TripInstance(new Trip(lastTripID, initDate), stopTimes);
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
