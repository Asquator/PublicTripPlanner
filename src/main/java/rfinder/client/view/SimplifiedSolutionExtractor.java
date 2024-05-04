package rfinder.client.view;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import rfinder.dynamic.ECriteria;
import rfinder.query.result.*;

import static rfinder.client.view.Images.loadImage;

public class SimplifiedSolutionExtractor implements NodeSolutionExtractor {

    public Node extract(QuerySolution item) {

        VBox solutionBox = new VBox();

        solutionBox.getChildren().add(extractDescription(item));
        Label tlabel = new Label();
        tlabel.setFont(DEFAULT_FONT);

        for (int i = 0; i < ECriteria.values().length; i++) {
            tlabel.setText(tlabel.getText() + ECriteria.values()[i].getParamName() + ": " + item.getFinalLabel().getLabels()[i].toString() + "\n");
        }

        solutionBox.getChildren().add(tlabel);

        return solutionBox;
    }

    private Node extractDescription(QuerySolution querySolution){

        HBox description = new HBox();
        description.getChildren().add(new ImageView(SOURCE_IMG));
        description.getChildren().add(new ImageView(NEXT_IMG));


        for (PathElement element : querySolution.elements()) {
            Node icon = element.getNodeWith(this);
            if (icon != null) {
                description.getChildren().add(icon);
                description.getChildren().add(new ImageView(NEXT_IMG));
            }
        }

        description.getChildren().add(new ImageView(DESTINATION_IMG));
        return description;
    }

    @Override
    public Node createNode(WalkSegment walkSegment) {
        HBox hbox = new HBox();
        hbox.getChildren().add(new ImageView(WALKING_IMG));
        return hbox;
    }

    @Override
    public Node createNode(RideSegment rideSegment) {
        HBox hbox = new HBox();
        hbox.getChildren().add(new ImageView(BUS_IMG));

        Label label = new Label(rideSegment.getRouteID().routeId());
        label.setFont(DEFAULT_FONT);
        hbox.getChildren().add(label);
        return hbox;
    }

    @Override
    public Node createNode(StopView stopView) {
        return null;
    }

    @Override
    public Node createNode(TerminalPathElement terminalPathElement) {
        return null;
    }

}
