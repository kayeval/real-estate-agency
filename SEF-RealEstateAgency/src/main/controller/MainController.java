package main.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.model.DBConnector;
import main.model.Property.*;
import main.model.Proposal.ContractDuration;
import main.model.User.Customer.Customer;
import main.model.User.Customer.Renter;
import main.model.User.PropertyOwner.PropertyOwner;
import main.model.User.User;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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
    private int userID;
    private UserController userController;
    private ObservableList<Property> data;
    private FilteredList<Property> filteredList;
    private DBConnector dbConnector = new DBConnector();
    private ContextMenu cm;
    private String type;

    @FXML
    void initialize() {
        //TODO DIFFERENT MAIN VIEW COMPONENTS FOR DIFFERENT USERS
        userController = new UserController();

        if (type != null) {
            System.out.println("in initialize(), TYPE OF USER = " + type);
        }
        dbConnector = new DBConnector();
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

                //add list of contract durations to set
                Capacity capacity = new Capacity(cars, beds, baths);
                String contractDurations = rs.getString("contractdurations");
                Set<ContractDuration> contractDurationSet = new HashSet<ContractDuration>(Arrays.stream(contractDurations.split("\\s*,\\s*"))
                        .map(ContractDuration::valueOf)
                        .collect(Collectors.toList()));

                if (rs.getBoolean("rental"))
                    data.add(new RentalProperty(address, suburb, capacity, PropertyType.valueOf(propertyType),
                            price, (PropertyOwner) userController.getPropertyOwners().get("landlord" + propertyOwnerID), contractDurationSet));
                else
                    data.add(new SaleProperty(address, suburb, capacity, PropertyType.valueOf(propertyType), price,
                            (PropertyOwner) userController.getPropertyOwners().get("vendor" + propertyOwnerID)));
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
    void logout(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/main/view/Login.fxml"));
        Parent nextPane = loader.load();
        LoginController controller = loader.getController();
        controller.setUserController(new UserController());
        Scene nextScene = new Scene(nextPane);

        Stage window = (Stage) userActions.getScene().getWindow();
        window.setScene(nextScene);
        window.show();
    }

    @FXML
    void searchSuburbs(ActionEvent event) {

    }

    @FXML
    void updateUserDetails(ActionEvent event) throws IOException {
        if (type.equals("buyer") || type.equals("renter")) {
            Stage editUserStage = new Stage();
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/main/view/EditCustomer.fxml"));
            AnchorPane rootLayout = loader.load();

            EditUserController editUserController = loader.getController();
            editUserController.hasSavedProperty().addListener((obs, wasSaved, isSaved) -> {
                if (isSaved) {
                    editUserStage.hide();
                }
            });
            editUserController.setUserController(userController);
            User u = null;

            System.out.println(userController.getCustomers().size());

            if (type.equals("renter")) {
                u = userController.getCustomers().get("renter" + userID);
                editUserController.setBuyer(false);
                editUserController.setIncome(((Renter) u).getIncome());
                editUserController.setOccupation(((Renter) u).getOccupation());
            } else {
                u = userController.getCustomers().get("buyer" + userID);
                editUserController.setBuyer(true);
            }
            editUserController.setUsername(u.getUsername());
            editUserController.setEmail(u.getEmail());
            editUserController.setSuburbs(String.join(",", ((Customer) u).getPreferredSuburbs()));

            editUserController.start();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            editUserStage.setScene(scene);
            editUserStage.show();
        }
    }

    public void setType(String type) {
        this.type = type;
        hideComponents(type);
    }

    public void hideComponents(String type) {
        System.out.println(type);
        switch (type) {
            case "buyer": {
                priceField.setText("Listed Price");
                btnAddListing.setVisible(false);
                MenuItem item1 = new MenuItem("Make offer");
                cm.getItems().add(item1);
                break;
            }
            case "renter": {
                priceField.setText("Weekly Rental");
                btnAddListing.setVisible(false);
                MenuItem item1 = new MenuItem("Send application");
                cm.getItems().add(item1);
                break;
            }
            case "vendor":
            case "landlord": {
                MenuItem item1 = new MenuItem("Edit");
                MenuItem item2 = new MenuItem("Delete");

                cm.getItems().add(item1);
                cm.getItems().add(item2);
                break;
            }
            case "salesconsulant": {
                //change table to add property owner column?
                MenuItem item1 = new MenuItem("Schedule inspection");
                MenuItem item2 = new MenuItem("View offers");

                cm.getItems().add(item1);
                cm.getItems().add(item2);

                item1.setOnAction(this::scheduleInspection);
                item2.setOnAction(this::viewProposals);
                break;
            }
            case "propertymanager": {
                MenuItem item1 = new MenuItem("Schedule inspection");
                MenuItem item2 = new MenuItem("View applications");

                cm.getItems().add(item1);
                cm.getItems().add(item2);

                item1.setOnAction(this::scheduleInspection);
                item2.setOnAction(this::viewProposals);
                break;
            }
            case "admin":
                //change table to employee details?
                break;
            case "manager": {
                //change table to add property owner & assignments column?
                MenuItem item1 = new MenuItem("Assign to employee");
                cm.getItems().add(item1);

                item1.setOnAction(this::assignProperty);
                break;
            }
        }
    }

    private void assignProperty(ActionEvent event) {

    }

    private void viewProposals(ActionEvent event) {

    }

    private void scheduleInspection(ActionEvent event) {

    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}