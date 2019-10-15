package main.controller;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import main.model.DBModel.PropertyDBModel;
import main.model.DBModel.UserDBModel;
import main.model.DecimalFilter;
import main.model.IntegerFilter;
import main.model.Property.PropertyType;
import main.model.Proposal.ContractDuration;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class EditRentalPropertyController {

    @FXML
    private CheckBox chk2Yrs;

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
    private CheckBox chk1Yr;

    @FXML
    private CheckBox chk6Mos;

    @FXML
    private Button saveBtn;

    private PropertyDBModel propertyDBModel;
    private UserDBModel userDBModel;
    private String address, suburb, username;
    private int baths, beds, cars, propertyID, userID;
    private PropertyType propertyType;
    private Set<ContractDuration> contractDurations;
    private double price;

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

    public void start() {
        cmbType.getItems().setAll(PropertyType.values());
        cmbType.getSelectionModel().select(propertyType);

        if (contractDurations.contains(ContractDuration.ONE_YEAR))
            chk1Yr.setSelected(true);
        if (contractDurations.contains(ContractDuration.SIX_MONTHS))
            chk6Mos.setSelected(true);
        if (contractDurations.contains(ContractDuration.TWO_YEARS))
            chk2Yrs.setSelected(true);

        addressField.setText(address);
        suburbsField.setText(suburb);
        bathsField.setText(baths + "");
        bedsField.setText(beds + "");
        carsField.setText(cars + "");
        priceField.setText(price + "");
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

            Stage confirmChangeStage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/main/view/PasswordPrompt.fxml"));
            AnchorPane rootLayout = null;
            try {
                rootLayout = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            PasswordPromptController passwordPromptController = loader.getController();
            passwordPromptController.setUsername(username);
            passwordPromptController.setUserDBModel(userDBModel);
            passwordPromptController.hasConfirmedProperty().addListener((obs, wasConfirmed, isConfirmed) -> {
                if (isConfirmed) {
                    confirmChangeStage.hide();

                    propertyDBModel.updatePropertyDetails(address, suburb, propertyType, baths, cars, beds, price, contractDurations, userID, propertyID);

                    setSaved(true);
                }
            });

            Scene scene = new Scene(rootLayout);
            confirmChangeStage.setScene(scene);
            confirmChangeStage.show();
        }
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setBaths(int baths) {
        this.baths = baths;
    }

    public void setCars(int cars) {
        this.cars = cars;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setBeds(int beds) {
        this.beds = beds;
    }

    public void setPropertyType(PropertyType propertyType) {
        this.propertyType = propertyType;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPropertyDBModel(PropertyDBModel propertyDBModel) {
        this.propertyDBModel = propertyDBModel;
    }

    public void setUserDBModel(UserDBModel userDBModel) {
        this.userDBModel = userDBModel;
    }

    public void setPropertyID(int propertyID) {
        this.propertyID = propertyID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setContractDurations(Set<ContractDuration> contractDurations) {
        this.contractDurations = contractDurations;
    }
}

