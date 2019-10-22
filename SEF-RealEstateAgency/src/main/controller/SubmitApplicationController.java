package main.controller;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Popup;
import main.model.DecimalFilter;
import main.model.Property.Property;
import main.model.Proposal.ContractDuration;
import main.model.User.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class SubmitApplicationController {
    @FXML
    private TextField listedPriceField;

    @FXML
    private Button addApplicantBtn;

    @FXML
    private RadioButton radio2Yrs;

    @FXML
    private TextField proposedPriceField;

    @FXML
    private TextField applicantField;

    @FXML
    private RadioButton radio6Mos;

    @FXML
    private Label error;

    @FXML
    private RadioButton radio1Yr;

    @FXML
    private Button saveBtn;

    private Property property;
    private User user;
    private Collection<User> renters;
    private final ObservableList data = FXCollections.observableArrayList();
    private ListView<User> selected = new ListView<>();
    private final Popup popup = new Popup();
    private final ListView listView = new ListView();

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

//        if (((RentalProperty) property).getAcceptedDurations().contains(ContractDuration.SIX_MONTHS))
//            radio6Mos.setDisable(false);
//        if (((RentalProperty) property).getAcceptedDurations().contains(ContractDuration.ONE_YEAR))
//            radio1Yr.setDisable(false);
//        if (((RentalProperty) property).getAcceptedDurations().contains(ContractDuration.TWO_YEARS))
//            radio2Yrs.setDisable(false);
    }

    public void setUser(User user) {
        this.user = user;
        applicantField.setText(user.getUsername());
    }

    public void setRenters(Collection<User> renters) {
        this.renters = renters;
        listView.setPrefSize(200, 250);
        listView.setEditable(true);

        renters.remove(user);
        if (renters.size() > 0) {
            data.addAll(renters);
            listView.setItems(data);
            listView.setCellFactory(param -> new ListCell<User>() {
                @Override
                protected void updateItem(User item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null || item.getUsername() == null) {
                        setText(null);
                    } else {
                        setText(item.getUsername());
                    }
                }
            });
            listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            listView.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
                selected.setItems(listView.getSelectionModel().getSelectedItems());
            });

            popup.getContent().add(listView);
        } else addApplicantBtn.setDisable(true);
    }

    @FXML
    void initialize() {
        listedPriceField.setDisable(true);
        applicantField.setEditable(false);

        final ToggleGroup group = new ToggleGroup();
        radio6Mos.setToggleGroup(group);
        radio1Yr.setToggleGroup(group);
        radio2Yrs.setToggleGroup(group);

//        radio6Mos.setDisable(true);
//        radio1Yr.setDisable(true);
//        radio2Yrs.setDisable(true);

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
    void addApplicantAction(ActionEvent event) {
        if (popup.isShowing()) {
            popup.hide();
            applicantsSelected();
        } else popup.show(((Node) event.getSource()).getScene().getWindow());
    }

    private void applicantsSelected() {
        //append to applicant field
        applicantField.setText(user.getUsername());
        for (User u : selected.getItems())
            applicantField.appendText(", " + u.getUsername());
    }

    @FXML
    void saveAction(ActionEvent event) {
        boolean valid = true;

        if (proposedPriceField.getText().equals("") || (!radio6Mos.isSelected() && !radio2Yrs.isSelected() && !radio1Yr.isSelected())) {
            error.setText("Please fill up all fields.");
            valid = false;
        } else if (!proposedPriceField.getText().equals("") && Double.parseDouble(proposedPriceField.getText()) < 0) {
            error.setText("Fee cannot be negative.");
            valid = false;
        }

        if (valid)
            setCanSubmit(true);
    }

    public Collection<User> getApplicants() {
        Collection<User> applicants = new ArrayList<>();

        for (String s : applicantField.getText().split(",")) {
            for (User u : renters) {
                if (u.getUsername().equals(s.trim()))
                    applicants.add(u);
            }
        }

        applicants.add(user);

        return applicants;
    }

    public Double getProposedPrice() {
        return Double.parseDouble(proposedPriceField.getText());
    }

    public ContractDuration getContractDuration() {
        ContractDuration contractDuration = null;

        if (radio1Yr.isSelected())
            contractDuration = ContractDuration.ONE_YEAR;
        if (radio2Yrs.isSelected())
            contractDuration = ContractDuration.SIX_MONTHS;
        if (radio6Mos.isSelected())
            contractDuration = ContractDuration.TWO_YEARS;

        return contractDuration;
    }
}
