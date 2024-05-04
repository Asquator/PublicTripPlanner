package rfinder.query.result;

import javafx.scene.Node;
import rfinder.client.view.NodeSolutionExtractor;
import rfinder.structures.common.Location;

public class StopView extends TerminalPathElement {

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

    @Override
    public Node getNodeWith(NodeSolutionExtractor extractor) {
        return extractor.createNode(this);
    }

    @Override
    public String toString() {
        return name.toUpperCase();
    }
}
