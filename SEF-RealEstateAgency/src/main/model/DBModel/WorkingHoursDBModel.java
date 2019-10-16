package main.model.DBModel;

import main.model.User.WorkingHour;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkingHoursDBModel {
    private DBConnector dbConnector;
    private UserDBModel userDBModel;

    public WorkingHoursDBModel() {
        dbConnector = new DBConnector();
        refreshWorkingHours();
    }

    public void setUserDBModel(UserDBModel userDBModel) {
        this.userDBModel = userDBModel;
    }

    public void inputHours(LocalDate date, double hours, int userID) {
        String sql = "INSERT INTO workingentries (date, hours, approved, userid) values (?, ?, ?, ?)";

        try (Connection connection = dbConnector.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(date));
            pstmt.setObject(2, hours, Types.DOUBLE);
            pstmt.setBoolean(3, false);
            pstmt.setInt(3, userID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void approveHours(String workingID, String userID) {
        String sql = "UPDATE workingentries SET approved=? WHERE workingid=? AND userid=?";
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setBoolean(1, true);
            pstmt.setInt(2, Integer.parseInt(workingID));
            pstmt.setInt(3, Integer.parseInt(userID.replaceAll("[^\\d.]", "")));

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Map<String, List<WorkingHour>> getWorkingHours() {
        Map<String, List<WorkingHour>> allWorkingHours = new HashMap<>();

        return allWorkingHours;
    }

    public Map<String, List<WorkingHour>> getApprovedWorkingHours() {
        Map<String, List<WorkingHour>> allWorkingHours = new HashMap<>();

        return allWorkingHours;
    }

    public Map<String, WorkingHour> getWorkingHours(int userID) {
        Map<String, WorkingHour> workingHours = new HashMap<>();

        return workingHours;
    }

    public Map<String, WorkingHour> getApprovedWorkingHours(int userID) {
        Map<String, WorkingHour> workingHours = new HashMap<>();

        return workingHours;
    }

    public void refreshWorkingHours() {
    }
}
