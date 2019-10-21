package main.model.DBModel;

import main.model.Property.Property;
import main.model.Proposal.Application;
import main.model.Proposal.ContractDuration;
import main.model.Proposal.Offer;
import main.model.Proposal.Proposal;
import main.model.User.Customer.Customer;
import main.model.User.Employee.BranchManager;
import main.model.User.Employee.SalesPerson.SalesPerson;
import main.model.User.PropertyOwner.PropertyOwner;
import main.model.User.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ProposalDBModel {
    private DBConnector dbConnector;
    private UserDBModel userDBModel;
    private PropertyDBModel propertyDBModel;
    private Map<String, Proposal> allProposals;

    public ProposalDBModel() {
        dbConnector = new DBConnector();
    }

    public void setPropertyDBModel(PropertyDBModel propertyDBModel) {
        this.propertyDBModel = propertyDBModel;
        loadProposalsFromDB();
    }

    public void setUserDBModel(UserDBModel userDBModel) {
        this.userDBModel = userDBModel;
    }

    private void updateCustomersForProposal(int proposalID, Collection<User> customers) throws SQLException {
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

    public int addProposal(double price, Collection<User> customers, String propertyID, ContractDuration contractDuration) throws SQLException {
        String sql = "INSERT INTO proposals (offerprice, submissiondate, accepted, " +
                "propertyid, contractduration, paid, withdrawn, waitingforpay, pending) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";

        int id = 0;

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setObject(1, price, Types.DOUBLE);
            pstmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now(ZoneId.systemDefault())));
            pstmt.setBoolean(3, false);
            pstmt.setInt(4, Integer.parseInt(propertyID.replaceAll("[^\\d.]", "")));
            if (contractDuration != null) {
                pstmt.setString(5, contractDuration.toString());
            } else pstmt.setString(5, "");
            pstmt.setBoolean(6, false);
            pstmt.setBoolean(7, false);
            pstmt.setBoolean(8, false);
            pstmt.setBoolean(9, true);

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();

            if (rs.next()) {
                id = rs.getInt(1);
                updateCustomersForProposal(id, customers);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        loadProposalsFromDB();

        return id;
    }

    //restriction: can only accept if within 3 day timeframe from submission
    public void acceptProposal(String proposalID) {
        String sql = "UPDATE proposals SET accepted=?, waitingforpay=?, pending=?, acceptdate=? WHERE proposalid=?";
        LocalDateTime dateAccepted = LocalDateTime.now(ZoneId.systemDefault());

        try (Connection connection = dbConnector.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setBoolean(1, true);
            pstmt.setBoolean(2, true);
            pstmt.setBoolean(3, false);
            pstmt.setTimestamp(4, Timestamp.valueOf(dateAccepted));
            pstmt.setInt(5, Integer.parseInt(proposalID.replaceAll("[^\\d.]", "")));

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        allProposals.get(proposalID).setAccepted(true);
        allProposals.get(proposalID).setPending(false);
        allProposals.get(proposalID).setWaitingForPayment(true);
        allProposals.get(proposalID).setAcceptDate(dateAccepted);
        allProposals.get(proposalID).getProperty().setProposal(allProposals.get(proposalID));
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

        allProposals.get(proposalID).setPaid(true);
        allProposals.get(proposalID).setWaitingForPayment(false);

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

    public void updateProposal(double price, Collection<User> users, ContractDuration contractDuration, String proposalID) throws SQLException {
        String sql = "UPDATE proposals SET offerprice=?, contractduration=?  WHERE proposalid=?";
        int proposalid = Integer.parseInt(proposalID.replaceAll("[^\\d.]", ""));

        try (Connection connection = dbConnector.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setObject(1, price, Types.DOUBLE);

            if (contractDuration != null) {
                pstmt.setString(2, contractDuration.toString());
            } else pstmt.setString(2, "");

            pstmt.setInt(3, proposalid);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        updateCustomersForProposal(proposalid, users);
        Map<String, User> applicants = new HashMap<>();
        for (User u : users)
            applicants.putIfAbsent(u.getUserID(), u);

        allProposals.get(proposalID).setPrice(price);
        allProposals.get(proposalID).setApplicants(applicants);

        if (allProposals.get(proposalID) instanceof Application)
            ((Application) allProposals.get(proposalID)).setContractDuration(contractDuration);
    }

    //also for reject
    public void withdrawProposal(String proposalID) {
        String sql = "UPDATE proposals SET pending=?, withdrawn=?, waitingforpay=? WHERE proposalid=?";

        try (Connection connection = dbConnector.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setBoolean(1, false);
            pstmt.setBoolean(2, true);
            pstmt.setBoolean(3, false);
            pstmt.setInt(4, Integer.parseInt(proposalID.replaceAll("[^\\d.]", "")));

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        allProposals.get(proposalID).setWithdrawn(true);
        allProposals.get(proposalID).setWaitingForPayment(false);
        allProposals.get(proposalID).setPending(false);
    }

    public Map<String, Proposal> getProposals() {
        allProposals = new HashMap<>();

        String sql = "SELECT p.proposalid, offerprice, submissiondate, acceptdate, accepted, pending, propertyid, contractduration, paid, withdrawn, " +
                "waitingforpay, c.userid FROM proposals p JOIN customerproposals c ON p.proposalid = c.proposalid";
        try (Connection conn = dbConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Proposal p;
                double price = rs.getDouble("offerprice");
                LocalDateTime submissionDate = rs.getTimestamp("submissiondate").toLocalDateTime();
                boolean accepted = rs.getBoolean("accepted");
                int propertyid = rs.getInt("propertyid");
                boolean paid = rs.getBoolean("paid");
                boolean pending = rs.getBoolean("pending");
                boolean withdrawn = rs.getBoolean("withdrawn");
                boolean waitingforpay = rs.getBoolean("waitingforpay");
                String proposalid = rs.getInt("proposalid") + "";

                if (!rs.getString("contractduration").equals("")) { //application
                    if (allProposals.get(proposalid) == null)
                        p = new Application(price, propertyDBModel.getAllProperties().get("rental" + propertyid),
                                ContractDuration.valueOf(rs.getString("contractduration")));
                    else p = allProposals.get(proposalid);

                } else {
                    if (allProposals.get(proposalid) == null)
                        p = new Offer(price, propertyDBModel.getAllProperties().get("sale" + propertyid));
                    else p = allProposals.get(proposalid);
                }
                p.setProposalID(rs.getInt("proposalid") + "");
                p.setWithdrawn(withdrawn);
                p.setAccepted(accepted);
                if (accepted) {
                    p.setAcceptDate(rs.getTimestamp("acceptdate").toLocalDateTime());
                }
                p.setSubmissionDate(submissionDate);
                p.setPaid(paid);
                p.setPending(pending);
                p.setWaitingForPayment(waitingforpay);

                //set property proposal to this
                p.getProperty().setProposal(p);

                User applicant = userDBModel.getUser(rs.getInt("userid"));
                ((Customer) applicant).getProposals().putIfAbsent(p.getProposalID(), p);
                p.getApplicants().putIfAbsent(applicant.getUserID(), applicant);

                allProposals.putIfAbsent(p.getProposalID(), p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return allProposals;
    }

    public Map<String, Proposal> getProposals(User user) {
        Map<String, Proposal> proposals = new HashMap<>();

        if (user instanceof Customer) {
            for (Proposal p : allProposals.values())
                if (p.getApplicants().containsKey(user.getUserID())) {
                    proposals.putIfAbsent(p.getProposalID(), p);
                }
        } else if (user instanceof SalesPerson) {
            for (Proposal p : allProposals.values())
                if (p.getProperty().getSalesPerson().getUserID().equals(user.getUserID()))
                    proposals.putIfAbsent(p.getProposalID(), p);
        } else if (user instanceof PropertyOwner) {
            for (Proposal p : allProposals.values())
                if (p.getProperty().getPropertyOwner().getUserID().equals(user.getUserID()))
                    proposals.putIfAbsent(p.getProposalID(), p);
        } else if (user instanceof BranchManager) {
            proposals = allProposals;
        }

        return proposals;
    }

    public Map<String, Proposal> getAcceptedProposals(User user) {
        Map<String, Proposal> proposals = new HashMap<>();

        for (Proposal p : getProposals(user).values())
            if (p.isAccepted())
                proposals.putIfAbsent(p.getProposalID(), p);

        return proposals;
    }

    public Map<String, Proposal> getPendingProposals(User user) {
        Map<String, Proposal> proposals = new HashMap<>();

        for (Proposal p : getProposals(user).values())
            if (p.isPending())
                proposals.putIfAbsent(p.getProposalID(), p);

        return proposals;
    }

    public Map<String, Proposal> getRejectedProposals(User user) {
        Map<String, Proposal> proposals = new HashMap<>();

        for (Proposal p : getProposals(user).values())
            if (!p.isAccepted() && !p.isPending())
                proposals.putIfAbsent(p.getProposalID(), p);

        return proposals;
    }

    public boolean hasSubmittedProposalForProperty(User user, Property property) {
        boolean found = false;

        Iterator iter = ((Customer) user).getProposals().entrySet().iterator();

        while (!found && iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();

            Proposal proposal = (Proposal) entry.getValue();
            if (proposal.getProperty().getPropertyID().equals(property.getPropertyID())) {
                found = true;
            }
        }

        return found;
    }

    public void loadProposalsFromDB() {
        allProposals = getProposals();
    }
}
