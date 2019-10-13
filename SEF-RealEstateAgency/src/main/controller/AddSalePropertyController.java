package main.controller;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.util.converter.IntegerStringConverter;
import main.model.Property.PropertyType;

public class AddSalePropertyController {
    @FXML
    private TextField carsField;

    @FXML
    private ComboBox<PropertyType> cmbType;

    @FXML
    private TextField suburbsField;

    @FXML
    private TextField priceField;

    @FXML
    private TextField bedsField;

    @FXML
    private Label error;

    @FXML
    private TextField addressField;

    @FXML
    private TextField bathsField;

    @FXML
    private Button saveBtn;

    private String address;
    private double price;
    private int baths;
    private int beds;
    private int cars;
    private String suburb;
    private PropertyType propertyType;

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
                bathsField.getText().equals("") || bedsField.getText().equals("") || carsField.getText().equals("")) {
            error.setText("Please fill up all fields.");
            valid = false;
        } else if (!priceField.getText().equals("") && Double.parseDouble(priceField.getText()) < 0) {
            error.setText("Price cannot be negative.");
            valid = false;
        }

        if (valid) {
            suburb = suburbsField.getText();
            address = addressField.getText();
            price = Double.parseDouble(priceField.getText());
            baths = Integer.parseInt(bathsField.getText());
            beds = Integer.parseInt(bedsField.getText());
            cars = Integer.parseInt(carsField.getText());
            propertyType = cmbType.getSelectionModel().getSelectedItem();

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
}
