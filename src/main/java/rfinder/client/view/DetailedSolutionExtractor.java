package rfinder.client.view;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import rfinder.query.result.*;

import static rfinder.client.view.Images.loadImage;

public class DetailedSolutionExtractor implements NodeSolutionExtractor{

    private static final Image TERMINAL_POINT_IMG = loadImage(DetailedSolutionExtractor.class, "terminal_point.png", 15, 15);

    public Node extract(QuerySolution item) {

        VBox solutionBox = new VBox();

        for (PathElement element : item.elements()) {
            Node icon = element.getNodeWith(this);
            solutionBox.getChildren().add(icon);
        }

        return solutionBox;
    }

    public Node createNode(WalkSegment walkSegment){
        HBox hbox = new HBox();
        Label label, labelSegment;
        VBox vBox = new VBox();
        labelSegment = new Label(DATE_TIME_FORMATTER.format(walkSegment.getStartTimeStamp()) + " - " +
                DATE_TIME_FORMATTER.format(walkSegment.getEndTimeStamp()));
        labelSegment.setFont(DEFAULT_FONT);

        hbox.getChildren().add(new ImageView(WALKING_IMG));
        label = new Label(walkSegment.getDuration().toMinutes() + " minutes | " +
                String.format("%.2f", walkSegment.getKm()) + " km");

        vBox.getChildren().add(labelSegment);
        vBox.getChildren().add(label);
        hbox.getChildren().add(vBox);

        return hbox;
    }

    public Node createNode(RideSegment rideSegment){
        HBox hbox = new HBox();
        Label label, labelSegment;
        VBox vBox = new VBox();
        labelSegment = new Label(DATE_TIME_FORMATTER.format(rideSegment.getStartTimeStamp()) + " - "
                + DATE_TIME_FORMATTER.format(rideSegment.getEndTimeStamp()));
        labelSegment.setFont(DEFAULT_FONT);

        label = new Label(rideSegment.getRouteID().routeId() + "  " + rideSegment.numOfStops() + " stops");
        vBox.getChildren().add(labelSegment);
        vBox.getChildren().add(label);

        hbox.getChildren().add(new ImageView(BUS_IMG));
        hbox.getChildren().add(vBox);
        return hbox;
    }

    public Node createNode(StopView stopView){
        HBox hbox = new HBox();
        Label label;

        hbox.getChildren().add(new ImageView(TERMINAL_POINT_IMG));
        label = new Label(stopView.getName() + " | " + stopView.getStopId());
        hbox.getChildren().add(label);

        return hbox;
    }

    public Node createNode(TerminalPathElement terminalPathElement){
        HBox hbox = new HBox();
        hbox.getChildren().add(new ImageView(TERMINAL_POINT_IMG));
        hbox.getChildren().add(new Label(terminalPathElement.toString()));
        return hbox;
    }

}
