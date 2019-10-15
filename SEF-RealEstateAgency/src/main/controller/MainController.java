package main.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.util.Duration;
import main.EditSalePropertyController;
import main.model.DBConnector;
import main.model.Inspection;
import main.model.Property.Property;
import main.model.Property.PropertyType;
import main.model.Property.RentalProperty;
import main.model.PropertyDBModel;
import main.model.Proposal.Proposal;
import main.model.User.Customer.Customer;
import main.model.User.Customer.Renter;
import main.model.User.Employee.Employee;
import main.model.User.Employee.PartTimeEmployee;
import main.model.User.User;
import main.model.UserDBModel;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class MainController {

    @FXML
    private TableView<Property> propertyTableView;

    @FXML
    private TableColumn<Property, String> boolSR;

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
    private TableColumn<Property, Double> minPriceField;

    @FXML
    private TextField propertySearchField;

    @FXML
    private ComboBox<String> propertyStateCmb;

    @FXML
    private MenuButton userActions;

    @FXML
    private MenuItem updateDetailsItem;

    @FXML
    private MenuItem logoutItem;

    @FXML
    private TableColumn<Proposal, Boolean> waitingForPaymentField;

    @FXML
    private TableColumn<Inspection, String> inspectionPropertyField;

    @FXML
    private TableColumn<Proposal, String> proposalPropertyField;

    @FXML
    private Label lblCurDate;

    @FXML
    private ComboBox<String> inspectionStateCmb;

    @FXML
    private Button btnAddListing;

    @FXML
    private TableColumn<Employee, String> emplStatusField;

    @FXML
    private Tab proposalTab;

    @FXML
    private Button runPayrollBtn;

    @FXML
    private Tab inspectionTab;

    @FXML
    private TableColumn<Employee, String> emplUsernameField;

    @FXML
    private TableView<Inspection> inspectionTableView;

    @FXML
    private TableColumn<Property, String> propertyStatusField;

    @FXML
    private ComboBox<String> proposalStateCmb;

    @FXML
    private TableView<Proposal> proposalTableView;

    @FXML
    private TableView<Employee> employeeTableView;

    @FXML
    private TableColumn<Inspection, LocalDate> inspectionDateField;

    @FXML
    private Tab propertyTab;

    @FXML
    private TableColumn<Inspection, String> inspectionStatusField;

    @FXML
    private TableColumn<Proposal, String> proposalStatusField;

    @FXML
    private TableColumn<Inspection, LocalTime> inspectionTimeField;

    @FXML
    private Button btnScheduleInspection;

    @FXML
    private Button notifBtn;

    @FXML
    private TableColumn<Proposal, LocalDate> proposalDateField;

    @FXML
    private TableColumn<Proposal, Double> offerPriceField;

    @FXML
    private TableColumn<Employee, Double> emplSalaryField;

    @FXML
    private ComboBox<String> employeeStateCmb;

    @FXML
    private TableColumn<Employee, LocalDate> emplHireDateField;

    @FXML
    private Tab employeeTab;

    @FXML
    private TableColumn<Employee, String> emplTypeField;

    @FXML
    private TableColumn<PartTimeEmployee, LocalDate> parttimeDateField;

    @FXML
    private TableColumn<PartTimeEmployee, Double> parttimeHoursField;

    @FXML
    private TableView<PartTimeEmployee> workingHoursTableView;

    @FXML
    private Tab workingHoursTab;

    @FXML
    private TableColumn<PartTimeEmployee, String> parttimeStatusField;

    @FXML
    private ComboBox<String> workingHoursStateCmb;

    @FXML
    private Button addWorkingHoursBtn;

    @FXML
    private TabPane tabPane;

    private int userID;
    private String registeredUserType;

    private DBConnector dbConnector = new DBConnector();

    private UserDBModel userDBModel;
    private PropertyDBModel propertyDBModel;

    private ObservableList<Property> properties;
    private ObservableList<Inspection> inspections;
    private ObservableList<User> employees;
    private ObservableList<Proposal> proposals;

    private FilteredList<Property> filteredList;

    private ContextMenu propertyContextMenu, inspectionContextMenu, proposalContextMenu, employeeContextMenu;

    private Property selectedProperty;
    private Inspection selectedInspection;
    private Proposal selectedProposal;
    private User selectedEmployee;

    @FXML
    void initialize() {
        tabPane.getTabs().remove(workingHoursTab);

        initClock();

        //TODO set graphic for notification button


        //setup controllers
        userDBModel = new UserDBModel();
        propertyDBModel = new PropertyDBModel();
        propertyDBModel.setUserDBModel(userDBModel);

        dbConnector = new DBConnector();

        //setup context menus
        propertyContextMenu = new ContextMenu();
        propertyContextMenu.setAutoHide(true);

        employeeContextMenu = new ContextMenu();
        employeeContextMenu.setAutoHide(true);

        inspectionContextMenu = new ContextMenu();
        inspectionContextMenu.setAutoHide(true);

        proposalContextMenu = new ContextMenu();
        proposalContextMenu.setAutoHide(true);

        //setup state combo boxes
        propertyStateCmb.getItems().addAll("All", "Active", "Pending", "Inactive");
        propertyStateCmb.getSelectionModel().selectFirst();
        propertyStateCmb.getSelectionModel().selectedIndexProperty().addListener((options, oldValue, newValue) -> {
            switch (propertyStateCmb.getValue()) {
                case "Active":
                    refreshPropertyTable("active");
                    break;
                case "Pending":
                    refreshPropertyTable("pending");
                    break;
                case "Inactive":
                    refreshPropertyTable("inactive");
                    break;
                default:
                    refreshPropertyTable("all");
                    break;
            }
        });

        employeeStateCmb.getItems().addAll("All", "Part-time", "Full-time");
        employeeStateCmb.getSelectionModel().selectFirst();
        employeeStateCmb.getSelectionModel().selectedIndexProperty().addListener((options, oldValue, newValue) -> {
            switch (employeeStateCmb.getValue()) {
                case "Part-time":
                    refreshEmployeeTable("part-time");
                    break;
                case "Full-time":
                    refreshEmployeeTable("full-time");
                    break;
                default:
                    refreshEmployeeTable("all");
                    break;
            }
        });

        proposalStateCmb.getItems().addAll("All", "Accepted", "Pending", "Rejected");
        proposalStateCmb.getSelectionModel().selectFirst();
        proposalStateCmb.getSelectionModel().selectedIndexProperty().addListener((options, oldValue, newValue) -> {
            switch (proposalStateCmb.getValue()) {
                case "Accepted":
                    refreshProposalTable("accepted");
                    break;
                case "Pending":
                    refreshProposalTable("pending");
                    break;
                case "Rejected":
                    refreshProposalTable("rejected");
                    break;
                default:
                    refreshProposalTable("all");
                    break;
            }
        });

        inspectionStateCmb.getItems().addAll("All", "Scheduled", "Completed", "Cancelled");
        inspectionStateCmb.getSelectionModel().selectFirst();
        inspectionStateCmb.getSelectionModel().selectedIndexProperty().addListener((options, oldValue, newValue) -> {
            switch (inspectionStateCmb.getValue()) {
                case "Scheduled":
                    refreshInspectionTable("scheduled");
                    break;
                case "Completed":
                    refreshInspectionTable("completed");
                    break;
                case "Cancelled":
                    refreshInspectionTable("cancelled");
                    break;
                default:
                    refreshInspectionTable("all");
                    break;
            }
        });
    }

    public void refreshEmployeeTable(String state) {
        switch (state) {
            case "part-time": {

                break;
            }
            case "full-time": {

                break;
            }
            default:
                employees = FXCollections.observableArrayList(userDBModel.getEmployees().values());
        }
        properties = FXCollections.observableArrayList(propertyDBModel.getActiveProperties().values());

        boolSR.setCellValueFactory(new PropertyValueFactory<Property, String>("strippedPropertyID"));
        typeField.setCellValueFactory(new PropertyValueFactory<Property, PropertyType>("propertyType"));
        addressField.setCellValueFactory(new PropertyValueFactory<Property, String>("address"));
        suburbField.setCellValueFactory(new PropertyValueFactory<Property, String>("suburb"));
        bedsField.setCellValueFactory(new PropertyValueFactory<Property, Integer>("beds"));
        bathsField.setCellValueFactory(new PropertyValueFactory<Property, Integer>("baths"));
        carsField.setCellValueFactory(new PropertyValueFactory<Property, Integer>("cars"));
        minPriceField.setCellValueFactory(new PropertyValueFactory<Property, Double>("price"));
        propertyStatusField.setCellValueFactory(new PropertyValueFactory<Property, String>("status"));

        propertyTableView.setItems(properties);
        propertyTableView.setContextMenu(propertyContextMenu);
    }

    public void refreshProposalTable(String state) {

    }

    public void refreshInspectionTable(String state) {

    }

    public void refreshPropertyTable(String state) {
        switch (registeredUserType) {
            case "buyer":
                properties = FXCollections.observableArrayList(propertyDBModel.getActiveSales().values());
                break;
            case "renter":
                properties = FXCollections.observableArrayList(propertyDBModel.getActiveRentals().values());
                break;
            case "vendor":
            case "landlord":
                switch (state) {
                    case "all":
                        properties = FXCollections.observableArrayList(propertyDBModel.getOwnProperties(registeredUserType, userID).values());
                        break;
                    case "inactive":
                        properties = FXCollections.observableArrayList(propertyDBModel.getOwnInactiveProperties(registeredUserType, userID).values());
                        break;
                    case "pending":
                        properties = FXCollections.observableArrayList(propertyDBModel.getOwnPendingProperties(registeredUserType, userID).values());
                        break;
                    case "active":
                        properties = FXCollections.observableArrayList(propertyDBModel.getOwnActiveProperties(registeredUserType, userID).values());
                        break;
                }
                break;
            case "propertymanager":
            case "salesconsultant":
                switch (state) {
                    case "all":
                        properties = FXCollections.observableArrayList(propertyDBModel.getAssignedProperties(userID).values());
                        break;
                    case "inactive":
                        properties = FXCollections.observableArrayList(propertyDBModel.getAssignedInactiveProperties(userID).values());
                        break;
                    case "pending":
                        properties = FXCollections.observableArrayList(propertyDBModel.getAssignedPendingProperties(userID).values());
                        break;
                    case "active":
                        properties = FXCollections.observableArrayList(propertyDBModel.getAssignedActiveProperties(userID).values());
                        break;
                }
                break;
            default:
                switch (state) {
                    case "all":
                        properties = FXCollections.observableArrayList(propertyDBModel.getProperties().values());
                        break;
                    case "inactive":
                        properties = FXCollections.observableArrayList(propertyDBModel.getInactiveProperties().values());
                        break;
                    case "pending":
                        properties = FXCollections.observableArrayList(propertyDBModel.getPendingProperties().values());
                        break;
                    case "active":
                        properties = FXCollections.observableArrayList(propertyDBModel.getActiveProperties().values());
                        break;
                }
                break;
        }

        boolSR.setCellValueFactory(new PropertyValueFactory<Property, String>("strippedPropertyID"));
        typeField.setCellValueFactory(new PropertyValueFactory<Property, PropertyType>("propertyType"));
        addressField.setCellValueFactory(new PropertyValueFactory<Property, String>("address"));
        suburbField.setCellValueFactory(new PropertyValueFactory<Property, String>("suburb"));
        bedsField.setCellValueFactory(new PropertyValueFactory<Property, Integer>("beds"));
        bathsField.setCellValueFactory(new PropertyValueFactory<Property, Integer>("baths"));
        carsField.setCellValueFactory(new PropertyValueFactory<Property, Integer>("cars"));
        minPriceField.setCellValueFactory(new PropertyValueFactory<Property, Double>("price"));
        propertyStatusField.setCellValueFactory(new PropertyValueFactory<Property, String>("status"));

        propertyTableView.setItems(properties);
        propertyTableView.setContextMenu(propertyContextMenu);
    }

    @FXML
    void addListing(ActionEvent event) throws IOException {

        Stage addListingStage = new Stage();
        // Load root layout from fxml file
        FXMLLoader loader = new FXMLLoader();
        if (registeredUserType.equals("vendor"))
            loader.setLocation(getClass().getResource("/main/view/AddSaleProperty.fxml"));
        else loader.setLocation(getClass().getResource("/main/view/AddRentalProperty.fxml"));
        AnchorPane rootLayout = loader.load();
        if (registeredUserType.equals("vendor")) {
            AddSalePropertyController addSalePropertyController = loader.getController();

            addSalePropertyController.hasSavedProperty().addListener((obs, wasSaved, isSaved) -> {
                if (isSaved) {
                    propertyDBModel.addProperty(addSalePropertyController.getAddress(), addSalePropertyController.getSuburb(), addSalePropertyController.getPropertyType(),
                            addSalePropertyController.getBaths(), addSalePropertyController.getCars(), addSalePropertyController.getBeds(), addSalePropertyController.getPrice(),
                            null, userID);
                    refreshPropertyTable(propertyStateCmb.getValue().toLowerCase());
                    addListingStage.hide();
                }
            });
        } else {
            AddRentalPropertyController addRentalPropertyController = loader.getController();

            addRentalPropertyController.hasSavedProperty().addListener((obs, wasSaved, isSaved) -> {
                if (isSaved) {
                    propertyDBModel.addProperty(addRentalPropertyController.getAddress(), addRentalPropertyController.getSuburb(), addRentalPropertyController.getPropertyType(),
                            addRentalPropertyController.getBaths(), addRentalPropertyController.getCars(), addRentalPropertyController.getBeds(), addRentalPropertyController.getPrice(),
                            addRentalPropertyController.getContractDurations(), userID);
                    refreshPropertyTable(propertyStateCmb.getValue().toLowerCase());
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
        controller.setUserDBModel(new UserDBModel());
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
        if (registeredUserType.equals("buyer") || registeredUserType.equals("renter")) {
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
            editCustomerController.setUserDBModel(userDBModel);
            User u = null;

            if (registeredUserType.equals("renter")) {
                u = userDBModel.getCustomers().get("renter" + userID);
                editCustomerController.setBuyer(false);
                editCustomerController.setIncome(((Renter) u).getIncome());
                editCustomerController.setOccupation(((Renter) u).getOccupation());
            } else {
                u = userDBModel.getCustomers().get("buyer" + userID);
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
            editUserController.setUserDBModel(userDBModel);
            User u = null;

            if (userDBModel.getEmployees().containsKey(registeredUserType + userID))
                u = userDBModel.getEmployees().get(registeredUserType + userID);
            else u = userDBModel.getPropertyOwners().get(registeredUserType + userID);
            editUserController.setUsername(u.getUsername());
            editUserController.setEmail(u.getEmail());

            editUserController.start();
            Scene scene = new Scene(rootLayout);
            editUserStage.setScene(scene);
            editUserStage.show();
        }
    }

    public void setRegisteredUserType(String registeredUserType) {
        this.registeredUserType = registeredUserType;

        hideComponents(registeredUserType);
    }

    public void hideComponents(String type) {
        switch (type) {
            case "buyer": {
                //set placeholder text
                propertyTableView.setPlaceholder(new Label("There are no active property listings."));
                proposalTableView.setPlaceholder(new Label("You have not submitted any offers yet."));
                inspectionTableView.setPlaceholder(new Label("You have no scheduled inspections."));

                minPriceField.setText("Listed Price");

                btnAddListing.setVisible(false);
                btnScheduleInspection.setVisible(false);

                propertyStateCmb.setVisible(false);

                MenuItem item1 = new MenuItem("Send offer");
                item1.setOnAction(this::submitProposal);

                propertyContextMenu.getItems().add(item1);

                MenuItem viewProposal = new MenuItem("View offer");
                viewProposal.setOnAction(this::viewProposal);

                proposalContextMenu.getItems().add(viewProposal);

                MenuItem item2 = new MenuItem("Withdraw offer");
                item2.setOnAction(this::withdrawProposal);

                proposalContextMenu.getItems().add(item2);

                MenuItem item3 = new MenuItem("Cancel inspection");
                item3.setOnAction(this::cancelInspection);

                inspectionContextMenu.getItems().add(item3);

                tabPane.getTabs().remove(employeeTab);
                btnScheduleInspection.setVisible(false);
                break;
            } //buyer

            case "renter": {
                //set placeholder text
                propertyTableView.setPlaceholder(new Label("There are no active property listings."));
                proposalTableView.setPlaceholder(new Label("You have not submitted any applications yet."));
                inspectionTableView.setPlaceholder(new Label("You have no scheduled inspections."));

                minPriceField.setText("Weekly Rental");

                btnAddListing.setVisible(false);
                btnScheduleInspection.setVisible(false);

                propertyStateCmb.setVisible(false);

                MenuItem item1 = new MenuItem("Send application");
                item1.setOnAction(this::submitProposal);
                propertyContextMenu.getItems().add(item1);

                MenuItem viewProposal = new MenuItem("View application");
                viewProposal.setOnAction(this::viewProposal);
                proposalContextMenu.getItems().add(viewProposal);

                MenuItem item2 = new MenuItem("Withdraw application");
                item2.setOnAction(this::withdrawProposal);
                proposalContextMenu.getItems().add(item2);

                MenuItem item3 = new MenuItem("Cancel inspection");
                item3.setOnAction(this::cancelInspection);
                inspectionContextMenu.getItems().add(item3);

                tabPane.getTabs().remove(employeeTab);
                btnScheduleInspection.setVisible(false);
                break;
            } //renter

            case "vendor": {
                //set placeholder text
                propertyTableView.setPlaceholder(new Label("You have not added any property listings."));
                proposalTableView.setPlaceholder(new Label("You have not received any offers yet."));
                inspectionTableView.setPlaceholder(new Label("You have no scheduled inspections."));

                btnScheduleInspection.setVisible(false);

                MenuItem item1 = new MenuItem("Edit");
                item1.setOnAction(this::updateProperty);

                MenuItem item2 = new MenuItem("Deactivate");
                item2.setOnAction(this::deactivateProperty);

                MenuItem item3 = new MenuItem("View offer");
                item3.setOnAction(this::viewProposal);

                propertyContextMenu.getItems().add(item1);
                propertyContextMenu.getItems().add(item2);
                proposalContextMenu.getItems().add(item3);

                tabPane.getTabs().remove(employeeTab);
                break;
            }
            case "landlord": {
                propertyTableView.setPlaceholder(new Label("You have not added any property listings."));
                proposalTableView.setPlaceholder(new Label("You have not received any applications yet."));
                inspectionTableView.setPlaceholder(new Label("You have no scheduled inspections."));

                btnScheduleInspection.setVisible(false);

                MenuItem item1 = new MenuItem("Edit");
                item1.setOnAction(this::updateProperty);

                MenuItem item2 = new MenuItem("Deactivate");
                item2.setOnAction(this::deactivateProperty);

                MenuItem item3 = new MenuItem("View application");
                item3.setOnAction(this::viewProposal);

                propertyContextMenu.getItems().add(item1);
                propertyContextMenu.getItems().add(item2);
                proposalContextMenu.getItems().add(item3);

                tabPane.getTabs().remove(employeeTab);
                break;
            } //vendor

            case "salesconsultant": {
                propertyTableView.setPlaceholder(new Label("You have not been assigned any properties."));
                proposalTableView.setPlaceholder(new Label("There are no applications for any of your assigned properties."));
                inspectionTableView.setPlaceholder(new Label("You have not scheduled any inspections yet."));

                btnAddListing.setVisible(false);
                minPriceField.setText("Weekly Rental");

                MenuItem item1 = new MenuItem("Advertise property");
//                item1.setOnAction(this::advertiseListing);

                MenuItem item2 = new MenuItem("Organize legal documents");
//                item2.setOnAction(this::organizeDocuments);

                MenuItem item3 = new MenuItem("Contact property owner");
                item3.setOnAction(this::contactPropertyOwner);

                MenuItem item4 = new MenuItem("View commission");
                item4.setOnAction(this::viewCommissionManagementFee);

                propertyContextMenu.getItems().add(item1);
                propertyContextMenu.getItems().add(item2);
                propertyContextMenu.getItems().add(item3);
                propertyContextMenu.getItems().add(item4);

                MenuItem contact = new MenuItem("Contact renter");
                contact.setOnAction(this::contactCustomer);
                proposalContextMenu.getItems().add(contact);

                MenuItem resched = new MenuItem("Reschedule");
                resched.setOnAction(this::rescheduleInspection);

                MenuItem sched = new MenuItem("Cancel");
                sched.setOnAction(this::cancelInspection);

                inspectionContextMenu.getItems().add(resched);
                inspectionContextMenu.getItems().add(sched);

                tabPane.getTabs().remove(employeeTab);
                break;
            } //salesconsultant

            case "propertymanager": {
                propertyTableView.setPlaceholder(new Label("You have not been assigned any properties."));
                proposalTableView.setPlaceholder(new Label("There are no offers for any of your assigned properties."));
                inspectionTableView.setPlaceholder(new Label("You have not scheduled any inspections yet."));

                btnAddListing.setVisible(false);

                MenuItem item1 = new MenuItem("View maintenance report");
                item1.setOnAction(this::viewMaintenanceReport);

                MenuItem item2 = new MenuItem("Contact property owner");
                item2.setOnAction(this::contactPropertyOwner);

                MenuItem item3 = new MenuItem("View management fee");
                item3.setOnAction(this::viewCommissionManagementFee);

                propertyContextMenu.getItems().add(item1);
                propertyContextMenu.getItems().add(item2);
                propertyContextMenu.getItems().add(item3);

                MenuItem contact = new MenuItem("Contact buyer");
                contact.setOnAction(this::contactCustomer);
                proposalContextMenu.getItems().add(contact);

                MenuItem resched = new MenuItem("Reschedule");
                resched.setOnAction(this::rescheduleInspection);

                MenuItem sched = new MenuItem("Cancel");
                sched.setOnAction(this::cancelInspection);

                inspectionContextMenu.getItems().add(resched);
                inspectionContextMenu.getItems().add(sched);

                tabPane.getTabs().remove(employeeTab);
                break;
            } //propertymanager

            case "admin": {
                employeeTableView.setPlaceholder(new Label("There are no registered employees in the system."));

                btnAddListing.setVisible(false);

                MenuItem item = new MenuItem("View assigned properties");
                item.setOnAction(this::viewAssignedProperties);

                employeeContextMenu.getItems().add(item);

                //remove all tabs except employees
                tabPane.getTabs().remove(proposalTab);
                tabPane.getTabs().remove(propertyTab);
                tabPane.getTabs().remove(inspectionTab);
                break;
            } //admin

            case "manager": {
                propertyTableView.setPlaceholder(new Label("There are no listings sent to the system."));
                proposalTableView.setPlaceholder(new Label("There are no offers or applications sent to the system."));
                inspectionTableView.setPlaceholder(new Label("Nobody has scheduled any inspections."));

                btnAddListing.setVisible(false);

                MenuItem item1 = new MenuItem("Assign to employee");
                item1.setOnAction(this::assignProperty);

                MenuItem item2 = new MenuItem("Inspect documents");
                item2.setOnAction(this::inspectDocuments);

                propertyContextMenu.getItems().add(item1);
                propertyContextMenu.getItems().add(item2);

                tabPane.getTabs().remove(employeeTab);
                break;
            } //manager

            case "guest":

            default: {
                propertyTableView.setPlaceholder(new Label("There are no active property listings."));

                updateDetailsItem.setVisible(false);
                logoutItem.setText("Login");
                btnAddListing.setVisible(false);
                propertyStateCmb.setVisible(false);

                //remove all tabs except properties
                tabPane.getTabs().remove(proposalTab);
                tabPane.getTabs().remove(employeeTab);
                tabPane.getTabs().remove(inspectionTab);
            }
        }

        if (userDBModel.isParttimer(userID)) {
            if (!tabPane.getTabs().contains(workingHoursTab))
                tabPane.getTabs().add(workingHoursTab);
        }

        refreshPropertyTable("all");
        refreshProposalTable("all");
        refreshInspectionTable("all");
        refreshEmployeeTable("all");
    }

    private void withdrawProposal(ActionEvent actionEvent) {
        Proposal proposal = proposalTableView.getSelectionModel().getTableView().getSelectionModel().getSelectedItem();

        if (proposal != null) {

        }
    }

    private void submitProposal(ActionEvent actionEvent) {
        Proposal proposal = proposalTableView.getSelectionModel().getTableView().getSelectionModel().getSelectedItem();

        if (proposal != null) {

        }
    }

    private void viewProposal(ActionEvent actionEvent) {
        Proposal proposal = proposalTableView.getSelectionModel().getTableView().getSelectionModel().getSelectedItem();

        if (proposal != null) {
            //either view as non-Customer (cannot edit) or view as Customer (has edit button)

        }

    }

    //get customer from proposal
    private void contactCustomer(ActionEvent actionEvent) {
        Proposal proposal = proposalTableView.getSelectionModel().getTableView().getSelectionModel().getSelectedItem();

        if (proposal != null) {

        }
    }

    @FXML
    void scheduleInspection(ActionEvent event) {
        Inspection inspection = inspectionTableView.getSelectionModel().getTableView().getSelectionModel().getSelectedItem();

        if (inspection != null) {

        }
    }

    private void rescheduleInspection(ActionEvent actionEvent) {
        Inspection inspection = inspectionTableView.getSelectionModel().getTableView().getSelectionModel().getSelectedItem();

        if (inspection != null) {

        }
    }

    private void cancelInspection(ActionEvent actionEvent) {
        Inspection inspection = inspectionTableView.getSelectionModel().getTableView().getSelectionModel().getSelectedItem();

        if (inspection != null) {

        }
    }

    private void viewAssignedProperties(ActionEvent actionEvent) {
        Employee employee = employeeTableView.getSelectionModel().getTableView().getSelectionModel().getSelectedItem();

        if (employee != null) {

        }
    }

    private void viewCommissionManagementFee(ActionEvent actionEvent) {

    }

    //get property owner from property
    private void contactPropertyOwner(ActionEvent actionEvent) {
        Property property = propertyTableView.getSelectionModel().getTableView().getSelectionModel().getSelectedItem();

        if (property != null) {

        }
    }

    private void viewMaintenanceReport(ActionEvent actionEvent) {
        Property property = propertyTableView.getSelectionModel().getTableView().getSelectionModel().getSelectedItem();

        if (property != null) {

        }
    }

    private void deactivateProperty(ActionEvent actionEvent) {
        Property property = propertyTableView.getSelectionModel().getTableView().getSelectionModel().getSelectedItem();

        if (property != null) {

        }
    }

    private void inspectDocuments(ActionEvent actionEvent) {
        Property property = propertyTableView.getSelectionModel().getTableView().getSelectionModel().getSelectedItem();

        if (property != null) {

        }
    }

    private void assignProperty(ActionEvent event) {
        Property property = propertyTableView.getSelectionModel().getTableView().getSelectionModel().getSelectedItem();

        if (property != null) {
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
            assignPropertyController.setUserDBModel(userDBModel);
            assignPropertyController.isRentalProperty(property.getPropertyID().startsWith("rental"));

            User currentAssignment = userDBModel.currentlyAssignedToProperty(property);

            if (currentAssignment == null)
                assignPropertyController.getLblCurrentAssign().setText("Currently assigned: NONE");
            else
                assignPropertyController.getLblCurrentAssign().setText("Currently assigned: " + currentAssignment.getUsername());

            assignPropertyController.hasConfirmedProperty().addListener((obs, wasConfirmed, isConfirmed) -> {
                if (isConfirmed) {
                    assignPropertyStage.hide();
                    propertyDBModel.assignProperty(property.getPropertyID(), assignPropertyController.getSelectedUser().getUserID());
                }
            });
            Scene scene = new Scene(rootLayout);
            assignPropertyStage.setScene(scene);
            assignPropertyStage.show();
        }
    }

    private void updateProperty(ActionEvent event) {
        Stage editPropertyStage = new Stage();
        editPropertyStage.setTitle("Edit Property Details");
        FXMLLoader loader = new FXMLLoader();
        if (registeredUserType.equals("landlord")) {
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
            controller.setUserDBModel(userDBModel);
            controller.setPropertyDBModel(propertyDBModel);

            Property p = propertyDBModel.getProperties().get(propertyTableView.getSelectionModel().getSelectedItem().getPropertyID());
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
            controller.setUsername(userDBModel.getUsername(userID));

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
            controller.setUserDBModel(userDBModel);
            controller.setPropertyDBModel(propertyDBModel);

            Property p = propertyDBModel.getProperties().get(propertyTableView.getSelectionModel().getSelectedItem().getPropertyID());
            controller.setPropertyID(Integer.parseInt(p.getPropertyID().replaceAll("[^\\d.]", "")));
            controller.setAddress(p.getAddress());
            controller.setSuburb(p.getSuburb());
            controller.setBaths(p.getBaths());
            controller.setBeds(p.getBeds());
            controller.setCars(p.getCars());
            controller.setPrice(p.getPrice());
            controller.setPropertyType(p.getPropertyType());
            controller.setUserID(userID);
            controller.setUsername(userDBModel.getUsername(userID));
            controller.start();

            Scene scene = new Scene(rootLayout);
            editPropertyStage.setScene(scene);
            editPropertyStage.show();
        }
    }


    @FXML
    void addWorkingHours(ActionEvent event) {

    }

    @FXML
    void runPayroll(ActionEvent event) {

    }

    @FXML
    void showNotifications(ActionEvent event) {
        System.out.println("NOTIFICATION POPUP HERE");
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    private void initClock() {
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy HH:mm");
            lblCurDate.setText("It is now: " + LocalDateTime.now().format(formatter));
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }
}