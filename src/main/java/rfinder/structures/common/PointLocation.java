package rfinder.structures.common;

import net.postgis.jdbc.geometry.Point;
import org.hibernate.annotations.Type;


class PointLocation extends Location{
    private final Point point;
    public PointLocation(Point point){
        this.point = point;
    }

    @Override
    public Point toPoint() {
        return point;
    }

    @Override
    public double latitude() {
        return point.y;
    }

    @Override
    public double longitude() {
        return point.x;
    }
}
