
package view;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.mapsforge.core.model.LatLong;
import rfinder.structures.common.Location;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

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

    private static final Font DEFAULT_FONT = new Font("Arial", 20);

    private static final DecimalFormat df = new DecimalFormat("0.0000000");

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
}