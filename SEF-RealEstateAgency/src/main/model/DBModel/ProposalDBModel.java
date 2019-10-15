package main.model.DBModel;

import main.model.Proposal.ContractDuration;
import main.model.Proposal.Proposal;
import main.model.User.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ProposalDBModel {
    private DBConnector dbConnector;
    private UserDBModel userDBModel;

    public ProposalDBModel() {
        dbConnector = new DBConnector();
    }

    public void setUserDBModel(UserDBModel userDBModel) {
        this.userDBModel = userDBModel;
    }

    private void updateCustomersForProposal(int proposalID, ArrayList<User> customers) throws SQLException {
        String sql = "DELETE FROM customerproposals WHERE proposalID=?";
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, proposalID);
            pstmt.executeUpdate();
        }

        sql = "INSERT INTO customerproposals values (?, ?)";
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            int i = 0;
            for (User user : customers) {
                pstmt.setInt(1, proposalID);
                pstmt.setInt(2, Integer.parseInt(user.getUserID().replaceAll("[^\\d.]", "")));
                pstmt.addBatch();
                i++;

                if (i % 1000 == 0 || i == customers.size()) {
                    pstmt.executeBatch(); // Execute every 1000 items
                }
            }
        }
    }


    public void addProposal(double price, ArrayList<User> customers, String propertyID, Set<ContractDuration> contractDurations) throws SQLException {
        String sql = "INSERT INTO proposals (offerprice, submissiondate, accepted, " +
                "propertyid, contractduration, paid, withdrawn, waitingforpay) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";

        int id = 0;
        Connection conn = dbConnector.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        pstmt.setObject(1, price, Types.DOUBLE);
        pstmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now(ZoneId.systemDefault())));
        pstmt.setBoolean(3, false);
        pstmt.setInt(4, Integer.parseInt(propertyID.replaceAll("[^\\d.]", "")));
        if (contractDurations != null) {
            ArrayList<String> acceptedContractDurations = new ArrayList<>();
            for (ContractDuration contractDuration : contractDurations)
                acceptedContractDurations.add(contractDuration.toString());
            pstmt.setString(5, String.join(",", acceptedContractDurations));
        } else pstmt.setString(5, "");
        pstmt.setBoolean(6, false);
        pstmt.setBoolean(7, false);
        pstmt.setBoolean(8, false);

        pstmt.executeUpdate();

        ResultSet rs = pstmt.getGeneratedKeys();

        if (rs.next()) {
            id = rs.getInt(1);
            updateCustomersForProposal(id, customers);
        }
    }

    public void acceptProposal(String proposalID) {
        String sql = "UPDATE proposals SET accepted=?, waitingforpay=? WHERE proposalid=?";
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setBoolean(1, true);
            pstmt.setBoolean(2, true);
            pstmt.setInt(3, Integer.parseInt(proposalID.replaceAll("[^\\d.]", "")));

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void payProposal(String proposalID) {
        String sql = "UPDATE proposals SET waitingforpay=?, paid=? WHERE proposalid=?";
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setBoolean(1, false);
            pstmt.setBoolean(2, true);
            pstmt.setInt(3, Integer.parseInt(proposalID.replaceAll("[^\\d.]", "")));

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    public void rejectProposal(String proposalID) {
//        String sql = "UPDATE proposals SET accepted=? WHERE proposalid=?";
//        try (Connection connection = dbConnector.getConnection();
//             PreparedStatement pstmt = connection.prepareStatement(sql)) {
//            pstmt.setBoolean(1, false);
//            pstmt.setInt(2, Integer.parseInt(proposalID.replaceAll("[^\\d.]", "")));
//
//            pstmt.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    public void updateProposal(double price, int userID, String
            propertyID, Set<ContractDuration> contractDurations, String proposalID) {
        String sql = "UPDATE proposals SET withdrawn=? WHERE proposalid=?";
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setObject(1, price, Types.DOUBLE);
            pstmt.setInt(2, userID);
            pstmt.setInt(3, Integer.parseInt(proposalID.replaceAll("[^\\d.]", "")));

            if (contractDurations != null) {
                ArrayList<String> acceptedContractDurations = new ArrayList<>();
                for (ContractDuration contractDuration : contractDurations)
                    acceptedContractDurations.add(contractDuration.toString());
                pstmt.setString(4, String.join(",", acceptedContractDurations));
            } else pstmt.setString(4, "");

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //also for reject
    public void withdrawProposal(String proposalID) {
        String sql = "UPDATE proposals SET withdrawn=?, waitingforpay=? WHERE proposalid=?";
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setBoolean(1, true);
            pstmt.setBoolean(2, false);
            pstmt.setInt(3, Integer.parseInt(proposalID.replaceAll("[^\\d.]", "")));

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Proposal> getProposals() {
        Map<String, Proposal> proposals = new HashMap<>();


        return proposals;
    }

    public Map<String, Proposal> getActiveProposals() {
        Map<String, Proposal> proposals = new HashMap<>();


        return proposals;
    }

    public Map<String, Proposal> getPendingProposals() {
        Map<String, Proposal> proposals = new HashMap<>();


        return proposals;
    }

    public Map<String, Proposal> getInactiveProposals() {
        Map<String, Proposal> proposals = new HashMap<>();


        return proposals;
    }

    public Map<String, Proposal> getProposals(String type, int userID) {
        Map<String, Proposal> proposals = new HashMap<>();
        //customer

        //propertyowner

        //salesperson

        return proposals;
    }

    public Map<String, Proposal> getActiveProposals(String type, int userID) {
        Map<String, Proposal> proposals = new HashMap<>();

        //customer

        //propertyowner

        //salesperson

        return proposals;
    }

    public Map<String, Proposal> getPendingProposals(String type, int userID) {
        Map<String, Proposal> proposals = new HashMap<>();

        //customer

        //propertyowner

        //salesperson

        return proposals;
    }

    public Map<String, Proposal> getInactiveProposals(String type, int userID) {
        Map<String, Proposal> proposals = new HashMap<>();

        //customer

        //propertyowner

        //salesperson

        return proposals;
    }
}
