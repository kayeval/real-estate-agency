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
import main.model.Proposal.Proposal;
import main.model.User.Customer.Customer;
import main.model.User.User;

public class ViewOfferController {

    @FXML
    private Button acceptBtn;

    @FXML
    private TextField listedPriceField;

    @FXML
    private TextField proposedPriceField;

    @FXML
    private Button editOrSaveBtn;

    @FXML
    private TextField applicantField;

    @FXML
    private Button rejectBtn;

    @FXML
    private Label error;

    private final BooleanProperty isAccepted = new SimpleBooleanProperty();

    public BooleanProperty isAcceptedProperty() {
        return isAccepted;
    }

    public final boolean isAccepted() {
        return isAcceptedProperty().get();
    }

    public final void setAccepted(boolean accepted) {
        isAcceptedProperty().set(accepted);
    }

    private final BooleanProperty isRejected = new SimpleBooleanProperty();

    public BooleanProperty isRejectedProperty() {
        return isRejected;
    }

    public final boolean isRejected() {
        return isRejectedProperty().get();
    }

    public final void setRejected(boolean rejected) {
        isRejectedProperty().set(rejected);
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

    private Property property;
    private User user;
    private Proposal p;

    public void setProperty(Property property) {
        this.property = property;
        listedPriceField.setText("" + property.getPrice());
    }

    public void setUser(User user) {
        this.user = user;
        applicantField.setText(user.getUsername());

        if (user instanceof Customer) {
            acceptBtn.setVisible(false);
            rejectBtn.setVisible(false);
        } else {
            editOrSaveBtn.setVisible(false);
            applicantField.setDisable(false);
            applicantField.setEditable(false);
            listedPriceField.setDisable(false);
            listedPriceField.setEditable(false);
            proposedPriceField.setDisable(false);
            proposedPriceField.setEditable(false);
        }
    }

    @FXML
    void initialize() {
        //disable
        proposedPriceField.setDisable(true);
        applicantField.setDisable(true);
        listedPriceField.setDisable(true);

        proposedPriceField.textProperty().addListener((observable) -> {
            error.setText("");
        });

        proposedPriceField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                editOrSaveBtn.fire();
            }
        });

        TextFormatter formatter = new TextFormatter(new DecimalFilter());
        proposedPriceField.setTextFormatter(formatter);
    }

    @FXML
    void editOrSaveAction(ActionEvent event) {
        if (editOrSaveBtn.getText().equals("Edit")) {
            proposedPriceField.setDisable(false);
            editOrSaveBtn.setText("Save");
        } else {
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
    }

    public void setProposal(Proposal p) {
        this.p = p;
        proposedPriceField.setText(p.getPrice() + "");
    }

    @FXML
    void rejectAction(ActionEvent event) {
        setRejected(true);
    }

    @FXML
    void acceptAction(ActionEvent event) {
        setAccepted(true);
    }

    public Button getAcceptBtn() {
        return acceptBtn;
    }

    public Button getRejectBtn() {
        return rejectBtn;
    }

    public Button getEditOrSaveBtn() {
        return editOrSaveBtn;
    }

    public Double getProposedPrice() {
        return Double.parseDouble(proposedPriceField.getText());
    }
}
