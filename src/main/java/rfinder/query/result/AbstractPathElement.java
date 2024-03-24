package rfinder.query.result;

import rfinder.structures.common.Location;

import java.util.List;

public class AbstractPathElement implements PathElement {

    private List<Location> shape;

    public AbstractPathElement(List<Location> shape){
        this.shape = shape;
    }

    @Override
    public List<Location> getShape() {
        return shape;
    }
}
