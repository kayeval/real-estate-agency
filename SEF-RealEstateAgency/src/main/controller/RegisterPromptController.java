package main.controller;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class RegisterPromptController {

    @FXML
    private Button propertyOwnerBtn;

    @FXML
    private Button customerBtn;

    private final BooleanProperty hasSelectedCustomer = new SimpleBooleanProperty();
    private final BooleanProperty hasSelectedPropertyOwner = new SimpleBooleanProperty();

    public BooleanProperty hasSelectedCustomerProperty() {
        return hasSelectedCustomer;
    }

    public final boolean isCustomerSelected() {
        return hasSelectedCustomerProperty().get();
    }

    public final void setCustomerSelected(boolean selected) {
        hasSelectedCustomerProperty().set(selected);
    }

    public BooleanProperty hasSelectedPropertyOwnerProperty() {
        return hasSelectedPropertyOwner;
    }

    public final boolean isPropertyOwnerSelected() {
        return hasSelectedPropertyOwnerProperty().get();
    }

    public final void setPropertyOwnerSelected(boolean selected) {
        hasSelectedPropertyOwnerProperty().set(selected);
    }

    @FXML
    void goToCustomer(ActionEvent event) throws IOException {
        setCustomerSelected(true);
    }

    @FXML
    void goToPropertyOwner(ActionEvent event) throws IOException {
        setPropertyOwnerSelected(true);
    }
}
