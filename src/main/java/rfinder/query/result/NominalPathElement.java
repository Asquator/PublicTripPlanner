package rfinder.query.result;

import rfinder.structures.common.Location;

import java.time.Duration;
import java.util.List;

public class NominalPathElement extends AbstractPathElement {

    private String name;

    public NominalPathElement(Location location){
        super(List.of(location));
        name = location.toString();
    }

    @Override
    public Color defaultColor() {
        return Color.TRANSPARENT;
    }

    void setName(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
