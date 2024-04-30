package rfinder.query.result;

import org.mapsforge.core.graphics.Color;
import rfinder.structures.common.Location;

import java.util.List;

public abstract class AbstractPathElement implements PathElement {

    private List<Location> shape;

    public AbstractPathElement(List<Location> shape){
        this.shape = shape;
    }

    @Override
    public List<Location> getShape() {
        return shape;
    }

}
