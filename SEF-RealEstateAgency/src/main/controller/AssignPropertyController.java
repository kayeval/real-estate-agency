package main.controller;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import main.model.User.Employee.Employee;
import main.model.User.Employee.SalesPerson.SalesPerson;
import main.model.User.User;
import main.model.UserDBModel;

import java.time.LocalDate;

public class AssignPropertyController {

    @FXML
    private TableColumn<User, String> usernameField;

    @FXML
    private TableColumn<Employee, LocalDate> hireField;

    @FXML
    private TextField searchField;

    @FXML
    private Button btnAssign;

    @FXML
    private TableColumn<SalesPerson, Integer> totalField;

    @FXML
    private TableView<User> userTableView;

    @FXML
    private TableColumn<Employee, String> typeField;

    @FXML
    private Label lblCurrentAssign;

    private ObservableList<User> data;

    private boolean isRentalProperty;

    private final BooleanProperty hasConfirmed = new SimpleBooleanProperty();

    public BooleanProperty hasConfirmedProperty() {
        return hasConfirmed;
    }

    public final boolean isConfirmed() {
        return hasConfirmedProperty().get();
    }

    public final void setConfirmed(boolean saved) {
        hasConfirmedProperty().set(saved);
    }

    private int userID;
    private User selectedUser;
    private UserDBModel userDBModel;

    @FXML
    void initialize() {
        btnAssign.setDisable(true);

        ChangeListener<Object> listener = (obs, oldValue, newValue) -> {
            btnAssign.setDisable(false);
        };

        userTableView.focusedProperty().addListener(listener);
        userTableView.getSelectionModel().selectedItemProperty().addListener(listener);
//        lblCurrentAssign.setVisible(false);
    }

    public void refreshTable() {
        //data is filtered according to property passed in
        if (isRentalProperty)
            data = FXCollections.observableArrayList(userDBModel.getPropertyManagers().values());
        else data = FXCollections.observableArrayList(userDBModel.getSalesConsultants().values());

        typeField.setCellValueFactory(new PropertyValueFactory<Employee, String>("type"));
        usernameField.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
        totalField.setCellValueFactory(new PropertyValueFactory<SalesPerson, Integer>("totalAssignments"));
        hireField.setCellValueFactory(new PropertyValueFactory<Employee, LocalDate>("hireDate"));

        userTableView.setItems(data);
    }

    @FXML
    void searchUsers(ActionEvent event) {

        //TODO
    }

    @FXML
    void assignProperty(ActionEvent event) {

        if (userTableView.getSelectionModel().getSelectedItem() != null) {
            selectedUser = userTableView.getSelectionModel().getSelectedItem();
            setConfirmed(true);
        }
    }

    public void setUserDBModel(UserDBModel userDBModel) {
        this.userDBModel = userDBModel;
    }

    public User getSelectedUser() {
        return selectedUser;
    }

    public Label getLblCurrentAssign() {
        return lblCurrentAssign;
    }

    public void isRentalProperty(boolean rental) {
        isRentalProperty = rental;

        refreshTable();
    }
}
