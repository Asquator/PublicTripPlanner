package rfinder.structures.common;

import net.postgis.jdbc.geometry.Point;

final class ExplicitLocation extends Location{
    private final double latitude;
    private final double longitude;

    ExplicitLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public Point toPoint() {
        return new Point(longitude, latitude);
    }

    public double latitude() {
        return latitude;
    }

    public double longitude() {
        return longitude;
    }

}
