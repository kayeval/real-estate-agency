package main.model.DBModel;

import main.model.Property.Property;
import main.model.User.Customer.Buyer;
import main.model.User.Customer.Customer;
import main.model.User.Customer.Renter;
import main.model.User.Employee.BranchAdmin;
import main.model.User.Employee.BranchManager;
import main.model.User.Employee.Employee;
import main.model.User.Employee.PartTimeEmployee;
import main.model.User.Employee.SalesPerson.PropertyManager;
import main.model.User.Employee.SalesPerson.SalesConsultant;
import main.model.User.InvalidEmailException;
import main.model.User.PropertyOwner.Landlord;
import main.model.User.PropertyOwner.Vendor;
import main.model.User.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class UserDBModel {
    private DBConnector dbConnector;
    private PropertyDBModel propertyDBModel;
    private Map<String, User> allUsers;
    private Map<String, User> customers;
    private Map<String, User> propertyOwners;
    private Map<String, User> employees;

    public UserDBModel() {
        dbConnector = new DBConnector();
        loadUsersFromDB();
    }

    public Map<String, User> getRenters() {
        Map<String, User> users = new HashMap<>();

        for (User u : customers.values())
            if (u instanceof Renter)
                users.putIfAbsent(u.getUserID(), u);

        return users;
    }

    public void loadUsersFromDB() {
        allUsers = new HashMap<>();

        customers = getCustomers();
        employees = getEmployees();
        propertyOwners = getPropertyOwners();

        allUsers.putAll(customers);
        allUsers.putAll(employees);
        allUsers.putAll(propertyOwners);
    }

    public void setPropertyDBModel(PropertyDBModel propertyDBModel) {
        this.propertyDBModel = propertyDBModel;
    }

    private Map<String, User> getCustomers() {
        customers = new HashMap<>();

        String sql = "SELECT u.userid, username, email, income, occupation, buyer, suburb FROM users u JOIN customers c ON u.userid=c.userid JOIN preferredsuburbs p ON c.userid=p.userid";
        try (Connection conn = dbConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            int id = 0;

            while (rs.next()) {
                id = rs.getInt("userid");

                User user;
                if (rs.getBoolean("buyer")) {
                    if (customers.get("buyer" + id) == null) {
                        user = new Buyer(rs.getString("username"), rs.getString("email"));
                        user.setUserID("buyer" + id);
                    } else user = customers.get("buyer" + id);
                } else {
                    if (customers.get("renter" + id) == null) {
                        user = new Renter(rs.getString("username"), rs.getString("email"), rs.getDouble("income"), rs.getString("occupation"));
                        user.setUserID("renter" + id);
                    } else user = customers.get("renter" + id);
                }
                ((Customer) user).getPreferredSuburbs().add(rs.getString("suburb"));
                customers.putIfAbsent(user.getUserID(), user);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return customers;
    }

    public Map<String, User> getEmployees() {
        employees = new HashMap<>();

        String sql = "SELECT u.userid, username, email, hiredate, etype, salary, parttime FROM users u JOIN employees e ON u.userid=e.userid";

        try (Connection conn = new DBConnector().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                User employee;
                switch (rs.getString("etype")) {
                    case "admin":
                        employee = new BranchAdmin(rs.getString("username"), rs.getString("email"), rs.getDate("hiredate").toLocalDate(), rs.getDouble("salary"));
                        break;
                    case "manager":
                        employee = new BranchManager(rs.getString("username"), rs.getString("email"), rs.getDate("hiredate").toLocalDate(), rs.getDouble("salary"));
                        break;
                    case "propertymanager":
                        employee = new PropertyManager(rs.getString("username"), rs.getString("email"), rs.getDate("hiredate").toLocalDate(), rs.getDouble("salary"));

                        //System.out.println("Assigned properties = " + propertyDBModel.getAssignedProperties(rs.getInt("userid")).values().size());
                        //set assigned properties
//                        ((SalesPerson) employee).setAssignedProperties(propertyDBModel.getAssignedProperties(rs.getInt("userid")));

                        //set scheduled inspections
                        break;
                    case "salesconsultant":
                        employee = new SalesConsultant(rs.getString("username"), rs.getString("email"), rs.getDate("hiredate").toLocalDate(), rs.getDouble("salary"));
                        //System.out.println("Assigned properties = " + propertyDBModel.getAssignedProperties(rs.getInt("userid")).values().size());

                        //set assigned properties
//                        ((SalesPerson) employee).setAssignedProperties(propertyDBModel.getAssignedProperties(rs.getInt("userid")));

                        //set scheduled inspections
                        break;
                    default:
                        employee = null;
                }
                if (employee != null) {
                    if (rs.getBoolean("parttime"))
                        ((Employee) employee).setPartTimeEmployee(new PartTimeEmployee());
                    employee.setUserID(rs.getString("etype") + rs.getInt("userid"));
                    employees.putIfAbsent(employee.getUserID(), employee);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return employees;
    }

    private Map<String, User> getPropertyOwners() {
        propertyOwners = new HashMap<>();

        String sql = "SELECT u.userid, username, email, vendor FROM users u JOIN propertyowners p ON u.userid=p.userid";

        try (Connection conn = dbConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                User propertyOwner;
                if (rs.getBoolean("vendor")) {
                    propertyOwner = new Vendor(rs.getString("username"), rs.getString("email"));
                    propertyOwner.setUserID("vendor" + rs.getInt("userid"));
                } else {
                    propertyOwner = new Vendor(rs.getString("username"), rs.getString("email"));
                    propertyOwner.setUserID("landlord" + rs.getInt("userid"));
                }

                propertyOwners.putIfAbsent(propertyOwner.getUserID(), propertyOwner);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return propertyOwners;
    }

    public boolean isUniqueUsername(String username) {
        boolean isAvailable = true;

        String sql = "SELECT COUNT(*) AS total FROM users WHERE username='" + username + "'";

        try (Connection conn = dbConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                if (rs.getInt("total") > 0)
                    isAvailable = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isAvailable;
    }

    public int canLogin(String username, String password) {
        boolean found = false;
        int id = 0;

        String sql = "SELECT userid FROM users WHERE username='" + username.toLowerCase() + "' AND password='" + hash(password) + "'";
        try (Connection conn = dbConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                id = rs.getInt("userid");
                if (id != 0)
                    found = true;
            }

            if (found) {
                sql = "UPDATE users SET lastlogin=? WHERE userid=?";

                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now(ZoneId.systemDefault())));
                pstmt.setInt(2, id);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return id;
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

    public void registerCustomer(String username, String email, String password, String occupation, String income, Set<String> preferredSuburbs) {
        int id = 0;

        String sql = "INSERT INTO users (username, email, password) VALUES(?, ?, ?)";

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, username);
            pstmt.setString(2, email);
            pstmt.setString(3, hash(password));
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();

            if (rs.next()) {
                id = rs.getInt(1);

                sql = "INSERT INTO customers (userid, buyer, income, occupation) VALUES (?, ?, ?, ?)";
                PreparedStatement pstmt2 = conn.prepareStatement(sql);

                pstmt2.setInt(1, id);
                pstmt2.setBoolean(2, occupation.equals("") && income.equals(""));

                Double incomeVal = null;
                if (!income.equals("")) incomeVal = Double.parseDouble(income);

                pstmt2.setObject(3, incomeVal, Types.DOUBLE);
                pstmt2.setString(4, occupation);
                pstmt2.executeUpdate();

                updatePreferredSuburbs(id, preferredSuburbs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        User u = null;
        if (income.equals("")) {
            try {
                u = new Buyer(username, email, preferredSuburbs);
                u.setUserID("buyer" + id);
            } catch (InvalidEmailException e) {
                e.printStackTrace();
            }
        } else {
            try {
                u = new Renter(username, email, preferredSuburbs, Double.parseDouble(income), occupation);
                u.setUserID("renter" + id);
            } catch (InvalidEmailException e) {
                e.printStackTrace();
            }
        }

        allUsers.putIfAbsent(u.getUserID(), u);
        customers.putIfAbsent(u.getUserID(), u);
    }

    private void updatePreferredSuburbs(int id, Set<String> preferredSuburbs) throws SQLException {
        String sql = "DELETE FROM preferredsuburbs WHERE userid=?";
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }

        sql = "INSERT INTO preferredsuburbs values (?, ?)";
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            int i = 0;
            for (String suburb : preferredSuburbs) {
                pstmt.setInt(1, id);
                pstmt.setString(2, suburb);
                pstmt.addBatch();
                i++;

                if (i % 1000 == 0 || i == preferredSuburbs.size()) {
                    pstmt.executeBatch(); // Execute every 1000 items
                }
            }
        }

        if (getUser(id) == null)
            loadUsersFromDB();
        else {
            ((Customer) getUser(id)).setPreferredSuburbs(preferredSuburbs);
        }
    }

    //MUST CALL canLogin() BEFORE
    public String getRegisteredUserType(int userID) {
        return getUser(userID).getUserID().replaceAll("[^A-Za-z]", "");
    }

    public int updateUserDetails(String username, String email, String oldUsername) {
        int id = 0;
        String sql = "UPDATE users SET username=?, email=? WHERE username=?";

        try (Connection connection = dbConnector.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, email);
            pstmt.setString(3, oldUsername);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        sql = "SELECT userid FROM users WHERE username='" + username + "'";
        try (Connection conn = dbConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next())
                id = rs.getInt("userid");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        getUser(id).setUsername(username);
        try {
            getUser(id).setEmail(email);
        } catch (InvalidEmailException e) {
            e.printStackTrace();
        }

        return id;
    }

    public User getUser(int userID) {
        User user = null;
        boolean found = false;

        Iterator iter = allUsers.entrySet().iterator();
        while (!found && iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();

            User u = (User) entry.getValue();
            if (Integer.parseInt(u.getUserID().replaceAll("[^\\d.]", "")) == userID) {
                found = true;
                user = u;
            }
        }

        return user;
    }

    public String getUsername(int id) {
        String username = "";

        String sql = "SELECT username FROM users WHERE userid=" + id;
        try (Connection conn = dbConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next())
                username = rs.getString("username");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return username;
    }

    public void updateCustomerDetails(User user, String username, String email, String occupation, String income, Set<String> preferredSuburbs, String oldUsername) throws SQLException {
        int id = updateUserDetails(username, email, oldUsername);

        String sql = "UPDATE customers SET occupation=?, income=? WHERE userid=?";
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, occupation);
            Double incomeVal = null;
            if (!income.equals("")) incomeVal = Double.parseDouble(income);

            pstmt.setObject(2, incomeVal, Types.DOUBLE);
            pstmt.setInt(3, id);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        getUser(id).setUsername(username);
        try {
            getUser(id).setEmail(email);
        } catch (InvalidEmailException e) {
            e.printStackTrace();
        }
        if (user instanceof Renter) {
            ((Renter) getUser(id)).setOccupation(occupation);
            ((Renter) getUser(id)).setIncome(Double.parseDouble(income));
        }

        updatePreferredSuburbs(id, preferredSuburbs);
    }

    //needed?
    public void deleteUser(String username) {
    }

    public void registerPropertyOwner(String username, String email, String password, boolean vendor) {
        int id = 0;

        String sql = "INSERT INTO users (username, email, password) VALUES(?, ?, ?)";
        try (Connection conn = dbConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, username);
            pstmt.setString(2, email);
            pstmt.setString(3, hash(password));
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();

            if (rs.next()) {
                id = rs.getInt(1);

                sql = "INSERT INTO propertyowners (userid, vendor) VALUES (?, ?)";
                PreparedStatement pstmt2 = conn.prepareStatement(sql);

                pstmt2.setInt(1, id);
                pstmt2.setBoolean(2, vendor);
                pstmt2.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        User u = null;
        if (vendor) {
            try {
                u = new Vendor(username, email);
                u.setUserID("vendor" + id);
            } catch (InvalidEmailException e) {
                e.printStackTrace();
            }
        } else {
            try {
                u = new Landlord(username, email);
            } catch (InvalidEmailException e) {
                e.printStackTrace();
            }
            u.setUserID("landlord" + id);
        }

        propertyOwners.putIfAbsent(u.getUserID(), u);
        allUsers.putIfAbsent(u.getUserID(), u);
    }

    public void registerEmployee(String username, String email, String password, double salary, LocalDate hireDate, String etype, boolean parttime) throws InvalidEmailException {
        int id = 0;

        String sql = "INSERT INTO users (username, email, password) VALUES(?, ?, ?)";

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, username);
            pstmt.setString(2, email);
            pstmt.setString(3, hash(password));
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();

            if (rs.next()) {
                id = rs.getInt(1);

                sql = "INSERT INTO employees (userid, salary, hiredate, etype, parttime) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement pstmt2 = conn.prepareStatement(sql);

                pstmt2.setInt(1, id);
                pstmt2.setDouble(2, salary);
                pstmt2.setDate(3, Date.valueOf(hireDate));
                pstmt2.setString(4, etype);
                pstmt2.setBoolean(5, parttime);
                pstmt2.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        User u = null;
        switch (etype) {
            case "salesconsultant":
                u = new SalesConsultant(username, email, hireDate, salary);
                break;
            case "propertymanager":
                u = new PropertyManager(username, email, hireDate, salary);
                break;
            case "admin":
                u = new BranchAdmin(username, email, hireDate, salary);
                break;
            case "manager":
                u = new BranchManager(username, email, hireDate, salary);
                break;
        }
        u.setUserID(etype + id);
        employees.putIfAbsent(u.getUserID(), u);
        allUsers.putIfAbsent(u.getUserID(), u);
    }

    public Map<String, User> getSalesPersons() {
        Map<String, User> salesPersons = new HashMap<>();

        for (String key : employees.keySet()) {
            if (key.startsWith("sales") || key.startsWith("property"))
                salesPersons.putIfAbsent(key, getEmployees().get(key));
        }

        return salesPersons;
    }

    public Map<String, User> getSalesConsultants() {
        Map<String, User> users = new HashMap<>();

        for (String key : getSalesPersons().keySet()) {
            if (key.startsWith("sales"))
                users.putIfAbsent(key, getSalesPersons().get(key));
        }

        return users;
    }

    public Map<String, User> getPropertyManagers() {
        Map<String, User> users = new HashMap<>();

        for (String key : getSalesPersons().keySet()) {
            if (!key.startsWith("sales"))
                users.putIfAbsent(key, getSalesPersons().get(key));
        }

        return users;
    }

    public User currentlyAssignedToProperty(Property p) {
        return p.getSalesPerson();
    }

    public boolean isParttimer(int userID) {
        boolean found = false, isParttime = false;

        Iterator iter = getEmployees().entrySet().iterator();

        while (!found && iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();

            Employee employee = (Employee) entry.getValue();
            if (employee.getUserID().contains(userID + "")) {
                found = true;

                if (employee.getPartTimeEmployee() != null)
                    isParttime = true;
            }
        }

        return isParttime;
    }

    public Map<String, User> getParttimers() {
        Map<String, User> users = new HashMap<>();

        for (User u : employees.values())
            if (((Employee) u).getPartTimeEmployee() != null)
                users.putIfAbsent(u.getUserID(), u);

        return users;
    }

    public Map<String, User> getFulltimers() {
        Map<String, User> users = new HashMap<>();

        for (User u : employees.values())
            if (((Employee) u).getPartTimeEmployee() == null)
                users.putIfAbsent(u.getUserID(), u);

        return users;
    }
}