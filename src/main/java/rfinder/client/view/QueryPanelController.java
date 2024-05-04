
package rfinder.client.view;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.mapsforge.core.model.LatLong;
import rfinder.client.QuerySubmitter;
import rfinder.client.view.map.MapController;
import rfinder.dynamic.ECriteria;
import rfinder.query.LocationPoint;
import rfinder.query.QueryInfo;
import rfinder.query.result.QuerySolution;
import rfinder.service.LocalQuerySubmitter;
import rfinder.structures.common.Location;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static rfinder.client.view.Images.loadImage;

public final class QueryPanelController {

    @FXML
    private Spinner<Integer> minutesSpinner;

    @FXML
    private Spinner<Integer> hoursSpinner;

    @FXML
    private Spinner<Integer> maxTripsSpinner;

    @FXML
    private Spinner<Integer> nondominatingSpinner;

    @FXML
    private DatePicker datePicker;

    @FXML
    private VBox sidePane;

    @FXML
    private BorderPane rootPane;

    @FXML
    private TextField sourceField;

    @FXML
    private TextField destinationField;

    @FXML
    private ListView<QuerySolution> solutionListView;

    @FXML
    private Button searchButton;

    @FXML
    private GridPane criteriaPanel;


    private QuerySubmitter submitter;

    private static final Font DEFAULT_HEAD_FONT = new Font("Arial", 20);

    public static final Font DEFAULT_SEC_FONT = new Font("Arial", 16);

    private static final Image ROUTE_ICON = loadImage(QueryPanelController.class, "route_icon.png", 50, 50);


    private static final DecimalFormat df = new DecimalFormat("0.0000000");
    private MapController mapController;

    private CompletableFuture<List<QuerySolution>> currentFuture;

    private final ObservableList<QuerySolution> solutions = FXCollections.observableArrayList();

    public void setMapController(MapController mapController) {
        this.mapController = mapController;
    }

    private QueryInfo createQuery(){
        LocalDateTime timestamp = getTimestamp();
        EnumSet<ECriteria> parameters = EnumSet.noneOf(ECriteria.class);

        for (Node child : criteriaPanel.getChildren()) {
            CheckBox checkBox = (CheckBox) child;
            if (checkBox.isSelected())
                parameters.add((ECriteria) checkBox.getUserData());

        }

        return new QueryInfo(new LocationPoint(sourceLocation()), new LocationPoint(destinationLocation()),
                timestamp, parameters, maxTripsSpinner.getValue(), nondominatingSpinner.getValue());
    }

