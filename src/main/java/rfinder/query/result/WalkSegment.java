package rfinder.query.result;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import rfinder.client.view.NodeSolutionExtractor;
import rfinder.structures.common.Location;

import java.time.Duration;
import java.util.List;

public class WalkSegment extends PathSegment implements PathElement{

    private final Duration duration;

    private final double km;

    public WalkSegment(List<Location> shape, Duration duration, double km){
        super(shape);
        this.duration = duration;
        this.km = km;
    }

    public Duration getDuration() {
        return duration;
    }

    public double getKm() {
        return km;
    }

    @Override
    public Color defaultColor() {
        return Color.GREEN;
    }

    @Override
    public Node getNodeWith(NodeSolutionExtractor extractor) {
        return extractor.createNode(this);
    }

}
