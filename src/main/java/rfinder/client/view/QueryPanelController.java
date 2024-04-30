
package rfinder.client.view;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.mapsforge.core.model.LatLong;
import rfinder.client.QuerySubmitter;
import rfinder.client.view.map.MapController;
import rfinder.query.LocationPoint;
import rfinder.query.QueryInfo;
import rfinder.query.result.QuerySolution;
import rfinder.service.LocalQuerySubmitter;
import rfinder.structures.common.Location;

import java.text.DecimalFormat;
import java.time.*;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public final class QueryPanelController {

    @FXML
    private Spinner<Integer> minutesSpinner;

    @FXML
    private Spinner<Integer> hoursSpinner;

    @FXML
    private DatePicker datePicker;

    @FXML
    private VBox sidePane;

    @FXML
    private TextField sourceField;

    @FXML
    private TextField destinationField;

    @FXML
    private ListView<QuerySolution> solutionListView;

    private QuerySubmitter submitter;

    private static final Font DEFAULT_FONT = new Font("Arial", 20);

    private static final DecimalFormat df = new DecimalFormat("0.0000000");
    private MapController mapController;

    private final ObservableList<QuerySolution> solutions = FXCollections.observableArrayList();

    public void setMapController(MapController mapController) {
        this.mapController = mapController;
    }

    private QueryInfo createQuery(){
        LocalDateTime timestamp = getTimestamp();

        return new QueryInfo(new LocationPoint(sourceLocation()), new LocationPoint(destinationLocation()),
                timestamp,3);
    }

    public void initialize(){

        // set default values
        minutesSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59));
        hoursSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23));

        // set editor fonts
        minutesSpinner.getEditor().setFont(DEFAULT_FONT);
        hoursSpinner.getEditor().setFont(DEFAULT_FONT);
        datePicker.getEditor().setFont(DEFAULT_FONT);

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

        submitter = new LocalQuerySubmitter();

        // initialize list view
        solutionListView.setItems(solutions);
        solutionListView.setCellFactory(querySolutionListView -> new SolutionCell());
        solutionListView.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<QuerySolution>() {
                    @Override
                    public void changed(ObservableValue<? extends QuerySolution> observableValue, QuerySolution oldValue, QuerySolution newValue) {
                        mapController.displaySolution(newValue);
                    }
                }
        );
    }

    public Location sourceLocation(){
        return Location.fromValues(Double.parseDouble(sourceField.getText().split(",")[0]), Double.parseDouble(sourceField.getText().split(",")[1]));
    }

    public Location destinationLocation(){
        return Location.fromValues(Double.parseDouble(destinationField.getText().split(",")[0]), Double.parseDouble(destinationField.getText().split(",")[1]));
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
        submitter.submit(createQuery()).thenApply(result -> {

            Platform.runLater(()->{
                solutions.clear();
                solutions.addAll(result);
            });

            mapController.clearDisplayedSolutions();
            mapController.addSolutions(result);
            mapController.displaySolution(result.getFirst());

            return true;
        });


    }
}