    public void initialize(){
        ImageView searchImg = new ImageView(ROUTE_ICON);

        searchButton.setStyle("-fx-background-color: #1E90FF; -fx-background-radius: 40;");
        searchButton.setGraphic(searchImg);
        searchButton.setContentDisplay(ContentDisplay.RIGHT);

        // set default values
        minutesSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59));
        hoursSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23));

        maxTripsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5));
        nondominatingSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 4));

        maxTripsSpinner.getValueFactory().setValue(3);
        nondominatingSpinner.getValueFactory().setValue(1);


        // set editor fonts
        minutesSpinner.getEditor().setFont(DEFAULT_HEAD_FONT);
        hoursSpinner.getEditor().setFont(DEFAULT_HEAD_FONT);
        datePicker.getEditor().setFont(DEFAULT_HEAD_FONT);

        // activate handlers for date/time changes
        minutesSpinner.getEditor().textProperty().addListener((observableValue, oldValue, newValue) -> {
            minutesSpinner.commitValue();
            if(!Objects.equals(oldValue, newValue)) {
                timestampChanged();
            }
        });

        hoursSpinner.getEditor().textProperty().addListener((observableValue, oldValue, newValue) -> {
            hoursSpinner.commitValue();
            if(!Objects.equals(oldValue, newValue))
                timestampChanged();
        });
        datePicker.valueProperty().addListener((observableValue, localDate, t1) -> timestampChanged());

        // set default values
        datePicker.setValue(LocalDate.now());
        hoursSpinner.getValueFactory().setValue(LocalTime.now().getHour());
        minutesSpinner.getValueFactory().setValue(LocalTime.now().getMinute());

        minutesSpinner.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_VERTICAL);
        hoursSpinner.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_VERTICAL);

        // connect to query submitter
        submitter = new LocalQuerySubmitter();

        SimplifiedSolutionExtractor simplifiedExtractor = new SimplifiedSolutionExtractor();
        DetailedSolutionExtractor detailedExtractor = new DetailedSolutionExtractor();

        // initialize list view
        solutionListView.setItems(solutions);
        solutionListView.setCellFactory(querySolutionListView -> {

            ListCell<QuerySolution> cell = new ListCell<>();
            BorderPane cellRoot = new BorderPane();

            // item changed handlers
            cell.itemProperty().addListener((observableValue, oldValue, newValue) -> {
                if (newValue != null) {
                    cellRoot.setCenter(simplifiedExtractor.extract(newValue));
                }
            });

            cell.emptyProperty().addListener((observableValue, wasEmpty, isEmpty) -> {
                if (isEmpty) {
                    cell.setGraphic(null);
                } else {
                    cell.setGraphic(cellRoot);
                }
            });

            return cell;
        });

        // selected handlers
        solutionListView.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldValue, newValue) -> {
                    if(newValue != null)
                        mapController.displaySolution(newValue);
                }
        );


        // handler to extract and display a detailed solution
        solutionListView.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, oldValue, newValue) -> {

                    if(newValue == null)
                        return;

                    FXMLLoader fxmlLoader = new FXMLLoader(QueryPanelController.class.getResource("detailed_solution.fxml"));

                    Scene scene;

                    try {
                        scene = new Scene(fxmlLoader.load());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    DetailedSolutionController controller = (DetailedSolutionController) fxmlLoader.getController();

                    Platform.runLater(()->{
                        solutionListView.getSelectionModel().clearSelection();

                        // close the scene on clicking back
                        controller.backPressedProperty().addListener((observableValue1, oldValue1, newValue1) -> {
                            rootPane.setCenter(sidePane);
                        });

                        controller.setMainNode(detailedExtractor.extract(newValue));

                        rootPane.setCenter(scene.getRoot());
                    });
                }
        );

        // initialize criteria panel
        criteriaPanel.setVgap(8);
        criteriaPanel.setHgap(4);

        int cols = criteriaPanel.getColumnCount();
        int row = 0, col = 0;

        for(ECriteria criteria : ECriteria.values()){
            CheckBox checkBox = new CheckBox(criteria.getCharacteristicName());
            checkBox.setFont(DEFAULT_SEC_FONT);
            checkBox.setUserData(criteria);
            criteriaPanel.add(checkBox, row, col++);

            if (col == cols){
                col = 0;
                row++;
            }
        }


    }

    public Location sourceLocation(){
        return Location.fromValues(Double.parseDouble(sourceField.getText().split(",")[0]),
                Double.parseDouble(sourceField.getText().split(",")[1]));
    }

    public Location destinationLocation(){
        return Location.fromValues(Double.parseDouble(destinationField.getText().split(",")[0]),
                Double.parseDouble(destinationField.getText().split(",")[1]));
    }

    public TextField getSourceField() {
        return sourceField;
    }

    public TextField getDestinationField() {
        return destinationField;
    }

    public void setSourcePoint(LatLong latLong){
        sourceField.setText(df.format(latLong.latitude) + ", " + df.format(latLong.longitude));
    }

    public void setDestinationPoint(LatLong latLong){
        destinationField.setText(df.format(latLong.latitude) + ", " + df.format(latLong.longitude));
    }

    public void timestampChanged(){
        System.out.println(getTimestamp());
    }

    public LocalDateTime getTimestamp(){
        return LocalDateTime.of(datePicker.getValue(), LocalTime.of(hoursSpinner.getValue(), minutesSpinner.getValue()));
    }

    @FXML
    void searchClicked(ActionEvent event) {
        synchronized (this){
            if(currentFuture != null)
                currentFuture.cancel(true);
        }

        currentFuture = submitter.submit(createQuery());
        currentFuture.thenApply(result -> {

            Platform.runLater(()->{
                solutions.clear();
                solutions.addAll(result);
            });

            mapController.clearDisplayedSolutions();
            mapController.addSolutions(result);
            mapController.displaySolution(result.getFirst());

            return true;
        });

        currentFuture = null;
    }
}