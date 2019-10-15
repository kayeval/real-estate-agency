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
import main.model.DBModel.*;
import main.model.Inspection;
import main.model.Property.DeactivatedPropertyException;
import main.model.Property.Property;
import main.model.Property.PropertyType;
import main.model.Property.RentalProperty;
import main.model.Proposal.Proposal;
import main.model.User.Customer.Buyer;
import main.model.User.Customer.Customer;
import main.model.User.Customer.Renter;
import main.model.User.Employee.Employee;
import main.model.User.Employee.PartTimeEmployee;
import main.model.User.Employee.SalesPerson.SalesPerson;
import main.model.User.User;

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
    private TableView<User> employeeTableView;

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

    @FXML
    private TableColumn<PartTimeEmployee, String> parttimeUsernameField;

    private int userID;
    private User user;
    private String registeredUserType;

    private DBConnector dbConnector = new DBConnector();

    private UserDBModel userDBModel;
    private PropertyDBModel propertyDBModel;
    private WorkingHoursDBModel workingHoursDBModel;
    private InspectionDBModel inspectionDBModel;
    private ProposalDBModel proposalDBModel;
    private BankAccountDBModel bankAccountDBModel;

    private ObservableList<Property> properties;
    private ObservableList<Inspection> inspections;
    private ObservableList<User> employees;
    private ObservableList<Proposal> proposals;

    //TODO for search by suburb
    private FilteredList<Property> filteredList;

    private ContextMenu propertyContextMenu, inspectionContextMenu, proposalContextMenu, employeeContextMenu, workingHoursContextMenu;

    private Property selectedProperty;
    private Inspection selectedInspection;
    private Proposal selectedProposal;
    private User selectedEmployee;

    private MenuItem cancelInspectionMenuItem, rescheduleInspectionMenuItem,
            contactCustomerMenuItem, viewPropertyManagementFeeOrCommissionMenuItem,
            contactPropertyOwnerMenuItem, organizeLegalDocumentsMenuItem,
            advertisePropertyMenuItem, deactivateListingMenuItem, editPropertyMenuItem,
            viewProposalMenuItem, withdrawProposalMenuItem, submitProposalMenuItem,
            viewMaintenanceReportMenuItem,
            viewAssignedPropertiesMenuItem,
            assignPropertyToEmployeeMenuItem,
            inspectPropertyDocumentsMenuItem;

    @FXML
    void initialize() {
        tabPane.getTabs().remove(workingHoursTab);

        initClock();

        //TODO set graphic for notification button


        //setup controllers
        userDBModel = new UserDBModel();
        propertyDBModel = new PropertyDBModel();
        propertyDBModel.setUserDBModel(userDBModel);

        proposalDBModel = new ProposalDBModel();
        proposalDBModel.setUserDBModel(userDBModel);

        workingHoursDBModel = new WorkingHoursDBModel();
        workingHoursDBModel.setUserDBModel(userDBModel);

        inspectionDBModel = new InspectionDBModel();

        bankAccountDBModel = new BankAccountDBModel();

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

        workingHoursContextMenu = new ContextMenu();
        workingHoursContextMenu.setAutoHide(true);

        //setup state combo boxes
        propertyStateCmb.getItems().addAll("All", "Active", "Pending", "Inactive");
        propertyStateCmb.getSelectionModel().selectFirst();
        propertyStateCmb.getSelectionModel().selectedIndexProperty().addListener((options, oldValue, newValue) -> {
            refreshPropertyTable();
        });

        employeeStateCmb.getItems().addAll("All", "Part-time", "Full-time");
        employeeStateCmb.getSelectionModel().selectFirst();
        employeeStateCmb.getSelectionModel().selectedIndexProperty().addListener((options, oldValue, newValue) -> {
            refreshEmployeeTable();
        });

        proposalStateCmb.getItems().addAll("All", "Accepted", "Pending", "Rejected");
        proposalStateCmb.getSelectionModel().selectFirst();
        proposalStateCmb.getSelectionModel().selectedIndexProperty().addListener((options, oldValue, newValue) -> {
            refreshProposalTable();
        });

        inspectionStateCmb.getItems().addAll("All", "Scheduled", "Completed", "Cancelled");
        inspectionStateCmb.getSelectionModel().selectFirst();
        inspectionStateCmb.getSelectionModel().selectedIndexProperty().addListener((options, oldValue, newValue) -> {
            refreshInspectionTable();
        });

        workingHoursStateCmb.getItems().addAll("All", "Approved", "Pending");
        workingHoursStateCmb.getSelectionModel().selectFirst();
        workingHoursStateCmb.getSelectionModel().selectedIndexProperty().addListener((options, oldValue, newValue) -> {
            refreshWorkingHoursTable();
        });
    }

    public void refreshEmployeeTable() {
        switch (employeeStateCmb.getValue()) {
            case "Part-time": {
                employees = FXCollections.observableArrayList(userDBModel.getParttimers().values());
                break;
            }
            case "Full-time": {
                employees = FXCollections.observableArrayList(userDBModel.getFulltimers().values());
                break;
            }
            default:
                employees = FXCollections.observableArrayList(userDBModel.getEmployees().values());
        }

        //set cell value factory

        employeeTableView.setItems(employees);
        employeeTableView.setContextMenu(employeeContextMenu);
    }

    public void refreshProposalTable() {
        switch (proposalStateCmb.getValue()) {
            case "Accepted": {
                break;
            }
            case "Pending": {
                break;
            }
            case "Rejected": {
                break;
            }
            default:

        }
    }

    public void refreshInspectionTable() {
        switch (inspectionStateCmb.getValue()) {
            case "Scheduled": {
                break;
            }
            case "Completed": {
                break;
            }
            case "Cancelled": {
                break;
            }
            default:

        }
    }

    public void refreshWorkingHoursTable() {
        switch (workingHoursStateCmb.getValue()) {
            case "Approved": {
                break;
            }
            case "Pending": {
                break;
            }
            default:

        }
    }

    public void refreshPropertyTable() {
        switch (registeredUserType) {
            case "buyer":
                properties = FXCollections.observableArrayList(propertyDBModel.getActiveSales().values());
                break;
            case "renter":
                properties = FXCollections.observableArrayList(propertyDBModel.getActiveRentals().values());
                break;
            case "vendor":
            case "landlord":
                switch (proposalStateCmb.getValue()) {
                    case "Inactive":
                        properties = FXCollections.observableArrayList(propertyDBModel.getInactiveProperties(registeredUserType, userID).values());
                        break;
                    case "Pending":
                        properties = FXCollections.observableArrayList(propertyDBModel.getPendingProperties(registeredUserType, userID).values());
                        break;
                    case "Active":
                        properties = FXCollections.observableArrayList(propertyDBModel.getActiveProperties(registeredUserType, userID).values());
                        break;
                    default:
                        properties = FXCollections.observableArrayList(propertyDBModel.getProperties(registeredUserType, userID).values());
                }
                break;
            case "propertymanager":
            case "salesconsultant":
                switch (proposalStateCmb.getValue()) {
                    case "Inactive":
                        properties = FXCollections.observableArrayList(propertyDBModel.getInactiveProperties(registeredUserType, userID).values());
                        break;
                    case "Pending":
                        properties = FXCollections.observableArrayList(propertyDBModel.getPendingProperties(registeredUserType, userID).values());
                        break;
                    case "Active":
                        properties = FXCollections.observableArrayList(propertyDBModel.getActiveProperties(registeredUserType, userID).values());
                        break;
                    default:
                        properties = FXCollections.observableArrayList(propertyDBModel.getAssignedProperties(userID).values());
                }
                break;
            default:
                switch (proposalStateCmb.getValue()) {
                    case "Inactive":
                        properties = FXCollections.observableArrayList(propertyDBModel.getInactiveProperties().values());
                        break;
                    case "Pending":
                        properties = FXCollections.observableArrayList(propertyDBModel.getPendingProperties().values());
                        break;
                    case "Active":
                        properties = FXCollections.observableArrayList(propertyDBModel.getActiveProperties().values());
                        break;
                    default:
                        properties = FXCollections.observableArrayList(propertyDBModel.getProperties().values());
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

//    public void refreshPropertyContextMenu() {
//        switch (registeredUserType) {
//            case "": {
//                switch (proposalStateCmb.getValue()) {
//                    case "Inactive": break;
//                    case "Pending": break;
//                    case "Active": break;
//                    default:
//                }
//                break;
//            }
//
//            case "" : {
//
//            }
//            default:
//        }
//    }
//
//    public void refreshInspectionContextMenu() {
//
//    }
//
//    public void refreshWorkingHoursContextMenu() {
//
//    }
//
//    public void refreshEmployeeContextMenu() {
//
//    }
//
//    public void refreshProposalContextMenu() {
//
//    }

    @FXML
    void addListing(ActionEvent event) throws IOException {

        Stage addListingStage = new Stage();
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
                    refreshPropertyTable();
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
                    refreshPropertyTable();
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

                submitProposalMenuItem = new MenuItem("Send offer");
                submitProposalMenuItem.setOnAction(this::submitProposal);

                propertyContextMenu.getItems().add(submitProposalMenuItem);

                viewProposalMenuItem = new MenuItem("View offer");
                viewProposalMenuItem.setOnAction(this::viewProposal);

                proposalContextMenu.getItems().add(viewProposalMenuItem);

                withdrawProposalMenuItem = new MenuItem("Withdraw offer");
                withdrawProposalMenuItem.setOnAction(this::withdrawProposal);

                proposalContextMenu.getItems().add(withdrawProposalMenuItem);

                cancelInspectionMenuItem = new MenuItem("Cancel inspection");
                cancelInspectionMenuItem.setOnAction(this::cancelInspection);

                inspectionContextMenu.getItems().add(cancelInspectionMenuItem);

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

                submitProposalMenuItem = new MenuItem("Send application");
                submitProposalMenuItem.setOnAction(this::submitProposal);
                propertyContextMenu.getItems().add(submitProposalMenuItem);

                viewProposalMenuItem = new MenuItem("View application");
                viewProposalMenuItem.setOnAction(this::viewProposal);
                proposalContextMenu.getItems().add(viewProposalMenuItem);

                withdrawProposalMenuItem = new MenuItem("Withdraw application");
                withdrawProposalMenuItem.setOnAction(this::withdrawProposal);
                proposalContextMenu.getItems().add(withdrawProposalMenuItem);

                cancelInspectionMenuItem = new MenuItem("Cancel inspection");
                cancelInspectionMenuItem.setOnAction(this::cancelInspection);
                inspectionContextMenu.getItems().add(cancelInspectionMenuItem);

                tabPane.getTabs().remove(employeeTab);
                btnScheduleInspection.setVisible(false);
                break;
            } //renter

            case "vendor": {
                propertyTableView.setPlaceholder(new Label("You have not added any property listings."));
                proposalTableView.setPlaceholder(new Label("You have not received any offers yet."));
                inspectionTableView.setPlaceholder(new Label("You have no scheduled inspections."));

                btnScheduleInspection.setVisible(false);

                editPropertyMenuItem = new MenuItem("Edit");
                editPropertyMenuItem.setOnAction(this::updateProperty);

//                MenuItem item2 = new MenuItem("Deactivate");
//                item2.setOnAction(this::deactivateProperty);

                viewProposalMenuItem = new MenuItem("View offer");
                viewProposalMenuItem.setOnAction(this::viewProposal);

                propertyContextMenu.getItems().add(editPropertyMenuItem);
//                propertyContextMenu.getItems().add(item2);
                proposalContextMenu.getItems().add(viewProposalMenuItem);

                tabPane.getTabs().remove(employeeTab);
                break;
            }
            case "landlord": {
                propertyTableView.setPlaceholder(new Label("You have not added any property listings."));
                proposalTableView.setPlaceholder(new Label("You have not received any applications yet."));
                inspectionTableView.setPlaceholder(new Label("You have no scheduled inspections."));

                btnScheduleInspection.setVisible(false);

                editPropertyMenuItem = new MenuItem("Edit");
                editPropertyMenuItem.setOnAction(this::updateProperty);

//                MenuItem item2 = new MenuItem("Deactivate");
//                item2.setOnAction(this::deactivateProperty);

                viewProposalMenuItem = new MenuItem("View application");
                viewProposalMenuItem.setOnAction(this::viewProposal);

                propertyContextMenu.getItems().add(editPropertyMenuItem);
//                propertyContextMenu.getItems().add(item2);
                proposalContextMenu.getItems().add(viewProposalMenuItem);

                tabPane.getTabs().remove(employeeTab);
                break;
            } //vendor

            case "salesconsultant": {
                propertyTableView.setPlaceholder(new Label("You have not been assigned any properties."));
                proposalTableView.setPlaceholder(new Label("There are no applications for any of your assigned properties."));
                inspectionTableView.setPlaceholder(new Label("You have not scheduled any inspections yet."));

                btnAddListing.setVisible(false);
                minPriceField.setText("Weekly Rental");

                deactivateListingMenuItem = new MenuItem("Deactivate");
                deactivateListingMenuItem.setOnAction(this::deactivateProperty);

                advertisePropertyMenuItem = new MenuItem("Advertise property");
//                item1.setOnAction(this::advertiseListing);

                organizeLegalDocumentsMenuItem = new MenuItem("Organize legal documents");
//                item2.setOnAction(this::organizeDocuments);

                contactPropertyOwnerMenuItem = new MenuItem("Contact property owner");
                contactPropertyOwnerMenuItem.setOnAction(this::contactPropertyOwner);

                viewPropertyManagementFeeOrCommissionMenuItem = new MenuItem("View commission");
                viewPropertyManagementFeeOrCommissionMenuItem.setOnAction(this::viewCommissionManagementFee);

                propertyContextMenu.getItems().add(deactivateListingMenuItem);
                propertyContextMenu.getItems().add(advertisePropertyMenuItem);
                propertyContextMenu.getItems().add(organizeLegalDocumentsMenuItem);
                propertyContextMenu.getItems().add(contactPropertyOwnerMenuItem);
                propertyContextMenu.getItems().add(viewPropertyManagementFeeOrCommissionMenuItem);

                contactCustomerMenuItem = new MenuItem("Contact renter");
                contactCustomerMenuItem.setOnAction(this::contactCustomer);
                proposalContextMenu.getItems().add(contactCustomerMenuItem);

                rescheduleInspectionMenuItem = new MenuItem("Reschedule");
                rescheduleInspectionMenuItem.setOnAction(this::rescheduleInspection);

                cancelInspectionMenuItem = new MenuItem("Cancel");
                cancelInspectionMenuItem.setOnAction(this::cancelInspection);

                inspectionContextMenu.getItems().add(rescheduleInspectionMenuItem);
                inspectionContextMenu.getItems().add(cancelInspectionMenuItem);

                tabPane.getTabs().remove(employeeTab);
                break;
            } //salesconsultant

            case "propertymanager": {
                propertyTableView.setPlaceholder(new Label("You have not been assigned any properties."));
                proposalTableView.setPlaceholder(new Label("There are no offers for any of your assigned properties."));
                inspectionTableView.setPlaceholder(new Label("You have not scheduled any inspections yet."));

                btnAddListing.setVisible(false);

                deactivateListingMenuItem = new MenuItem("Deactivate");
                deactivateListingMenuItem.setOnAction(this::deactivateProperty);

                viewMaintenanceReportMenuItem = new MenuItem("View maintenance report");
                viewMaintenanceReportMenuItem.setOnAction(this::viewMaintenanceReport);

                contactPropertyOwnerMenuItem = new MenuItem("Contact property owner");
                contactPropertyOwnerMenuItem.setOnAction(this::contactPropertyOwner);

                viewPropertyManagementFeeOrCommissionMenuItem = new MenuItem("View management fee");
                viewPropertyManagementFeeOrCommissionMenuItem.setOnAction(this::viewCommissionManagementFee);

                propertyContextMenu.getItems().add(deactivateListingMenuItem);
                propertyContextMenu.getItems().add(viewMaintenanceReportMenuItem);
                propertyContextMenu.getItems().add(contactPropertyOwnerMenuItem);
                propertyContextMenu.getItems().add(viewPropertyManagementFeeOrCommissionMenuItem);

                contactCustomerMenuItem = new MenuItem("Contact buyer");
                contactCustomerMenuItem.setOnAction(this::contactCustomer);
                proposalContextMenu.getItems().add(contactCustomerMenuItem);

                rescheduleInspectionMenuItem = new MenuItem("Reschedule");
                rescheduleInspectionMenuItem.setOnAction(this::rescheduleInspection);

                cancelInspectionMenuItem = new MenuItem("Cancel");
                cancelInspectionMenuItem.setOnAction(this::cancelInspection);

                inspectionContextMenu.getItems().add(rescheduleInspectionMenuItem);
                inspectionContextMenu.getItems().add(cancelInspectionMenuItem);

                tabPane.getTabs().remove(employeeTab);
                break;
            } //propertymanager

            case "admin": {
                employeeTableView.setPlaceholder(new Label("There are no registered employees in the system."));

                btnAddListing.setVisible(false);

                viewAssignedPropertiesMenuItem = new MenuItem("View assigned properties");
                viewAssignedPropertiesMenuItem.setOnAction(this::viewAssignedProperties);

                employeeContextMenu.getItems().add(viewAssignedPropertiesMenuItem);

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

                assignPropertyToEmployeeMenuItem = new MenuItem("Assign to employee");
                assignPropertyToEmployeeMenuItem.setOnAction(this::assignProperty);

                inspectPropertyDocumentsMenuItem = new MenuItem("Inspect documents");
                inspectPropertyDocumentsMenuItem.setOnAction(this::inspectDocuments);

                propertyContextMenu.getItems().add(assignPropertyToEmployeeMenuItem);
                propertyContextMenu.getItems().add(inspectPropertyDocumentsMenuItem);

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
            workingHoursTableView.setPlaceholder(new Label("You have not added any working hours yet."));
        }

        if (userDBModel.getUser(userID).getUserID().startsWith("manager")) {
            addWorkingHoursBtn.setVisible(false);

            if (!tabPane.getTabs().contains(workingHoursTab))
                tabPane.getTabs().add(workingHoursTab);

            workingHoursTableView.setPlaceholder(new Label("No part-timers have added any working hours yet."));

            MenuItem item = new MenuItem("Approve");
            workingHoursContextMenu.getItems().add(item);
        }

        refreshPropertyTable();
        refreshProposalTable();
        refreshInspectionTable();
        refreshEmployeeTable();
    }

    private void withdrawProposal(ActionEvent actionEvent) {
        Proposal proposal = proposalTableView.getSelectionModel().getTableView().getSelectionModel().getSelectedItem();
        String proposaltype = "proposal";

        if (user instanceof Buyer)
            proposaltype = "offer";
        else proposaltype = "application";

        if (proposal != null) {
            if (proposal.isWithdrawn()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Not allowed to withdraw this " + proposaltype);
                alert.setContentText("This " + proposaltype + " has already been withdrawn or rejected");

                alert.showAndWait();
            } else {
                //todo

            }
        }
    }

    public void showCancelledInspectionDialog() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Not allowed to cancel this inspection");
        alert.setContentText("This inspection has already been cancelled.");

        alert.showAndWait();
    }

    public void showInactivePropertyDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("No maintenance report");
        alert.setHeaderText(null);
        alert.setContentText("This property has already been deactivated.");

        alert.showAndWait();
    }

    private void submitProposal(ActionEvent actionEvent) {
        Property property = propertyTableView.getSelectionModel().getTableView().getSelectionModel().getSelectedItem();

        String proposaltype = "proposal";

        if (user instanceof Buyer)
            proposaltype = "offer";
        else proposaltype = "application";

        if (property != null) {
            try {
                if (property.isActive()) {
                    if (user instanceof Customer) {
                        if (((Customer) user).getProposals().containsValue(property)) {
                            //todo
                        } else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText("Not allowed to submit this " + proposaltype);
                            alert.setContentText("You have already submitted a " + proposaltype + " for this property.");

                            alert.showAndWait();
                        }
                    }
                }
            } catch (DeactivatedPropertyException e) {
                showInactivePropertyDialog();
            }
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
            if (inspection.isCancelled()) {
                showCancelledInspectionDialog();
            } else {
                //todo
            }
        }
    }

    private void rescheduleInspection(ActionEvent actionEvent) {
        Inspection inspection = inspectionTableView.getSelectionModel().getTableView().getSelectionModel().getSelectedItem();

        if (inspection != null) {
            if (inspection.isCancelled()) {
                showCancelledInspectionDialog();
            } else {
                //todo
            }
        }
    }

    private void cancelInspection(ActionEvent actionEvent) {
        Inspection inspection = inspectionTableView.getSelectionModel().getTableView().getSelectionModel().getSelectedItem();

        if (inspection != null) {
            if (inspection.isCancelled()) {
                showCancelledInspectionDialog();
            } else {
                //todo
            }
        }
    }

    private void viewAssignedProperties(ActionEvent actionEvent) {
        User employee = employeeTableView.getSelectionModel().getTableView().getSelectionModel().getSelectedItem();

        if (employee != null) {
            if (((SalesPerson) employee).getAssignedProperties().size() == 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("No properties to show");
                alert.setHeaderText(null);
                alert.setContentText("This employee has no assigned properties.");

                alert.showAndWait();
            } else {
                //todo
            }
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
            try {
                if (property.isActive()) {
                    //todo

                }
            } catch (DeactivatedPropertyException e) {
                showInactivePropertyDialog();
            }
        }
    }

    private void deactivateProperty(ActionEvent actionEvent) {
        Property property = propertyTableView.getSelectionModel().getTableView().getSelectionModel().getSelectedItem();

        if (property != null) {
            try {
                if (property.isActive()) {
                    //todo

                }
            } catch (DeactivatedPropertyException e) {
                showInactivePropertyDialog();
            }
        }
    }

    private void inspectDocuments(ActionEvent actionEvent) {
        Property property = propertyTableView.getSelectionModel().getTableView().getSelectionModel().getSelectedItem();

        if (property != null) {
            if (property.areDocumentsInspected()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Property already inspected");
                alert.setHeaderText(null);
                alert.setContentText("This property has already been inspected.");

                alert.showAndWait();
            } else {
                //todo
            }
        }
    }

    private void assignProperty(ActionEvent event) {
        Property property = propertyTableView.getSelectionModel().getTableView().getSelectionModel().getSelectedItem();

        if (property != null) {
            if (!property.areDocumentsInspected()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Not allowed to assign this property");
                alert.setContentText("You have not inspected the documents for this property yet.");

                alert.showAndWait();
            } else {
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
    }

    private void updateProperty(ActionEvent event) {
        Property p = propertyDBModel.getProperties().get(propertyTableView.getSelectionModel().getSelectedItem().getPropertyID());
        try {
            if (p.isActive()) {
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
        } catch (DeactivatedPropertyException e) {
            showInactivePropertyDialog();
        }
    }


    @FXML
    void addWorkingHours(ActionEvent event) {
        //TODO
    }

    @FXML
    void runPayroll(ActionEvent event) {
        //TODO
    }

    @FXML
    void showNotifications(ActionEvent event) {
        //TODO

    }

    public void setUserID(int userID) {
        this.userID = userID;

        this.user = userDBModel.getUser(userID);
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