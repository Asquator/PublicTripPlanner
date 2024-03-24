package rfinder.query.result;

import rfinder.structures.common.Location;

import java.time.Duration;
import java.util.List;

public class NominalPathElement extends AbstractPathElement {

    public NominalPathElement(Location location){
        super(List.of(location));
    }

}
