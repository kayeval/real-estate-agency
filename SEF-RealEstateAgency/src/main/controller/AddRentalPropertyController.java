package main.controller;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.util.converter.IntegerStringConverter;
import main.model.Property.PropertyType;
import main.model.Proposal.ContractDuration;

import java.util.HashSet;
import java.util.Set;

public class AddRentalPropertyController {

    @FXML
    private Label error;

    @FXML
    private ComboBox<PropertyType> cmbType;

    @FXML
    private TextField addressField;

    @FXML
    private TextField suburbsField;

    @FXML
    private TextField priceField;

    @FXML
    private TextField bedsField;

    @FXML
    private TextField bathsField;

    @FXML
    private TextField carsField;

    @FXML
    private CheckBox chk6Mos;

    @FXML
    private CheckBox chk1Yr;

    @FXML
    private CheckBox chk2Yrs;

    @FXML
    private Button saveBtn;

    private String address;
    private double price;
    private int baths;
    private int beds;
    private int cars;
    private String suburb;
    private PropertyType propertyType;
    private Set<ContractDuration> contractDurations;

    private final BooleanProperty hasSaved = new SimpleBooleanProperty();

    public BooleanProperty hasSavedProperty() {
        return hasSaved;
    }

    public final boolean isSaved() {
        return hasSavedProperty().get();
    }

    public final void setSaved(boolean saved) {
        hasSavedProperty().set(saved);
    }

    @FXML
    void initialize() {
        cmbType.getItems().setAll(PropertyType.values());
        cmbType.getSelectionModel().selectFirst();

        addressField.textProperty().addListener((observable) -> {
            error.setText("");
        });

        priceField.textProperty().addListener((observable) -> {
            error.setText("");
        });

        suburbsField.textProperty().addListener((observable) -> {
            error.setText("");
        });

        bedsField.textProperty().addListener((observable) -> {
            error.setText("");
        });

        bathsField.textProperty().addListener((observable) -> {
            error.setText("");
        });

        carsField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                saveBtn.fire();
            }
        });

        addressField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                saveBtn.fire();
            }
        });

        suburbsField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                saveBtn.fire();
            }
        });

        priceField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                saveBtn.fire();
            }
        });

        bedsField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                saveBtn.fire();
            }
        });

        bathsField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                saveBtn.fire();
            }
        });

        carsField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                saveBtn.fire();
            }
        });

        TextFormatter formatter = new TextFormatter(new DecimalFilter());
        priceField.setTextFormatter(formatter);

        bathsField.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, new IntegerFilter()));
        carsField.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, new IntegerFilter()));
        bedsField.setTextFormatter(new TextFormatter<Integer>(new IntegerStringConverter(), 0, new IntegerFilter()));
    }

    @FXML
    void saveAction(ActionEvent event) {
        boolean valid = true;

        if (priceField.getText().equals("") || addressField.getText().equals("") || suburbsField.getText().equals("") ||
                bathsField.getText().equals("") || bedsField.getText().equals("") || carsField.getText().equals("") ||
                (!chk2Yrs.isSelected() && !chk6Mos.isSelected() && !chk1Yr.isSelected())) {
            error.setText("Please fill up all fields.");
            valid = false;
        } else if (!priceField.getText().equals("") && Double.parseDouble(priceField.getText()) < 0) {
            error.setText("Price cannot be negative.");
            valid = false;
        }

        if (valid) {
            contractDurations = new HashSet<>();
            suburb = suburbsField.getText();
            address = addressField.getText();
            price = Double.parseDouble(priceField.getText());
            baths = Integer.parseInt(bathsField.getText());
            beds = Integer.parseInt(bedsField.getText());
            cars = Integer.parseInt(carsField.getText());
            propertyType = cmbType.getSelectionModel().getSelectedItem();

            if (chk1Yr.isSelected())
                contractDurations.add(ContractDuration.ONE_YEAR);
            if (chk6Mos.isSelected())
                contractDurations.add(ContractDuration.SIX_MONTHS);
            if (chk2Yrs.isSelected())
                contractDurations.add(ContractDuration.TWO_YEARS);
            setSaved(true);
        }
    }

    public String getSuburb() {
        return suburb;
    }

    public String getAddress() {
        return address;
    }

    public double getPrice() {
        return price;
    }

    public PropertyType getPropertyType() {
        return propertyType;
    }

    public int getBaths() {
        return baths;
    }

    public int getBeds() {
        return beds;
    }

    public int getCars() {
        return cars;
    }

    public Set<ContractDuration> getContractDurations() {
        return contractDurations;
    }
}
