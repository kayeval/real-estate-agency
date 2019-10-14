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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.EditSalePropertyController;
import main.model.DBConnector;
import main.model.Property.Property;
import main.model.Property.PropertyType;
import main.model.Property.RentalProperty;
import main.model.User.Customer.Customer;
import main.model.User.Customer.Renter;
import main.model.User.User;

import java.io.IOException;

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
    private PropertyController propertyController;
    private ObservableList<Property> data;
    private FilteredList<Property> filteredList;
    private DBConnector dbConnector = new DBConnector();
    private ContextMenu cm;
    private String type;
    private Property selectedProperty;

    @FXML
    void initialize() {
        //TODO DIFFERENT MAIN VIEW COMPONENTS FOR DIFFERENT USERS
        userController = new UserController();
        propertyController = new PropertyController();
        propertyController.setUserController(userController);

        dbConnector = new DBConnector();
        cm = new ContextMenu();
        cm.setAutoHide(true);

//        ChangeListener<Object> listener = (obs, oldValue, newValue) -> {
//            if (newValue instanceof Property) {
//                System.out.println(propertyTableView.getSelectionModel());
//                System.out.println(propertyTableView.getSelectionModel().getTableView());
//                System.out.println(propertyTableView.getSelectionModel().getSelectedItem().getPropertyID());
//            }
//        };

//        propertyTableView.focusedProperty().addListener(listener);
//        propertyTableView.getSelectionModel().selectedItemProperty().addListener(listener);
    }

    public void refreshTable() {
        switch (type) {
            case "buyer":
                data = FXCollections.observableArrayList(propertyController.getSales().values());
                break;
            case "renter":
                data = FXCollections.observableArrayList(propertyController.getRentals().values());
                break;
            case "vendor":
            case "landlord":
                data = FXCollections.observableArrayList(propertyController.getProperties(type, userID).values());
                break;
            case "propertymanager":
            case "salesconsultant":
                data = FXCollections.observableArrayList(propertyController.getAssignedProperties(userID).values());
                break;
            default:
                data = FXCollections.observableArrayList(propertyController.getProperties().values());
                break;
        }

//        idField.setCellValueFactory(new PropertyValueFactory<Property, String>("propertyID"));
        typeField.setCellValueFactory(new PropertyValueFactory<Property, PropertyType>("propertyType"));
        addressField.setCellValueFactory(new PropertyValueFactory<Property, String>("address"));
        suburbField.setCellValueFactory(new PropertyValueFactory<Property, String>("suburb"));
        bedsField.setCellValueFactory(new PropertyValueFactory<Property, Integer>("beds"));
        bathsField.setCellValueFactory(new PropertyValueFactory<Property, Integer>("baths"));
        carsField.setCellValueFactory(new PropertyValueFactory<Property, Integer>("cars"));
        priceField.setCellValueFactory(new PropertyValueFactory<Property, Double>("price"));

        propertyTableView.setItems(data);
        propertyTableView.setContextMenu(cm);
    }


    @FXML
    void addListing(ActionEvent event) throws IOException {

        Stage addListingStage = new Stage();
        // Load root layout from fxml file
        FXMLLoader loader = new FXMLLoader();
        if (type.equals("vendor"))
            loader.setLocation(getClass().getResource("/main/view/AddSaleProperty.fxml"));
        else loader.setLocation(getClass().getResource("/main/view/AddRentalProperty.fxml"));
        AnchorPane rootLayout = loader.load();
        if (type.equals("vendor")) {
            AddSalePropertyController addSalePropertyController = loader.getController();

            addSalePropertyController.hasSavedProperty().addListener((obs, wasSaved, isSaved) -> {
                if (isSaved) {
                    propertyController.addProperty(addSalePropertyController.getAddress(), addSalePropertyController.getSuburb(), addSalePropertyController.getPropertyType(),
                            addSalePropertyController.getBaths(), addSalePropertyController.getCars(), addSalePropertyController.getBeds(), addSalePropertyController.getPrice(),
                            null, userID);
                    refreshTable();
                    addListingStage.hide();
                }
            });
        } else {
            AddRentalPropertyController addRentalPropertyController = loader.getController();

            addRentalPropertyController.hasSavedProperty().addListener((obs, wasSaved, isSaved) -> {
                if (isSaved) {
                    propertyController.addProperty(addRentalPropertyController.getAddress(), addRentalPropertyController.getSuburb(), addRentalPropertyController.getPropertyType(),
                            addRentalPropertyController.getBaths(), addRentalPropertyController.getCars(), addRentalPropertyController.getBeds(), addRentalPropertyController.getPrice(),
                            addRentalPropertyController.getContractDurations(), userID);
                    refreshTable();
                    addListingStage.hide();
                }
            });
        }
        // Show the scene containing the root layout
        Scene scene = new Scene(rootLayout);
        addListingStage.setScene(scene);
        addListingStage.show();
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
        //TODO
    }

    @FXML
    void updateUserDetails(ActionEvent event) throws IOException {
        if (type.equals("buyer") || type.equals("renter")) {
            Stage editUserStage = new Stage();
            editUserStage.setTitle("Edit User Details");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/main/view/EditCustomer.fxml"));
            AnchorPane rootLayout = loader.load();

            EditCustomerController editCustomerController = loader.getController();
            editCustomerController.hasSavedProperty().addListener((obs, wasSaved, isSaved) -> {
                if (isSaved) {
                    editUserStage.hide();
                }
            });
            editCustomerController.setUserController(userController);
            User u = null;

            if (type.equals("renter")) {
                u = userController.getCustomers().get("renter" + userID);
                editCustomerController.setBuyer(false);
                editCustomerController.setIncome(((Renter) u).getIncome());
                editCustomerController.setOccupation(((Renter) u).getOccupation());
            } else {
                u = userController.getCustomers().get("buyer" + userID);
                editCustomerController.setBuyer(true);
            }
            editCustomerController.setUsername(u.getUsername());
            editCustomerController.setEmail(u.getEmail());
            editCustomerController.setSuburbs(String.join(",", ((Customer) u).getPreferredSuburbs()));

            editCustomerController.start();

            Scene scene = new Scene(rootLayout);
            editUserStage.setScene(scene);
            editUserStage.show();
        } else {
            Stage editUserStage = new Stage();
            editUserStage.setTitle("Edit User Details");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/main/view/EditUser.fxml"));
            AnchorPane rootLayout = loader.load();

            EditUserController editUserController = loader.getController();
            editUserController.hasSavedProperty().addListener((obs, wasSaved, isSaved) -> {
                if (isSaved) {
                    editUserStage.hide();
                }
            });
            editUserController.setUserController(userController);
            User u = null;

            if (userController.getEmployees().containsKey(type + userID))
                u = userController.getEmployees().get(type + userID);
            else u = userController.getPropertyOwners().get(type + userID);
            editUserController.setUsername(u.getUsername());
            editUserController.setEmail(u.getEmail());

            editUserController.start();
            Scene scene = new Scene(rootLayout);
            editUserStage.setScene(scene);
            editUserStage.show();
        }
    }

    public void setType(String type) {
        this.type = type;
        hideComponents(type);
    }


    public void showContextMenu(TableView tv, MouseEvent e) {
        cm.show(tv, e.getScreenX(), e.getScreenY());
    }

    public void hideComponents(String type) {
        switch (type) {
            case "guest": {
                updateDetailsItem.setVisible(false);
                logoutItem.setText("Login");
                btnAddListing.setVisible(false);
                break;
            }
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

                item1.setOnAction(this::updateProperty);
                break;
            }
            case "salesconsulant": {
                //change table to add property owner column?
                btnAddListing.setVisible(false);

                MenuItem item1 = new MenuItem("Schedule inspection");
                MenuItem item2 = new MenuItem("View offers");

                cm.getItems().add(item1);
                cm.getItems().add(item2);

                item1.setOnAction(this::scheduleInspection);
                item2.setOnAction(this::viewProposals);
                break;
            }
            case "propertymanager": {
                btnAddListing.setVisible(false);

                MenuItem item1 = new MenuItem("Schedule inspection");
                MenuItem item2 = new MenuItem("View applications");

                cm.getItems().add(item1);
                cm.getItems().add(item2);

                item1.setOnAction(this::scheduleInspection);
                item2.setOnAction(this::viewProposals);
                break;
            }
            case "admin":
                //change table to see employee details?
                btnAddListing.setVisible(false);
                break;
            case "manager": {
                btnAddListing.setVisible(false);

                MenuItem item1 = new MenuItem("Assign to employee");
                cm.getItems().add(item1);

                item1.setOnAction(this::assignProperty);
                break;
            }
        }
        refreshTable();
    }

    private void assignProperty(ActionEvent event) {
        Property property = propertyTableView.getSelectionModel().getTableView().getSelectionModel().getSelectedItem();
        System.out.println(property.getPropertyID());
        Stage assignPropertyStage = new Stage();
        assignPropertyStage.setTitle("Assign Property");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/main/view/AssignProperty.fxml"));
        AnchorPane rootLayout = null;
        try {
            rootLayout = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        AssignPropertyController assignPropertyController = loader.getController();
        assignPropertyController.setUserController(userController);

        User currentAssignment = userController.currentlyAssignedToProperty(property);

        if (currentAssignment == null)
            assignPropertyController.getLblCurrentAssign().setText("Currently assigned: NONE");
        else
            assignPropertyController.getLblCurrentAssign().setText("Currently assigned: " + currentAssignment.getUsername());

        assignPropertyController.hasConfirmedProperty().addListener((obs, wasConfirmed, isConfirmed) -> {
            if (isConfirmed) {
                assignPropertyStage.hide();
                propertyController.assignProperty(property.getPropertyID(), assignPropertyController.getSelectedUser().getUserID());
            }
        });
        Scene scene = new Scene(rootLayout);
        assignPropertyStage.setScene(scene);
        assignPropertyStage.show();
    }

    private void viewProposals(ActionEvent event) {

    }

    private void scheduleInspection(ActionEvent event) {

    }

    private void updateProperty(ActionEvent event) {
        Stage editPropertyStage = new Stage();
        editPropertyStage.setTitle("Edit Property Details");
        FXMLLoader loader = new FXMLLoader();
        if (type.equals("landlord")) {
            loader.setLocation(getClass().getResource("/main/view/EditRentalProperty.fxml"));
            AnchorPane rootLayout = null;
            try {
                rootLayout = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            EditRentalPropertyController controller = loader.getController();
            controller.hasSavedProperty().addListener((obs, wasSaved, isSaved) -> {
                if (isSaved) {
                    editPropertyStage.hide();
                }
            });
            controller.setUserController(userController);
            controller.setPropertyController(propertyController);

            Property p = propertyController.getProperties().get(propertyTableView.getSelectionModel().getSelectedItem().getPropertyID());
            controller.setPropertyID(Integer.parseInt(p.getPropertyID().replaceAll("[^\\d.]", "")));
            controller.setAddress(p.getAddress());
            controller.setSuburb(p.getSuburb());
            controller.setBaths(p.getBaths());
            controller.setBeds(p.getBeds());
            controller.setCars(p.getCars());
            controller.setPrice(p.getPrice());
            controller.setPropertyType(p.getPropertyType());
            controller.setUserID(userID);
            controller.setContractDurations(((RentalProperty) p).getAcceptedDurations());
            controller.setUsername(userController.getUsername(userID));

            controller.start();

            Scene scene = new Scene(rootLayout);
            editPropertyStage.setScene(scene);
            editPropertyStage.show();

        } else {
            loader.setLocation(getClass().getResource("/main/view/EditSaleProperty.fxml"));
            AnchorPane rootLayout = null;
            try {
                rootLayout = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            EditSalePropertyController controller = loader.getController();
            controller.hasSavedProperty().addListener((obs, wasSaved, isSaved) -> {
                if (isSaved) {
                    editPropertyStage.hide();
                }
            });
            controller.setUserController(userController);
            controller.setPropertyController(propertyController);

            Property p = propertyController.getProperties().get(propertyTableView.getSelectionModel().getSelectedItem().getPropertyID());
            controller.setPropertyID(Integer.parseInt(p.getPropertyID().replaceAll("[^\\d.]", "")));
            controller.setAddress(p.getAddress());
            controller.setSuburb(p.getSuburb());
            controller.setBaths(p.getBaths());
            controller.setBeds(p.getBeds());
            controller.setCars(p.getCars());
            controller.setPrice(p.getPrice());
            controller.setPropertyType(p.getPropertyType());
            controller.setUserID(userID);
            controller.setUsername(userController.getUsername(userID));
            controller.start();

            Scene scene = new Scene(rootLayout);
            editPropertyStage.setScene(scene);
            editPropertyStage.show();
        }


    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}