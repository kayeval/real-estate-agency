package main.controller;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.util.Callback;
import main.model.DBModel.InspectionDBModel;
import main.model.Property.Property;
import main.model.User.User;
import main.view.custom.DateTimePicker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.chrono.Chronology;
import java.time.temporal.ChronoUnit;
import java.util.Collection;

public class SubmitInspectionController {
    @FXML
    private ComboBox cmbCustomer;

    @FXML
    private Button scheduleBtn;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Label error;

    private final BooleanProperty isConfirmed = new SimpleBooleanProperty();

    public BooleanProperty isConfirmedProperty() {
        return isConfirmed;
    }

    public final boolean isConfirmed() {
        return isConfirmedProperty().get();
    }

    public final void setConfirmed(boolean confirmed) {
        isConfirmedProperty().set(confirmed);
    }

    private InspectionDBModel inspectionDBModel;

    private Collection<User> customers;

    private Property property;

    @FXML
    void initialize() {
        Callback<ListView<User>, ListCell<User>> cellFactory = new Callback<>() {
            @Override
            public ListCell<User> call(ListView<User> l) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(User item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(item.getUsername());
                        }
                    }
                };
            }
        };

        cmbCustomer.setButtonCell(cellFactory.call(null));
        cmbCustomer.setCellFactory(cellFactory);

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

        datePicker.setPromptText("yyyy-MM-dd HH:mm");
        datePicker.setDayCellFactory(dayCellFactory);
        datePicker.getEditor().textProperty().addListener((observable -> {
            error.setText("");
        }));

        datePicker.chronologyProperty().addListener((observable) -> {
            error.setText("");
        });

        datePicker.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                scheduleBtn.fire();
            }
        });
    }

    @FXML
    void scheduleAction(ActionEvent event) {
        boolean valid = true;

        if (datePicker.getValue() == null) {
            error.setText("Please specify a date and time.");
            valid = false;
        } else if (getDate().toLocalDate().isEqual(LocalDate.now(ZoneId.systemDefault())) &&
                getDate().toLocalTime().isBefore(LocalTime.now(ZoneId.systemDefault()).truncatedTo(ChronoUnit.HOURS))) { //same day but before current time
            error.setText("Please choose a later time.");
            valid = false;
        } else if (!inspectionDBModel.isTimeAvailable(property, ((DateTimePicker) datePicker).getDateTimeValue())) {
            error.setText("That time is already taken," +
                    "\nplease choose another timeslot.");
            valid = false;
        }

        if (valid)
            setConfirmed(true);
    }


    public void setInspectionDBModel(InspectionDBModel inspectionDBModel) {
        this.inspectionDBModel = inspectionDBModel;
    }

    public void setCustomers(Collection<User> customers) {
        this.customers = customers;

        cmbCustomer.getItems().addAll(customers);
        cmbCustomer.getSelectionModel().selectFirst();
    }

    public void setDueDateTime(LocalDateTime dateTime) {
        datePicker.setChronology(Chronology.from(dateTime));
    }

    public void setSelectedCustomer(User user) {
        cmbCustomer.getSelectionModel().select(user);
        cmbCustomer.setDisable(true);
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public User getSelectedUser() {
        return (User) cmbCustomer.getSelectionModel().getSelectedItem();
    }

    public LocalDateTime getDate() {
        return ((DateTimePicker) datePicker).getDateTimeValue();
    }
}
