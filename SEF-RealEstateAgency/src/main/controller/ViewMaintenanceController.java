package main.controller;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.util.Callback;
import main.model.Property.RentalProperty;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class ViewMaintenanceController {

    @FXML
    private DatePicker nextMaintenanceDatePicker;

    @FXML
    private Button scheduleBtn;

    @FXML
    private Label error;

    @FXML
    private TextField prevMaintenanceField;

    private RentalProperty property;

    public void setProperty(RentalProperty property) {
        this.property = property;

        if (property.getNextMaintenance().isAfter(property.getPreviousMaintenance()))
            prevMaintenanceField.setText(property.getNextMaintenance().format(DateTimeFormatter.ofPattern("MM/dd/yy")));
        else
//        if (property.getDateListed().toLocalDate().isEqual(property.getPreviousMaintenance())) {
            prevMaintenanceField.setPromptText("No previous maintenance record");
//        }


        //can't schedule next appointment yet if previous maintenance not done
        if (property.getNextMaintenance().isAfter(LocalDate.now(ZoneId.systemDefault()))) {
            scheduleBtn.setDisable(true);
            nextMaintenanceDatePicker.setDisable(true);
        }
    }

    private final BooleanProperty canSubmit = new SimpleBooleanProperty();

    public BooleanProperty canSubmitProperty() {
        return canSubmit;
    }

    public final boolean isCanSubmit() {
        return canSubmitProperty().get();
    }

    public final void setCanSubmit(boolean selected) {
        canSubmitProperty().set(selected);
    }

    @FXML
    void initialize() {
        prevMaintenanceField.setDisable(true);

        nextMaintenanceDatePicker.setStyle("-fx-font-size: 16px;");

        final Callback<DatePicker, DateCell> dayCellFactory =
                new Callback<>() {
                    @Override
                    public DateCell call(final DatePicker datePicker) {
                        return new DateCell() {
                            @Override
                            public void updateItem(LocalDate item, boolean empty) {
                                super.updateItem(item, empty);

                                if (item.isBefore(LocalDate.now())) {
                                    setDisable(true);
                                    setStyle("-fx-background-color: #ffc0cb;");
                                }
                            }
                        };
                    }
                };

        nextMaintenanceDatePicker.setDayCellFactory(dayCellFactory);

        nextMaintenanceDatePicker.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                scheduleBtn.fire();
            }
        });
    }

    @FXML
    void scheduleAction(ActionEvent event) {
        boolean valid = true;

        if (nextMaintenanceDatePicker.getValue() == null) {
            error.setText("Please select a date.");
            valid = false;
        }

        if (valid)
            setCanSubmit(true);
    }

    public LocalDate getNextMaintenance() {
        return nextMaintenanceDatePicker.getValue();
    }
}
