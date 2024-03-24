package rfinder.query;

import java.util.Objects;

public non-sealed class StopPoint implements QueryPoint  {
    private final int stopId;

    public StopPoint(int stopId) {
        this.stopId = stopId;
    }

    public int stopId() {
        return stopId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (StopPoint) obj;
        return this.stopId == that.stopId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(stopId);
    }

    @Override
    public String toString() {
        return "StopPoint[" +
                "stopId=" + stopId + ']';
    }


}
