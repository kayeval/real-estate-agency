package main.controller;

import main.model.DBConnector;
import main.model.User.Customer.Buyer;
import main.model.User.Customer.Customer;
import main.model.User.Customer.Renter;
import main.model.User.Employee.BranchAdmin;
import main.model.User.Employee.BranchManager;
import main.model.User.Employee.Employee;
import main.model.User.Employee.PartTimeEmployee;
import main.model.User.Employee.SalesPerson.PropertyManager;
import main.model.User.Employee.SalesPerson.SalesConsultant;
import main.model.User.PropertyOwner.PropertyOwner;
import main.model.User.PropertyOwner.Vendor;
import main.model.User.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class UserController {
    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public Map<String, User> getCustomers() {
        Map<String, User> users = new HashMap<>();

        String sql = "SELECT userid, [name], email, password, (SELECT * FROM customers C WHERE userid=C.userid) FROM users";

        try (Connection conn = mainController.getDbConnector().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Customer customer = null;
                if (rs.getBoolean("buyer")) {
                    customer = new Buyer(rs.getString("name"), rs.getString("email"));
                    customer.setUserID("buyer" + rs.getInt("userid"));
                } else {
                    customer = new Renter(rs.getString("name"), rs.getString("email"), rs.getDouble("income"), rs.getString("occupation"));
                    customer.setUserID("renter" + rs.getInt("userid"));
                }

                users.putIfAbsent(customer.getUserID(), customer);
            }
        } catch (Exception ex) {
        }

        return users;
    }

    public Map<String, User> getEmployees() {
        Map<String, User> users = new HashMap<>();

        String sql = "SELECT userid, [name], email, password, (SELECT * FROM employees E WHERE userid=E.userid) FROM users";

        try (Connection conn = new DBConnector().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Employee employee = null;
                switch (rs.getString("type")) {
                    case "admin":
                        employee = new BranchAdmin(rs.getString("name"), rs.getString("email"), rs.getDate("hiredate").toLocalDate(), rs.getDouble("salary"));
                        break;
                    case "manager":
                        employee = new BranchManager(rs.getString("name"), rs.getString("email"), rs.getDate("hiredate").toLocalDate(), rs.getDouble("salary"));
                        break;
                    case "parttime":
                        employee = new PartTimeEmployee(rs.getString("name"), rs.getString("email"), rs.getDate("hiredate").toLocalDate(), rs.getDouble("salary"));
                        break;
                    case "propertymanager":
                        employee = new PropertyManager(rs.getString("name"), rs.getString("email"), rs.getDate("hiredate").toLocalDate(), rs.getDouble("salary"));
                        break;
                    case "salesconsultant":
                        employee = new SalesConsultant(rs.getString("name"), rs.getString("email"), rs.getDate("hiredate").toLocalDate(), rs.getDouble("salary"));
                        break;
                    default:
                }

                employee.setUserID(rs.getString("type") + rs.getInt("userid"));
                users.putIfAbsent(employee.getUserID(), employee);
            }
        } catch (Exception ex) {
        }

        return users;
    }

    public Map<String, User> getPropertyOwners() {
        Map<String, User> users = new HashMap<>();

        String sql = "SELECT userid, [name], email, password, (SELECT vendor FROM propertyowners P WHERE userid=P.userid) FROM users";

        try (Connection conn = mainController.getDbConnector().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                PropertyOwner propertyOwner = null;
                if (rs.getBoolean("vendor")) {
                    propertyOwner = new Vendor(rs.getString("name"), rs.getString("email"));
                    propertyOwner.setUserID("vendor" + rs.getInt("userid"));
                } else {
                    propertyOwner = new Vendor(rs.getString("name"), rs.getString("email"));
                    propertyOwner.setUserID("landlord" + rs.getInt("userid"));
                }

                users.putIfAbsent(propertyOwner.getUserID(), propertyOwner);
            }
        } catch (Exception ex) {
        }

        return users;
    }

    public Boolean isUnique(String username) {
        boolean isAvailable = true;

        String sql = "SELECT COUNT(*) AS total FROM users WHERE username='" + username + "'";

        try (Connection conn = mainController.getDbConnector().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                if (rs.getInt("total") > 0)
                    isAvailable = false;
            }
        } catch (Exception ex) {
        }

        return isAvailable;
    }

    public boolean canLogin(String username, String password) {
        boolean found = false;

        String sql = "SELECT COUNT(*) AS total FROM users WHERE username='" + username.toLowerCase() + "' AND password='" + hash(password) + "'";

        try (Connection conn = mainController.getDbConnector().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                if (rs.getInt("total") == 1)
                    found = true;
            }
        } catch (Exception ex) {
        }

        return found;
    }

    public String hash(String password) {
        try {
            MessageDigest sha512 = MessageDigest.getInstance("SHA-512");
            String salt = "some_random_salt";
            String passWithSalt = password + salt;
            byte[] passBytes = passWithSalt.getBytes();
            byte[] passHash = sha512.digest(passBytes);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < passHash.length; i++) {
                sb.append(Integer.toString((passHash[i] & 0xff) + 0x100, 16).substring(1));
            }
            String generatedPassword = sb.toString();
            return generatedPassword;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    //contain only ASCII letters and digits, with hyphens, underscores and spaces
    public boolean isValidUsernameFormat(String username) {
        final Pattern USERNAME_REGEX = Pattern.compile("^[A-Za-z0-9]+(?:[ _-][A-Za-z0-9]+)*$",
                Pattern.CASE_INSENSITIVE);
        return USERNAME_REGEX.matcher(username).matches();
    }

    public boolean isValidEmailFormat(String email) {
        final Pattern EMAIL_REGEX = Pattern.compile("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
                Pattern.CASE_INSENSITIVE);
        return EMAIL_REGEX.matcher(email).matches();
    }

    public void registerUser(String username, String email, String password, String occupation, String income) throws SQLException {
        String sql = "INSERT INTO users (username, email, password) VALUES(?, ?, ?)";
        Connection conn = mainController.getDbConnector().getConnection();
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, username);
            pstmt.setString(2, email);
            pstmt.setString(3, hash(password));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (pstmt != null) {
            ResultSet rs = pstmt.getGeneratedKeys();

            if (rs.next()) {
                sql = "INSERT INTO customers (userid, buyer, income, occupation) VALUES (?, ?, ?, ?)";
                pstmt = conn.prepareStatement(sql);

                pstmt.setInt(1, rs.getInt(1));
                pstmt.setBoolean(2, occupation.equals("") && income.equals(""));

                Double incomeVal = null;
                if (!income.equals("")) incomeVal = Double.parseDouble(income);

                pstmt.setObject(3, incomeVal, Types.DOUBLE);
                pstmt.setString(4, occupation);
                pstmt.executeUpdate();
            }
        }
    }

    public void registerCustomer() {

    }

    //TODO
    public void updateUserDetails() {

    }

    //DELETE USER?
    public void deleteUser(String username) {

    }
}
