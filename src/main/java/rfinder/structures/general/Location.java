package rfinder.structures.general;

import net.postgis.jdbc.geometry.Point;

public abstract class Location {
    public abstract double latitude();
    public abstract double longitude();

    public abstract Point toPoint();

    public static Location fromPoint (Point point){
        return new PointLocation(point);
    }

    public static Location fromValues(double latitude, double longitude){
        return new ExplicitLocation(latitude, longitude);
    }

    @Override
    public String toString() {
        return "[latitude: " + latitude() + " longitude: " + longitude() + "]";
    }
}
