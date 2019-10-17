package main.controller;

//TODO FIX ERROR ON LOADING FXML FILES FOR VIEW OFFER/APPLICATION AND MAINTENANCE REPORT


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
import main.model.DBModel.*;
import main.model.Inspection;
import main.model.Property.*;
import main.model.Proposal.*;
import main.model.User.Customer.Buyer;
import main.model.User.Customer.Customer;
import main.model.User.Customer.Renter;
import main.model.User.Employee.BranchManager;
import main.model.User.Employee.Employee;
import main.model.User.Employee.PartTimeEmployee;
import main.model.User.Employee.SalesPerson.SalesPerson;
import main.model.User.User;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class MainController {

    @FXML
    private TableView<Property> propertyTableView;

    @FXML
    private TableColumn<Property, Boolean> propertyAssignedField;

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
    private TableColumn<Inspection, String> inspectionDateField;

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
    private TableColumn<Proposal, String> proposalDateField;

    @FXML
    private TableColumn<Proposal, Double> offerPriceField;

    @FXML
    private TableColumn<Employee, Double> emplSalaryField;

    @FXML
    private ComboBox<String> employeeStateCmb;

    @FXML
    private TableColumn<Employee, String> emplHireDateField;

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
        userDBModel.loadUsersFromDB();
        propertyDBModel = new PropertyDBModel();
//        propertyDBModel.refreshProperties();

        userDBModel.setPropertyDBModel(propertyDBModel);
        propertyDBModel.setUserDBModel(userDBModel);

        proposalDBModel = new ProposalDBModel();
//        proposalDBModel.refreshProposals();

        proposalDBModel.setUserDBModel(userDBModel);
        proposalDBModel.setPropertyDBModel(propertyDBModel);

        workingHoursDBModel = new WorkingHoursDBModel();
//        workingHoursDBModel.refreshWorkingHours();

        workingHoursDBModel.setUserDBModel(userDBModel);

        inspectionDBModel = new InspectionDBModel();
//        inspectionDBModel.refreshInspections();

        bankAccountDBModel = new BankAccountDBModel();

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
        switch (employeeStateCmb.getSelectionModel().getSelectedItem()) {
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
        emplHireDateField.setCellValueFactory(new PropertyValueFactory<>("hireDateFormatted"));
        emplTypeField.setCellValueFactory(new PropertyValueFactory<>("type"));
        emplSalaryField.setCellValueFactory(new PropertyValueFactory<>("salary"));
        emplUsernameField.setCellValueFactory(new PropertyValueFactory<>("username"));
        emplStatusField.setCellValueFactory(new PropertyValueFactory<>("status"));

        employeeTableView.setItems(employees);
        employeeTableView.setContextMenu(employeeContextMenu);
    }

    public void refreshProposalTable() {
        //TODO table not refreshing right after update
        System.out.println("PROPOSAL TABLE REFRESH");

        switch (proposalStateCmb.getSelectionModel().getSelectedItem()) {
            case "Accepted": {
                proposals = FXCollections.observableArrayList(proposalDBModel.getAcceptedProposals(user).values());
                break;
            }
            case "Pending": {
                proposals = FXCollections.observableArrayList(proposalDBModel.getPendingProposals(user).values());
                break;
            }
            case "Rejected": {
                proposals = FXCollections.observableArrayList(proposalDBModel.getRejectedProposals(user).values());
                break;
            }
            default:
                proposals = FXCollections.observableArrayList(proposalDBModel.getProposals(user).values());
        }

        //set cell value factory
        waitingForPaymentField.setCellValueFactory(new PropertyValueFactory<>("waitingForPayment"));
        proposalDateField.setCellValueFactory(new PropertyValueFactory<>("submissionDateFormatted"));
        proposalPropertyField.setCellValueFactory(new PropertyValueFactory<>("propertyAddress"));
        offerPriceField.setCellValueFactory(new PropertyValueFactory<>("price"));
        proposalStatusField.setCellValueFactory(new PropertyValueFactory<>("status"));

        proposalTableView.setItems(proposals);
        proposalTableView.setContextMenu(proposalContextMenu);
    }

    public void refreshInspectionTable() {
        //TODO

        switch (inspectionStateCmb.getSelectionModel().getSelectedItem()) {
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
        //TODO

        switch (workingHoursStateCmb.getSelectionModel().getSelectedItem()) {
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
            case "guest":
                properties = FXCollections.observableArrayList(propertyDBModel.getActiveProperties().values());
                break;
            case "buyer":
                properties = FXCollections.observableArrayList(propertyDBModel.getActiveSales().values());
                break;
            case "renter":
                properties = FXCollections.observableArrayList(propertyDBModel.getActiveRentals().values());
                break;
            case "vendor":
            case "landlord":
            case "propertymanager":
            case "salesconsultant":
                switch (propertyStateCmb.getSelectionModel().getSelectedItem()) {
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
            default:
                switch (propertyStateCmb.getSelectionModel().getSelectedItem()) {
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
                        properties = FXCollections.observableArrayList(propertyDBModel.getAllProperties().values());
                }
                break;
        }

        propertyAssignedField.setCellValueFactory(new PropertyValueFactory<Property, Boolean>("hasBeenAssigned"));
        propertyAssignedField.setCellFactory(column -> new TableCell<>() {
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);

                TableRow<Property> currentRow = getTableRow();
                if (!isEmpty() && user instanceof BranchManager) {
                    if (item)
                        currentRow.setStyle("-fx-background-color:transparent");
                    else
                        currentRow.setStyle("-fx-background-color:pink");
                }
            }
        });
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
        addListingStage.setTitle("Add New Listing");
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
                    addListingStage.hide();

                    Alert actionSuccess = new Alert(Alert.AlertType.INFORMATION);
                    actionSuccess.setTitle("Add listing successful");
                    actionSuccess.setHeaderText(null);
                    actionSuccess.setContentText("You have successfully added a new property listing.");

                    actionSuccess.showAndWait();
                    refreshPropertyTable();
                }
            });
        } else {
            AddRentalPropertyController addRentalPropertyController = loader.getController();

            addRentalPropertyController.hasSavedProperty().addListener((obs, wasSaved, isSaved) -> {
                if (isSaved) {
                    propertyDBModel.addProperty(addRentalPropertyController.getAddress(), addRentalPropertyController.getSuburb(), addRentalPropertyController.getPropertyType(),
                            addRentalPropertyController.getBaths(), addRentalPropertyController.getCars(), addRentalPropertyController.getBeds(), addRentalPropertyController.getPrice(),
                            addRentalPropertyController.getContractDurations(), userID);

                    addListingStage.hide();

                    Alert actionSuccess = new Alert(Alert.AlertType.INFORMATION);
                    actionSuccess.setTitle("Add listing successful");
                    actionSuccess.setHeaderText(null);
                    actionSuccess.setContentText("You have successfully added a new property listing.");

                    actionSuccess.showAndWait();
                    refreshPropertyTable();
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
        controller.setUserDBModel(userDBModel);
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

                    Alert actionSuccess = new Alert(Alert.AlertType.INFORMATION);
                    actionSuccess.setTitle("Update user details successful");
                    actionSuccess.setHeaderText(null);
                    actionSuccess.setContentText("You have successfully updated your details.");

                    actionSuccess.showAndWait();
                    refreshEmployeeTable();
                }
            });
            editCustomerController.setUser(user);
            editCustomerController.setUserDBModel(userDBModel);

            User u;
            if (registeredUserType.equals("renter")) {
                u = userDBModel.getUser(userID);
                editCustomerController.setBuyer(false);
                editCustomerController.setIncome(((Renter) u).getIncome());
                editCustomerController.setOccupation(((Renter) u).getOccupation());
            } else {
                u = userDBModel.getUser(userID);
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

                    Alert actionSuccess = new Alert(Alert.AlertType.INFORMATION);
                    actionSuccess.setTitle("Update user details successful");
                    actionSuccess.setHeaderText(null);
                    actionSuccess.setContentText("You have successfully updated your details.");

                    actionSuccess.showAndWait();
                    refreshEmployeeTable();
                }
            });
            editUserController.setUserDBModel(userDBModel);

            editUserController.setUser(userDBModel.getUser(userID));
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

                contactCustomerMenuItem = new MenuItem("Contact renter/s");
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
                logoutItem.setText("Back to login");
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

        if (!registeredUserType.equals("guest") && userDBModel.getUser(userID).getUserID().startsWith("manager")) {
            addWorkingHoursBtn.setVisible(false);

            if (!tabPane.getTabs().contains(workingHoursTab))
                tabPane.getTabs().add(workingHoursTab);

            workingHoursTableView.setPlaceholder(new Label("No part-timers have added any working hours yet."));

            MenuItem item = new MenuItem("Approve");
            item.setOnAction(this::approveWorkingHours);
            workingHoursContextMenu.getItems().add(item);
        }

        refreshPropertyTable();
        refreshProposalTable();
        refreshInspectionTable();
        refreshEmployeeTable();
    }

    private void approveWorkingHours(ActionEvent event) {
        //todo

        refreshWorkingHoursTable();
    }

    private void withdrawProposal(ActionEvent actionEvent) {
        Proposal proposal = proposalTableView.getSelectionModel().getTableView().getSelectionModel().getSelectedItem();
        String proposaltype;

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
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Withdraw " + proposaltype);
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to proceed?");

                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == ButtonType.OK) {
                    proposalDBModel.withdrawProposal(proposal.getProposalID());

                    Alert actionSuccess = new Alert(Alert.AlertType.INFORMATION);
                    actionSuccess.setTitle("Withdraw " + proposaltype + " successful");
                    actionSuccess.setHeaderText(null);
                    actionSuccess.setContentText("You have successfully withdrawn your " + proposaltype + ".");

                    actionSuccess.showAndWait();
                    refreshProposalTable();
                }
            }
        }

    }

    public void showCancelledInspectionDialog(String action) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(action + " unsuccessful");
        alert.setHeaderText(null);
        alert.setContentText("This inspection has already been cancelled.");

        alert.showAndWait();
    }

    public void showInactivePropertyDialog(String action) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(action + " unsuccessful");
        alert.setHeaderText(null);
        alert.setContentText("This property is still pending or already deactivated.");

        alert.showAndWait();
    }

    private void submitProposal(ActionEvent actionEvent) {
        Property property = propertyTableView.getSelectionModel().getTableView().getSelectionModel().getSelectedItem();

        String proposaltype;

        if (user instanceof Buyer)
            proposaltype = "offer";
        else proposaltype = "application";

        if (property != null) {
            try {
                if (property.isActive()) {
                    if (user instanceof Customer) {
                        // cannot submit if there's already an accepted proposal for the property
                        if (property.getProposal() == null) {

                            if (!proposalDBModel.hasSubmittedProposalForProperty(user, property)) {
                                if (user instanceof Buyer) {
                                    Stage submitOfferStage = new Stage();
                                    submitOfferStage.setTitle("Submit Offer");
                                    // Load root layout from fxml file
                                    FXMLLoader loader = new FXMLLoader();
                                    loader.setLocation(getClass().getResource("/main/view/SubmitOffer.fxml"));
                                    AnchorPane rootLayout = loader.load();

                                    SubmitOfferController controller = loader.getController();
                                    controller.setUser(user);
                                    controller.setProperty(property);
                                    controller.canSubmitProperty().addListener((obs, wasConfirmed, isConfirmed) -> {
                                        if (isConfirmed) {
                                            submitOfferStage.hide();
                                            try {
                                                proposalDBModel.addProposal(controller.getProposedPrice(), Collections.singletonList(user),
                                                        property.getPropertyID(), null);
                                            } catch (SQLException e) {
                                                e.printStackTrace();
                                            }

                                            Alert actionSuccess = new Alert(Alert.AlertType.INFORMATION);
                                            actionSuccess.setTitle("Submission successful");
                                            actionSuccess.setHeaderText(null);
                                            actionSuccess.setContentText("You have successfully submitted your " + proposaltype + ".");

                                            actionSuccess.showAndWait();
                                            refreshProposalTable();
                                        }
                                    });

                                    // Show the scene containing the root layout
                                    Scene scene = new Scene(rootLayout);
                                    submitOfferStage.setScene(scene);
                                    submitOfferStage.show();
                                } else {
                                    Stage submitApplicationStage = new Stage();
                                    submitApplicationStage.setTitle("Submit Application");
                                    // Load root layout from fxml file
                                    FXMLLoader loader = new FXMLLoader();
                                    loader.setLocation(getClass().getResource("/main/view/SubmitApplication.fxml"));
                                    AnchorPane rootLayout = loader.load();

                                    SubmitApplicationController submitApplicationController = loader.getController();
                                    submitApplicationController.setUser(user);
                                    submitApplicationController.setProperty(property);
                                    submitApplicationController.setRenters(userDBModel.getRenters().values());
                                    submitApplicationController.canSubmitProperty().addListener((obs, wasConfirmed, isConfirmed) -> {
                                        if (isConfirmed) {
                                            submitApplicationStage.hide();

                                            try {
                                                int id = proposalDBModel.addProposal(submitApplicationController.getProposedPrice(), submitApplicationController.getApplicants(),
                                                        property.getPropertyID(), submitApplicationController.getContractDuration());

                                                ((Customer) user).submitProposal(proposalDBModel.getProposals().get(id + ""));
                                            } catch (SQLException | SoldPropertyException | InvalidContractDurationException | ProposalNotFoundException | DeactivatedPropertyException | PendingProposalException e) {
                                                e.printStackTrace();
                                            }

                                            Alert actionSuccess = new Alert(Alert.AlertType.INFORMATION);
                                            actionSuccess.setTitle("Submission successful");
                                            actionSuccess.setHeaderText(null);
                                            actionSuccess.setContentText("You have successfully submitted your " + proposaltype + ".");

                                            actionSuccess.showAndWait();
                                            refreshProposalTable();
                                        }
                                    });

                                    // Show the scene containing the root layout
                                    Scene scene = new Scene(rootLayout);
                                    submitApplicationStage.setScene(scene);
                                    submitApplicationStage.show();
                                }
                            } else {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Error");
                                alert.setHeaderText("Not allowed to submit this " + proposaltype);
                                alert.setContentText("You have already submitted an " + proposaltype + " for this property.");

                                alert.showAndWait();
                            }
                        } else {

                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Submission unsuccessful");
                            alert.setHeaderText(null);
                            alert.setContentText("Another " + proposaltype + " has already been submitted for this property.");

                            alert.showAndWait();
                        }
                    }
                }
            } catch (DeactivatedPropertyException | IOException e) {
                showInactivePropertyDialog("Submit " + proposaltype);
                e.printStackTrace();
            }
        }
    }

    private void viewProposal(ActionEvent actionEvent) {
        Proposal proposal = proposalTableView.getSelectionModel().getTableView().getSelectionModel().getSelectedItem();
        String proposaltype;

        if (proposal != null) {
            if (proposal.isPending()) {
                if (proposal instanceof Offer) {
                    proposaltype = "offer";

                    Stage viewOfferStage = new Stage();
                    viewOfferStage.setTitle("View offer");

                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/main/view/ViewOffer.fxml"));
                    AnchorPane rootLayout = null;
                    try {
                        rootLayout = loader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ViewOfferController controller = loader.getController();
                    controller.setUser(user);
                    controller.setProperty(proposal.getProperty());

                    controller.canSubmitProperty().addListener((obs, wasConfirmed, isConfirmed) -> {
                        if (isConfirmed) {
                            viewOfferStage.hide();

                            try {
                                proposalDBModel.updateProposal(controller.getProposedPrice(), Collections.singletonList(user), null,
                                        proposal.getProposalID());
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }

                            Alert actionSuccess = new Alert(Alert.AlertType.INFORMATION);
                            actionSuccess.setTitle("Update successful");
                            actionSuccess.setHeaderText(null);
                            actionSuccess.setContentText("You have successfully changed your " + proposaltype + " details.");

                            refreshProposalTable();
                            actionSuccess.showAndWait();
                        }
                    });

                    controller.isAcceptedProperty().addListener((observableValue, wasAccepted, isAccepted) -> {
                        if (isAccepted) {
                            viewOfferStage.hide();
                            proposalDBModel.acceptProposal(proposal.getProposalID());

                            Alert actionSuccess = new Alert(Alert.AlertType.INFORMATION);
                            actionSuccess.setTitle("Accept successful");
                            actionSuccess.setHeaderText(null);
                            actionSuccess.setContentText("You have successfully accepted this " + proposaltype + ".");

                            actionSuccess.showAndWait();
                            refreshProposalTable();
                        } else {
                            viewOfferStage.hide();
                            proposalDBModel.withdrawProposal(proposal.getProposalID());

                            Alert actionSuccess = new Alert(Alert.AlertType.INFORMATION);
                            actionSuccess.setTitle("Reject successful");
                            actionSuccess.setHeaderText(null);
                            actionSuccess.setContentText("You have successfully rejected this " + proposaltype + ".");

                            actionSuccess.showAndWait();
                            refreshProposalTable();
                        }
                    });

                    // Show the scene containing the root layout
                    Scene scene = new Scene(rootLayout);
                    viewOfferStage.setScene(scene);
                    viewOfferStage.show();
                } else {
                    proposaltype = "application";

                    Stage viewApplicationStage = new Stage();
                    viewApplicationStage.setTitle("View application");

                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/main/view/ViewApplication.fxml"));
                    AnchorPane rootLayout = null;
                    try {
                        rootLayout = loader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ViewApplicationController controller = loader.getController();
                    controller.setUser(user);
                    controller.setProperty(proposal.getProperty());
                    controller.setRenters(userDBModel.getRenters().values());
                    controller.canSubmitProperty().addListener((obs, wasConfirmed, isConfirmed) -> {
                        if (isConfirmed) {
                            viewApplicationStage.hide();

                            try {
                                proposalDBModel.updateProposal(controller.getProposedPrice(), controller.getApplicants(), controller.getContractDuration(),
                                        proposal.getProposalID());
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }

                            Alert actionSuccess = new Alert(Alert.AlertType.INFORMATION);
                            actionSuccess.setTitle("Update successful");
                            actionSuccess.setHeaderText(null);
                            actionSuccess.setContentText("You have successfully changed your " + proposaltype + " details.");

                            actionSuccess.showAndWait();
                            refreshProposalTable();
                        }
                    });

                    controller.isAcceptedProperty().addListener((observableValue, wasAccepted, isAccepted) -> {
                        if (isAccepted) {
                            viewApplicationStage.hide();
                            proposalDBModel.acceptProposal(proposal.getProposalID());

                            Alert actionSuccess = new Alert(Alert.AlertType.INFORMATION);
                            actionSuccess.setTitle("Accept successful");
                            actionSuccess.setHeaderText(null);
                            actionSuccess.setContentText("You have successfully accepted this " + proposaltype + ".");

                            actionSuccess.showAndWait();
                            refreshProposalTable();
                        } else {
                            viewApplicationStage.hide();
                            proposalDBModel.withdrawProposal(proposal.getProposalID());

                            Alert actionSuccess = new Alert(Alert.AlertType.INFORMATION);
                            actionSuccess.setTitle("Reject successful");
                            actionSuccess.setHeaderText(null);
                            actionSuccess.setContentText("You have successfully rejected this " + proposaltype + ".");

                            actionSuccess.showAndWait();
                            refreshProposalTable();
                        }
                    });

                    // Show the scene containing the root layout
                    Scene scene = new Scene(rootLayout);
                    viewApplicationStage.setScene(scene);
                    viewApplicationStage.show();
                }
            } else {
                //show in read only format
                if (proposal instanceof Application) {
                    proposaltype = "application";

                    Stage viewApplicationStage = new Stage();
                    viewApplicationStage.setTitle("View application");

                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/main/view/ViewApplication.fxml"));
                    AnchorPane rootLayout = null;
                    try {
                        rootLayout = loader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ViewApplicationController controller = loader.getController();
                    controller.setUser(user);
                    controller.setProperty(proposal.getProperty());
                    controller.setRenters(userDBModel.getRenters().values());

                    controller.getAcceptBtn().setVisible(false);
                    controller.getRejectBtn().setVisible(false);
                    controller.getEditOrSaveBtn().setVisible(false);
                    controller.getAddApplicantBtn().setVisible(false);

                    Scene scene = new Scene(rootLayout);
                    viewApplicationStage.setScene(scene);
                    viewApplicationStage.show();
                } else {
                    proposaltype = "offer";

                    Stage viewOfferStage = new Stage();
                    viewOfferStage.setTitle("View offer");

                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/main/view/ViewOffer.fxml"));
                    AnchorPane rootLayout = null;
                    try {
                        rootLayout = loader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ViewOfferController controller = loader.getController();
                    controller.setUser(user);
                    controller.setProperty(proposal.getProperty());

                    controller.getAcceptBtn().setVisible(false);
                    controller.getRejectBtn().setVisible(false);
                    controller.getEditOrSaveBtn().setVisible(false);

                    Scene scene = new Scene(rootLayout);
                    viewOfferStage.setScene(scene);
                    viewOfferStage.show();
                }
            }
        }

    }

    //get customer from proposal
    private void contactCustomer(ActionEvent actionEvent) {
        Proposal proposal = proposalTableView.getSelectionModel().getTableView().getSelectionModel().getSelectedItem();

        if (proposal != null) {
            String customerType;

            List<String> emails = new ArrayList<>();

            if (proposal instanceof Offer) {
                customerType = "buyer";
                emails.add(((Offer) proposal).getApplicant().getEmail());
            } else {
                customerType = "renter";

                for (User u : proposal.getApplicants().values())
                    emails.add(u.getEmail());
            }

            Alert actionSuccess = new Alert(Alert.AlertType.INFORMATION);
            actionSuccess.setTitle("Contact " + customerType);
            actionSuccess.setHeaderText(null);
            actionSuccess.setContentText("Email: \n" + String.join(", ", emails));

            actionSuccess.showAndWait();
        }
    }

    @FXML
    void scheduleInspection(ActionEvent event) {
        Inspection inspection = inspectionTableView.getSelectionModel().getTableView().getSelectionModel().getSelectedItem();

        if (inspection != null) {
            //todo
        }

        refreshInspectionTable();
    }

    private void rescheduleInspection(ActionEvent actionEvent) {
        Inspection inspection = inspectionTableView.getSelectionModel().getTableView().getSelectionModel().getSelectedItem();

        if (inspection != null) {
            if (inspection.isCancelled()) {
                showCancelledInspectionDialog("Reschedule inspection");
            } else {
                //todo
            }
        }

        refreshInspectionTable();
    }

    private void cancelInspection(ActionEvent actionEvent) {
        Inspection inspection = inspectionTableView.getSelectionModel().getTableView().getSelectionModel().getSelectedItem();

        if (inspection != null) {
            if (inspection.isCancelled()) {
                showCancelledInspectionDialog("Cancel inspection");
            } else {
                //todo

            }
        }

        refreshInspectionTable();
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
        //todo
    }

    //get property owner from property
    private void contactPropertyOwner(ActionEvent actionEvent) {
        Property property = propertyTableView.getSelectionModel().getTableView().getSelectionModel().getSelectedItem();

        if (property != null) {
            String propertyOwnerType;

            if (property instanceof SaleProperty)
                propertyOwnerType = "vendor";
            else propertyOwnerType = "landlord";

            Alert actionSuccess = new Alert(Alert.AlertType.INFORMATION);
            actionSuccess.setTitle("Contact " + propertyOwnerType);
            actionSuccess.setHeaderText(null);
            actionSuccess.setContentText("Email of " + propertyOwnerType + " is: " + property.getPropertyOwner().getEmail());

            actionSuccess.showAndWait();
        }
    }

    private void viewMaintenanceReport(ActionEvent actionEvent) {
        Property property = propertyTableView.getSelectionModel().getTableView().getSelectionModel().getSelectedItem();

        if (property != null) {
            try {
                if (property.isActive()) {
                    Stage viewMaintenanceStage = new Stage();
                    viewMaintenanceStage.setTitle("View Maintenance Report");
                    FXMLLoader loader = new FXMLLoader();
//                    loader.setLocation(getClass().getResource("/main/view/ViewMaintenanceReport.fxml"));
//                    AnchorPane rootLayout = loader.load();

                    loader.setLocation(getClass().getResource("/main/view/ViewMaintenance.fxml"));
                    AnchorPane rootLayout = null;
                    try {
                        rootLayout = loader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

//                    AnchorPane root = null;
//                    try {
//                        root = FXMLLoader.load(getClass().getResource("/main/view/ViewMaintenanceReport.fxml"));
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

                    ViewMaintenanceController controller = loader.getController();
                    controller.setProperty((RentalProperty) property);

                    controller.canSubmitProperty().addListener((obs, wasConfirmed, isConfirmed) -> {
                        if (isConfirmed) {
                            viewMaintenanceStage.hide();

                            propertyDBModel.updateMaintenance(controller.getNextMaintenance(), property);

                            Alert actionSuccess = new Alert(Alert.AlertType.INFORMATION);
                            actionSuccess.setTitle("Update maintenance record successful");
                            actionSuccess.setHeaderText(null);
                            actionSuccess.setContentText("You have scheduled the next maintenance date.");

                            actionSuccess.showAndWait();
                            refreshPropertyTable();
                        }
                    });
                    Scene scene = new Scene(rootLayout);
                    viewMaintenanceStage.setScene(scene);
                    viewMaintenanceStage.show();

                }
            } catch (DeactivatedPropertyException e) {
                showInactivePropertyDialog("View maintenance report");
//                e.printStackTrace();
            }
        }

    }

    private void deactivateProperty(ActionEvent actionEvent) {
        Property property = propertyTableView.getSelectionModel().getTableView().getSelectionModel().getSelectedItem();

        if (property != null) {
            try {
                if (property.isActive()) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Deactivate property listing");
                    alert.setHeaderText(null);
                    alert.setContentText("Are you sure you want to proceed?");

                    Optional<ButtonType> result = alert.showAndWait();

                    if (result.get() == ButtonType.OK) {
                        propertyDBModel.deactivateListing(property);

                        Alert actionSuccess = new Alert(Alert.AlertType.INFORMATION);
                        actionSuccess.setTitle("Deactivate property listing successful");
                        actionSuccess.setHeaderText(null);
                        actionSuccess.setContentText("Successfully deactivated property listing for " + property.getAddress() + ".");

                        actionSuccess.showAndWait();
                        refreshPropertyTable();
                    }
                }
            } catch (DeactivatedPropertyException e) {
                showInactivePropertyDialog("Deactivate property");
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
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Activate property listing");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to proceed?");

                Optional<ButtonType> result = alert.showAndWait();

                if (result.get() == ButtonType.OK) {
                    propertyDBModel.setDocumentsInspected(property);

                    Alert actionSuccess = new Alert(Alert.AlertType.INFORMATION);
                    actionSuccess.setTitle("Inspect property documents");
                    actionSuccess.setHeaderText(null);
                    actionSuccess.setContentText("Property listing for " + property.getAddress() + " is now active.");

                    actionSuccess.showAndWait();
                    refreshPropertyTable();
                }
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

                        Alert actionSuccess = new Alert(Alert.AlertType.INFORMATION);
                        actionSuccess.setTitle("Property assignment successful");
                        actionSuccess.setHeaderText(null);
                        actionSuccess.setContentText("You have successfully assigned a property to " + assignPropertyController.getSelectedUser().getUsername() + ".");

                        actionSuccess.showAndWait();
                        refreshPropertyTable();
                    }
                });
                Scene scene = new Scene(rootLayout);
                assignPropertyStage.setScene(scene);
                assignPropertyStage.show();
            }
        }
    }

    private void updateProperty(ActionEvent event) {
        Property p = propertyDBModel.getAllProperties().get(propertyTableView.getSelectionModel().getSelectedItem().getPropertyID());

        if (p != null) {
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

                                Alert actionSuccess = new Alert(Alert.AlertType.INFORMATION);
                                actionSuccess.setTitle("Update listing successful");
                                actionSuccess.setHeaderText(null);
                                actionSuccess.setContentText("You have successfully updated the property listing.");

                                actionSuccess.showAndWait();
                                refreshPropertyTable();
                            }
                        });
                        controller.setUserDBModel(userDBModel);
                        controller.setPropertyDBModel(propertyDBModel);
                        controller.setUser(user);

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

                                Alert actionSuccess = new Alert(Alert.AlertType.INFORMATION);
                                actionSuccess.setTitle("Update listing successful");
                                actionSuccess.setHeaderText(null);
                                actionSuccess.setContentText("You have successfully updated the property listing.");

                                actionSuccess.showAndWait();
                                refreshPropertyTable();
                            }
                        });
                        controller.setUserDBModel(userDBModel);
                        controller.setPropertyDBModel(propertyDBModel);
                        controller.setUser(user);

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
                showInactivePropertyDialog("Change property listing details");
            }
        }
    }

    @FXML
    void addWorkingHours(ActionEvent event) {
        //TODO

        refreshWorkingHoursTable();
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