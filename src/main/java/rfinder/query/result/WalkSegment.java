package rfinder.query.result;

import rfinder.structures.common.Location;

import java.time.Duration;
import java.util.List;

public class WalkSegment extends PathSegment implements PathElement{

    private final Duration duration;

    private final double km;

    public WalkSegment(List<Location> shape, Duration duration, double km){
        super(shape);
        this.duration = duration;
        this.km = km;
    }

    public Duration getDuration() {
        return duration;
    }

    public double getKm() {
        return km;
    }

    @Override
    public Color defaultColor() {
        return Color.GREEN;
    }

    @Override
    public String toString() {
        return "walk";
    }
}
