package rfinder.client.view;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import rfinder.client.view.map.MapController;
import rfinder.dynamic.ECriteria;
import rfinder.query.result.*;

import static rfinder.client.view.Images.loadImage;

public class SolutionCell extends ListCell<QuerySolution> {

    private static final Font DEFAULT_FONT = new Font("Arial", 20);
    private static final int ICON_SIZE = 20;

    private static final Image BUS_IMG = loadImage("bus.png");
    private static final Image WALKING_IMG = loadImage("pedestrian.png");
    private static final Image NEXT_IMG = loadImage("next.png");

    private static final Image SOURCE_IMG = loadImage(MapController.class, "marker_blue.png");
    private static final Image DESTINATION_IMG = loadImage(MapController.class, "marker.png");

    @Override
    protected void updateItem(QuerySolution item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setGraphic(null);
            return;
        }

        VBox solutionBox = new VBox();
        HBox description = new HBox();

        description.getChildren().add(new ImageView(SOURCE_IMG));
        description.getChildren().add(new ImageView(NEXT_IMG));


        for (PathElement element : item.elements()) {
            Node icon = createIcon(element);
            if (icon != null) {
                description.getChildren().add(icon);
                description.getChildren().add(new ImageView(NEXT_IMG));
            }
        }

        description.getChildren().add(new ImageView(DESTINATION_IMG));

        solutionBox.getChildren().add(description);
        Label tlabel = new Label();
        tlabel.setFont(DEFAULT_FONT);

        for (int i = 0; i < ECriteria.values().length; i++) {
            tlabel.setText(tlabel.getText() + ECriteria.values()[i].getName() + ": " + item.getFinalLabel().getLabels()[i].toString() + "\n");
        }

        solutionBox.getChildren().add(tlabel);

        setGraphic(solutionBox);
    }

    private Node createIcon(PathElement element) {
        HBox hbox = new HBox();
        if (element instanceof WalkSegment) {
            hbox.getChildren().add(new ImageView(WALKING_IMG));
        }
        else if (element instanceof RideSegment rideSegment) {
            hbox.getChildren().add(new ImageView(BUS_IMG));

            Label label = new Label(rideSegment.getRouteID().routeId());
            label.setFont(DEFAULT_FONT);
            hbox.getChildren().add(label);
        }

        else {
            return null;
        }

        return hbox;
    }
}
