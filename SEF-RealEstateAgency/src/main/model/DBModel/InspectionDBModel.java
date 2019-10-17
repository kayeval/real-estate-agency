package main.model.DBModel;

import main.model.Inspection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

public class InspectionDBModel {
    private DBConnector dbConnector;

    public InspectionDBModel() {
        dbConnector = new DBConnector();
        refreshInspections();
    }

    public void addInspection(LocalDateTime date, String propertyID, int userID) {
        String sql = "INSERT INTO inspections (submissiondate, propertyid, cancelled, " +
                "userid, duedate) VALUES(?, ?, ?, ?, ?)";

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now(ZoneId.systemDefault())));
            pstmt.setInt(2, Integer.parseInt(propertyID.replaceAll("[^\\d.]", "")));
            pstmt.setBoolean(3, false);
            pstmt.setInt(4, userID);
            pstmt.setTimestamp(5, Timestamp.valueOf(date));

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void cancelInspection(int inspectionID) {
        String sql = "UPDATE inspections SET cancelled=? WHERE inspectionid=?";
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setBoolean(1, true);
            pstmt.setInt(2, inspectionID);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void rescheduleInspection(LocalDateTime date, String inspectionID) {
        String sql = "UPDATE inspections SET duedate=? WHERE inspectionid=?";
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setTimestamp(1, Timestamp.valueOf(date));
            pstmt.setInt(2, Integer.parseInt(inspectionID));

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Inspection> getInspections() {
        Map<String, Inspection> inspections = new HashMap<>();


        return inspections;
    }

    public Map<String, Inspection> getScheduledInspections() {
        Map<String, Inspection> inspections = new HashMap<>();


        return inspections;
    }

    public Map<String, Inspection> getCompletedInspections() {
        Map<String, Inspection> inspections = new HashMap<>();


        return inspections;
    }

    public Map<String, Inspection> getCancelledInspections() {
        Map<String, Inspection> inspections = new HashMap<>();


        return inspections;
    }

    public Map<String, Inspection> getInspections(String type, int userID) {
        Map<String, Inspection> inspections = new HashMap<>();

        if (type.equals("renter") || type.equals("buyer")) {

        } else if (type.equals("landlord") || type.equals("vendor")) {

        } else if (type.equals("salesconsultant") || type.equals("propertymanager")) {

        }

        return inspections;
    }

    public Map<String, Inspection> getScheduledInspections(String type, int userID) {
        Map<String, Inspection> inspections = new HashMap<>();

        if (type.equals("renter") || type.equals("buyer")) {

        } else if (type.equals("landlord") || type.equals("vendor")) {

        } else if (type.equals("salesconsultant") || type.equals("propertymanager")) {

        }

        return inspections;
    }

    public Map<String, Inspection> getCompletedInspections(String type, int userID) {
        Map<String, Inspection> inspections = new HashMap<>();

        if (type.equals("renter") || type.equals("buyer")) {

        } else if (type.equals("landlord") || type.equals("vendor")) {

        } else if (type.equals("salesconsultant") || type.equals("propertymanager")) {

        }

        return inspections;
    }

    public Map<String, Inspection> getCancelledInspections(String type, int userID) {
        Map<String, Inspection> inspections = new HashMap<>();

        if (type.equals("renter") || type.equals("buyer")) {

        } else if (type.equals("landlord") || type.equals("vendor")) {

        } else if (type.equals("salesconsultant") || type.equals("propertymanager")) {

        }

        return inspections;
    }

    public void refreshInspections() {
        //todo
    }

    //check for clash in timeslot
    public boolean hasNoConflict(LocalDateTime tentative) {
        boolean valid = true;



        return valid;
    }
}
