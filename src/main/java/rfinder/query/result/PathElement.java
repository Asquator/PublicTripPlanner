package rfinder.query.result;

import javafx.scene.Node;
import org.mapsforge.core.graphics.Color;
import rfinder.client.view.NodeSolutionExtractor;
import rfinder.structures.common.Location;

import java.util.List;

public interface PathElement {
    enum Color {
        RED("RED"),
        BLUE("BLUE"),
        GREEN("GREEN"),
        TRANSPARENT("TRANSPARENT");

        Color(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        private final String name;
    }

    List<Location> getShape();

    Color defaultColor();

    Node getNodeWith(NodeSolutionExtractor extractor);

}