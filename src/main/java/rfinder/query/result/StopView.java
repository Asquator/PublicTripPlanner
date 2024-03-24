package rfinder.query.result;

import rfinder.structures.common.Location;
import org.hibernate.*;

public class StopView extends NominalPathElement{

    private final int stopId;
    private final String name;

    public StopView(int stopId, Location location, String name) {
        super(location);
        this.stopId = stopId;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getStopId() {
        return stopId;
    }

}
