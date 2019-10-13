package main.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import main.model.DBConnector;
import main.model.Property.*;
import main.model.User.PropertyOwner.PropertyOwner;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MainController {

    @FXML
    private TableView<Property> propertyTableView;

    @FXML
    private TableColumn<Property, PropertyType> typeField;

    @FXML
    private TableColumn<Property, String> addressField;

    @FXML
    private TableColumn<Property, String> suburbField;

    @FXML
    private TableColumn<Property, Integer> bedsField;

    @FXML
    private TableColumn<Property, Integer> bathsField;

    @FXML
    private TableColumn<Property, Integer> carsField;

    @FXML
    private TableColumn<Property, Double> priceField;

    @FXML
    private TextField searchField;

    @FXML
    private MenuButton userActions;

    @FXML
    private MenuItem updateDetailsItem;

    @FXML
    private MenuItem logoutItem;

    @FXML
    private Button btnAddListing;

    private UserController userController;
    private ObservableList<Property> data;
    private FilteredList<Property> filteredList;
    private DBConnector dbConnector;
    private ContextMenu cm;
    private String type;

    @FXML
    void initialize() {
        //TODO DIFFERENT MAIN VIEW COMPONENTS FOR DIFFERENT USERS

        if (type != null) {
            System.out.println("in initialize(), TYPE OF USER = " + type);
        }

        try {
            Connection newConn = dbConnector.getConnection();
            data = FXCollections.observableArrayList();

            ResultSet rs = newConn.createStatement().executeQuery("SELECT * FROM properties");
            while (rs.next()) {
                String address = rs.getString("address");
                int baths = rs.getInt("baths");
                int cars = rs.getInt("carspaces");
                int beds = rs.getInt("beds");
                String propertyType = rs.getString("propertytype");
                double price = rs.getDouble("price");
                String suburb = rs.getString("suburb");
                int propertyOwnerID = rs.getInt("listedby");
                //contract duration to set
                String contractDurations = rs.getString("contractdurations");
                Set<String> set = new HashSet<String>(Arrays.asList(contractDurations.split(",")));

                if (rs.getBoolean("rental"))
                    data.add(new RentalProperty(address, suburb, new Capacity(cars, beds, baths), PropertyType.valueOf(propertyType),
                            price, (PropertyOwner) userController.getPropertyOwners().get("landlord" + propertyOwnerID)), );
                else
                    data.add(new SaleProperty());
            }
        } catch (SQLException | DeactivatedPropertyException e) {
            e.printStackTrace();
        }

        typeField.setCellValueFactory(new PropertyValueFactory<Property, PropertyType>("propertyType"));
        addressField.setCellValueFactory(new PropertyValueFactory<Property, String>("address"));
        suburbField.setCellValueFactory(new PropertyValueFactory<Property, String>("suburb"));
        bedsField.setCellValueFactory(new PropertyValueFactory<Property, Integer>("beds"));
        bathsField.setCellValueFactory(new PropertyValueFactory<Property, Integer>("baths"));
        carsField.setCellValueFactory(new PropertyValueFactory<Property, Integer>("cars"));
        priceField.setCellValueFactory(new PropertyValueFactory<Property, Double>("price"));

        propertyTableView.setItems(data);

        cm = new ContextMenu();
    }


    @FXML
    void addListing(ActionEvent event) {

    }

    @FXML
    void logout(ActionEvent event) {

    }

    @FXML
    void searchSuburbs(ActionEvent event) {

    }

    @FXML
    void updateUserDetails(ActionEvent event) {

    }


    public void setDbConnector(DBConnector dbConnector) {
        this.dbConnector = dbConnector;
    }

    public DBConnector getDbConnector() {
        return dbConnector;
    }

    public void setType(String type) {
        this.type = type;
        hideComponents(type);
    }

    public void hideComponents(String type) {
        if (type.equals("buyer")) {
            btnAddListing.setVisible(false);
            MenuItem item1 = new MenuItem("Make offer");
            cm.getItems().add(item1);
        } else if (type.equals("renter")) {
            btnAddListing.setVisible(false);
            MenuItem item1 = new MenuItem("Send application");
            cm.getItems().add(item1);
        } else if (type.equals("vendor") || type.equals("landlord")) {
            MenuItem item1 = new MenuItem("Edit");
            MenuItem item2 = new MenuItem("Delete");

            cm.getItems().add(item1);
            cm.getItems().add(item2);
        } else if (type.equals("salesconsulant")) {
            //change table to add property owner column?
            MenuItem item1 = new MenuItem("Schedule inspection");
            MenuItem item2 = new MenuItem("View offers");

            cm.getItems().add(item1);
            cm.getItems().add(item2);

            item1.setOnAction(this::scheduleInspection);
            item2.setOnAction(this::viewProposals);
        } else if (type.equals("propertymanager")) {
            MenuItem item1 = new MenuItem("Schedule inspection");
            MenuItem item2 = new MenuItem("View applications");

            cm.getItems().add(item1);
            cm.getItems().add(item2);

            item1.setOnAction(this::scheduleInspection);
            item2.setOnAction(this::viewProposals);
        } else if (type.equals("admin")) {
            //change table to employee details?
        } else if (type.equals("manager")) {
            //change table to add property owner & assignments column?
            MenuItem item1 = new MenuItem("Assign to employee");
            cm.getItems().add(item1);

            item1.setOnAction(this::assignProperty);
        }
    }

    public void setUserController(UserController userController) {
        this.userController = userController;
    }

    private void assignProperty(ActionEvent event) {

    }

    private void viewProposals(ActionEvent event) {

    }

    private void scheduleInspection(ActionEvent event) {

    }
}