package rfinder.client.view;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

public class DetailedSolutionController {

    @FXML
    private Button backButton;


    @FXML
    private BorderPane mainPane;

    public ReadOnlyBooleanProperty backPressedProperty() {
        return backButton.pressedProperty();
    }

    public void setMainNode(Node mainNode) {
        this.mainPane.setCenter(mainNode);
    }
}
