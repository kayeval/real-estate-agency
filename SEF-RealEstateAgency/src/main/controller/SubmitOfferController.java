package main.controller;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import main.model.DecimalFilter;
import main.model.Property.Property;
import main.model.User.User;

public class SubmitOfferController {

    @FXML
    private TextField listedPriceField;

    @FXML
    private TextField proposedPriceField;

    @FXML
    private TextField applicantField;

    @FXML
    private Label error;

    @FXML
    private Button saveBtn;

    private Property property;
    private User user;

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

    public void setProperty(Property property) {
        this.property = property;
        listedPriceField.setText("" + property.getPrice());
    }

    public void setUser(User user) {
        this.user = user;
        applicantField.setText(user.getUsername());
    }

    @FXML
    void initialize() {
        listedPriceField.setDisable(true);
        applicantField.setDisable(true);

        proposedPriceField.textProperty().addListener((observable) -> {
            error.setText("");
        });

        proposedPriceField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                saveBtn.fire();
            }
        });

        TextFormatter formatter = new TextFormatter(new DecimalFilter());
        proposedPriceField.setTextFormatter(formatter);
    }

    @FXML
    void saveAction(ActionEvent event) {
        boolean valid = true;

        if (proposedPriceField.getText().equals("")) {
            error.setText("Please fill up all fields.");
            valid = false;
        } else if (!proposedPriceField.getText().equals("") && Double.parseDouble(proposedPriceField.getText()) < 0) {
            error.setText("Price cannot be negative.");
            valid = false;
        }

        if (valid)
            setCanSubmit(true);
    }

    public Double getProposedPrice() {
        return Double.parseDouble(proposedPriceField.getText());
    }
}
