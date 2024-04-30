package rfinder.query.result;

import rfinder.structures.common.Location;

import java.time.Duration;
import java.util.List;

public abstract class SegmentPathElement implements PathElement{
    private final List<Location> shape;
    private final Duration duration;
    private final double km;

    public SegmentPathElement(List<Location> shape, Duration duration, double km) {
        this.shape = shape;
        this.duration = duration;
        this.km = km;
    }

    @Override
    public List<Location> getShape() {
        return shape;
    }



    public Duration getDuration() {
        return duration;
    }

    public double getKm() {
        return km;
    }

}
