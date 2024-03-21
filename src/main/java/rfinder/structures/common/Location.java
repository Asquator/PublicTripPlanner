package rfinder.structures.common;

import net.postgis.jdbc.geometry.Point;

public abstract class Location {
    public abstract double latitude();
    public abstract double longitude();

    public static Location fromPoint (Point point){
        return new PointLocation(point);
    }

    public static Location fromValues(double latitude, double longitude){
        return new ExplicitLocation(latitude, longitude);
    }

    public Point toPoint(){
        return new Point(longitude(), latitude());
    }

    @Override
    public String toString() {
        return "[latitude: " + latitude() + " longitude: " + longitude() + "]";
    }


}
