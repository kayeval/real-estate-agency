package main.model.DBModel;

import main.model.Inspection;
import main.model.Property.Property;
import main.model.User.Customer.Customer;
import main.model.User.Employee.SalesPerson.SalesPerson;
import main.model.User.PropertyOwner.PropertyOwner;
import main.model.User.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

public class InspectionDBModel {
    private DBConnector dbConnector;
    private PropertyDBModel propertyDBModel;
    private UserDBModel userDBModel;
    private Map<String, Inspection> allInspections;

    public InspectionDBModel() {
        dbConnector = new DBConnector();
    }

    public void loadInspectionsFromDB() {
        allInspections = getInspections();
    }

    public Inspection addInspection(LocalDateTime dueDate, Property property, User user) {
        String sql = "INSERT INTO inspections (submissiondate, propertyid, cancelled, " +
                "userid, duedate) VALUES(?, ?, ?, ?, ?)";
        Inspection inspection = null;
        int id = 0;
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now(ZoneId.systemDefault())));
            pstmt.setInt(2, Integer.parseInt(property.getPropertyID().replaceAll("[^\\d.]", "")));
            pstmt.setBoolean(3, false);
            pstmt.setInt(4, Integer.parseInt(user.getUserID().replaceAll("[^\\d.]", "")));
            pstmt.setTimestamp(5, Timestamp.valueOf(dueDate));

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();

            if (rs.next()) {
                id = rs.getInt(1);
                inspection = new Inspection(dueDate);
                inspection.setProperty(property);
                inspection.setInspectionID(id + "");
                inspection.setCustomer(user);
                
                allInspections.putIfAbsent(inspection.getInspectionID(), inspection);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return inspection;
    }

    public void cancelInspection(Inspection inspection) {
        String sql = "UPDATE inspections SET cancelled=? WHERE inspectionid=?";
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setBoolean(1, true);
            pstmt.setInt(2, Integer.parseInt(inspection.getInspectionID().replaceAll("[^\\d.]", "")));

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        inspection.setCancelled(true);
    }

    public void rescheduleInspection(LocalDateTime date, Inspection inspection) {
        String sql = "UPDATE inspections SET duedate=? WHERE inspectionid=?";
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setTimestamp(1, Timestamp.valueOf(date));
            pstmt.setInt(2, Integer.parseInt(inspection.getInspectionID().replaceAll("[^\\d.]", "")));

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        inspection.setDueDate(date);
    }

    private Map<String, Inspection> getInspections() {
        Map<String, Inspection> inspections = new HashMap<>();

        String sql = "SELECT * from inspections";
        try (Connection conn = dbConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Inspection i = new Inspection(rs.getTimestamp("duedate").toLocalDateTime());
                i.setInspectionID(rs.getInt("inspectionid") + "");
                i.setDateCreated(rs.getTimestamp("submissiondate").toLocalDateTime());
                i.setCancelled(rs.getBoolean("cancelled"));

                i.setProperty(propertyDBModel.getProperty(rs.getInt("propertyid")));
                Customer user = ((Customer) userDBModel.getUser(rs.getInt("userid")));
                user.addInspection(i);
                i.setCustomer(user);

                inspections.putIfAbsent(i.getInspectionID(), i);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return inspections;
    }

    public Map<String, Inspection> getInspections(SalesPerson salesPerson) {
        Map<String, Inspection> inspections = new HashMap<>();

        for (Inspection i : allInspections.values())
            for (Property p : salesPerson.getAssignedProperties().values()) {
                if (i.getProperty().getPropertyID().equals(p.getPropertyID()))
                    inspections.putIfAbsent(i.getInspectionID(), i);
            }

        return inspections;
    }

    public Map<String, Inspection> getInspections(PropertyOwner propertyOwner) {
        Map<String, Inspection> inspections = new HashMap<>();

        for (Inspection i : allInspections.values())
            for (Property p : propertyOwner.getListedProperties().values()) {
                if (i.getProperty().getPropertyID().equals(p.getPropertyID()))
                    inspections.putIfAbsent(i.getInspectionID(), i);
            }

        return inspections;
    }

    public Map<String, Inspection> getInspections(Customer customer) {
        Map<String, Inspection> inspections = new HashMap<>();

        for (Inspection i : allInspections.values())
            if (i.getCustomer().getUserID().equals(customer.getUserID()))
                inspections.putIfAbsent(i.getInspectionID(), i);

        return inspections;
    }

    public Map<String, Inspection> getInspections(User user) {
        Map<String, Inspection> inspections = new HashMap<>();

        if (user instanceof Customer) {
            inspections = getInspections((Customer) user);
        } else if (user instanceof PropertyOwner) {
            inspections = getInspections((PropertyOwner) user);
        } else if (user instanceof SalesPerson) {
            inspections = getInspections((SalesPerson) user);
        } else {
            inspections = allInspections;
        }

        return inspections;
    }

    public Map<String, Inspection> getScheduledInspections(User user) {
        Map<String, Inspection> inspections = new HashMap<>();

        for (Inspection i : getInspections(user).values()) {
            if (!i.isCancelled() && i.getDueDate().isBefore(LocalDateTime.now(ZoneId.systemDefault())))
                inspections.putIfAbsent(i.getInspectionID(), i);
        }

        return inspections;
    }

    public Map<String, Inspection> getCompletedInspections(User user) {
        Map<String, Inspection> inspections = new HashMap<>();

        for (Inspection i : getInspections(user).values()) {
            if (!i.isCancelled() && i.getDueDate().isAfter(LocalDateTime.now(ZoneId.systemDefault())))
                inspections.putIfAbsent(i.getInspectionID(), i);
        }

        return inspections;
    }

    public Map<String, Inspection> getCancelledInspections(User user) {
        Map<String, Inspection> inspections = new HashMap<>();

        for (Inspection i : getInspections(user).values()) {
            if (i.isCancelled())
                inspections.putIfAbsent(i.getInspectionID(), i);
        }

        return inspections;
    }

    //check for clash in timeslot
    public boolean isTimeAvailable(Property property, LocalDateTime tentative) {
        boolean valid = true;

        for (Inspection i : allInspections.values())
            if (i.getProperty().getPropertyID().equals(property.getPropertyID()))
                if (i.getDueDate().isEqual(tentative))
                    valid = false;

        return valid;
    }

    public void setPropertyDBModel(PropertyDBModel propertyDBModel) {
        this.propertyDBModel = propertyDBModel;
        loadInspectionsFromDB();
    }

    public void setUserDBModel(UserDBModel userDBModel) {
        this.userDBModel = userDBModel;
    }
}
