package rfinder.query.result;


import rfinder.structures.common.Location;

import java.time.LocalDateTime;
import java.util.List;

public abstract class PathSegment implements PathElement{

    private final List<Location> shape;


    private LocalDateTime startTimeStamp;
    private LocalDateTime endTimeStamp;


    public PathSegment(List<Location> shape) {
        this.shape = shape;
    }

    public LocalDateTime getStartTimeStamp() {
        return startTimeStamp;
    }

    public LocalDateTime getEndTimeStamp() {
        return endTimeStamp;
    }

    public void setStartTimeStamp(LocalDateTime startTimeStamp) {
        this.startTimeStamp = startTimeStamp;
    }

    public void setEndTimeStamp(LocalDateTime endTimeStamp) {
        this.endTimeStamp = endTimeStamp;
    }

    @Override
    public List<Location> getShape() {
        return shape;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
