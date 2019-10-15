package main.model;

import java.sql.*;
import java.time.LocalDate;

public class WorkingHoursDBModel {
    private DBConnector dbConnector;

    public WorkingHoursDBModel() {
        dbConnector = new DBConnector();
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


}
