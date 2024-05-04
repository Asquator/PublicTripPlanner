package rfinder.query.result;

import javafx.scene.Node;
import rfinder.client.view.NodeSolutionExtractor;
import rfinder.structures.common.Location;

import java.time.LocalDateTime;
import java.util.List;

public class TerminalPathElement extends AbstractPathElement {

    private String name;


    public TerminalPathElement(Location location) {
        super(List.of(location));
        name = location.toString();
    }

    @Override
    public Color defaultColor() {
        return Color.TRANSPARENT;
    }

    @Override
    public Node getNodeWith(NodeSolutionExtractor extractor) {
        return extractor.createNode(this);
    }

    void setName(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
