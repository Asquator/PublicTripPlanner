package rfinder.query.result;
import rfinder.structures.common.Location;

public class StopElement extends NominalPathElement{
    private final String name;
    private final int stopId;

    public StopElement(int stopId, Location location, String name) {
        super(location);
        this.stopId = stopId;
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
