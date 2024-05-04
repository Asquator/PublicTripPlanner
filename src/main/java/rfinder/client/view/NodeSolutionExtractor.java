package rfinder.client.view;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import rfinder.client.view.map.MapController;
import rfinder.query.result.*;

import java.time.format.DateTimeFormatter;

import static rfinder.client.view.Images.loadImage;

public interface NodeSolutionExtractor {

    DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    Font DEFAULT_FONT = new Font("Arial", 20);
    Image BUS_IMG = Images.loadImage("bus.png");
    Image NEXT_IMG = Images.loadImage("next.png");
    Image WALKING_IMG = Images.loadImage("pedestrian.png");

    Image SOURCE_IMG = loadImage(MapController.class, "marker_blue.png");
    Image DESTINATION_IMG = loadImage(MapController.class, "marker.png");

    Node extract(QuerySolution item);

    Node createNode(WalkSegment walkSegment);
    Node createNode(RideSegment rideSegment);
    Node createNode(StopView stopView);
    Node createNode(TerminalPathElement terminalPathElement);
}
