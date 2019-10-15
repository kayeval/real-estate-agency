package main.model;

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
import main.model.User.Employee.SalesPerson.SalesPerson;
import main.model.User.PropertyOwner.Vendor;
import main.model.User.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Pattern;

public class UserDBModel {
    private DBConnector dbConnector;
    private PropertyDBModel propertyDBModel;

    public UserDBModel() {
        dbConnector = new DBConnector();
        propertyDBModel = new PropertyDBModel();
        propertyDBModel.setUserDBModel(this);
    }

    public Map<String, User> getCustomers() {
        Map<String, User> users = new HashMap<>();

        String sql = "SELECT u.userid, username, email, income, occupation, buyer, suburb FROM users u JOIN customers c ON u.userid=c.userid JOIN preferredsuburbs p ON c.userid=p.userid";
        try (Connection conn = dbConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            int id = 0;

            while (rs.next()) {
                id = rs.getInt("userid");

                User user = null;
                if (rs.getBoolean("buyer")) {
                    if (users.get("buyer" + id) == null) {
                        user = new Buyer(rs.getString("username"), rs.getString("email"));
                        user.setUserID("buyer" + id);
                    } else user = users.get("buyer" + id);

                } else {
                    if (users.get("renter" + id) == null) {
                        user = new Renter(rs.getString("username"), rs.getString("email"), rs.getDouble("income"), rs.getString("occupation"));
                        user.setUserID("renter" + id);
                    } else user = users.get("renter" + id);
                }
//                System.out.println(user.getUsername());
                ((Customer) user).getPreferredSuburbs().add(rs.getString("suburb"));
                users.putIfAbsent(user.getUserID(), user);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return users;
    }

    public Map<String, User> getEmployees() {
        Map<String, User> users = new HashMap<>();

        String sql = "SELECT u.userid, username, email, hiredate, etype, salary, parttime FROM users u JOIN employees e ON u.userid=e.userid";

        try (Connection conn = new DBConnector().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                User employee = null;
                switch (rs.getString("etype")) {
                    case "admin":
                        employee = new BranchAdmin(rs.getString("username"), rs.getString("email"), rs.getDate("hiredate").toLocalDate(), rs.getDouble("salary"));
                        break;
                    case "manager":
                        employee = new BranchManager(rs.getString("username"), rs.getString("email"), rs.getDate("hiredate").toLocalDate(), rs.getDouble("salary"));
                        break;
                    case "propertymanager":
                        employee = new PropertyManager(rs.getString("username"), rs.getString("email"), rs.getDate("hiredate").toLocalDate(), rs.getDouble("salary"));
                        //set assigned properties
                        ((SalesPerson) employee).setAssignedProperties(propertyDBModel.getAssignedProperties(rs.getInt("userid")));

                        //set scheduled inspections
                        break;
                    case "salesconsultant":
                        employee = new SalesConsultant(rs.getString("username"), rs.getString("email"), rs.getDate("hiredate").toLocalDate(), rs.getDouble("salary"));
                        //set assigned properties
                        ((SalesPerson) employee).setAssignedProperties(propertyDBModel.getAssignedProperties(rs.getInt("userid")));

                        //set scheduled inspections
                        break;
                    default:
                        employee = null;
                }
                if (employee != null) {
                    if (rs.getBoolean("parttime"))
                        ((Employee) employee).setPartTimeEmployee(new PartTimeEmployee());
                    employee.setUserID(rs.getString("etype") + rs.getInt("userid"));
                    users.putIfAbsent(employee.getUserID(), employee);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return users;
    }

    public Map<String, User> getPropertyOwners() {
        Map<String, User> users = new HashMap<>();

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
//                System.out.println(propertyOwner.getUsername());
                users.putIfAbsent(propertyOwner.getUserID(), propertyOwner);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return users;
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
        } catch (Exception ex) {
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
        } catch (Exception ex) {
        }

        if (found) {
            sql = "UPDATE users SET lastlogin=? WHERE userid=?";
            try (Connection conn = dbConnector.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now(ZoneId.systemDefault())));
                pstmt.setInt(2, id);
                pstmt.executeUpdate();
            } catch (Exception ex) {
            }
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

    public void registerCustomer(String username, String email, String password, String occupation, String income, List<String> preferredSuburbs) throws SQLException {
        int id = 0;

        String sql = "INSERT INTO users (username, email, password) VALUES(?, ?, ?)";
        Connection conn = dbConnector.getConnection();
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, username);
            pstmt.setString(2, email);
            pstmt.setString(3, hash(password));
//            pstmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.MIN));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (pstmt != null) {
            ResultSet rs = pstmt.getGeneratedKeys();

            if (rs.next()) {
                id = rs.getInt(1);

                sql = "INSERT INTO customers (userid, buyer, income, occupation) VALUES (?, ?, ?, ?)";
                pstmt = conn.prepareStatement(sql);

                pstmt.setInt(1, id);
                pstmt.setBoolean(2, occupation.equals("") && income.equals(""));

                Double incomeVal = null;
                if (!income.equals("")) incomeVal = Double.parseDouble(income);

                pstmt.setObject(3, incomeVal, Types.DOUBLE);
                pstmt.setString(4, occupation);
                pstmt.executeUpdate();

                updatePreferredSuburbs(id, preferredSuburbs);
            }
        }
    }

    private void updatePreferredSuburbs(int id, List<String> preferredSuburbs) throws SQLException {
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
    }

    //MUST CALL canLogin() BEFORE
    public String loginType(int id) {
        String type = "";
        boolean found = false;

        if (getCustomers().containsKey("buyer" + id))
            type = "buyer";
        else if (getCustomers().containsKey("renter" + id))
            type = "renter";
        else if (getPropertyOwners().containsKey("landlord" + id))
            type = "landlord";
        else if (getPropertyOwners().containsKey("vendor" + id))
            type = "vendor";
        else {
            if (getEmployees().size() > 0) {
                List<String> userKeys = new ArrayList<>(getEmployees().keySet());
                int i = 0;
                do {
                    String key = userKeys.get(i);
                    User u = getEmployees().get(key);
                    if (u.getUserID().contains(id + "")) {
                        type = u.getUserID().replaceAll("\\d", "");
                        found = true;
                    }
                    i++;
                } while (!found);
            }
        }
//        List<String> userKeys = new ArrayList<>(getCustomers().keySet());
//        int i = 0;
//        do {
//            String key = userKeys.get(i);
//            User u = getCustomers().get(key);
//            if (u.getUsername().equals(username)) {
//                isCustomer = true;
//                type = u.getUserID().replaceAll("\\d", "");
//                found = true;
//            }
//            i++;
//        } while (!found);
//
//        if (!isCustomer) {
//            userKeys = new ArrayList<>(getPropertyOwners().keySet());
//            i = 0;
//            do {
//                String key = userKeys.get(i);
//                User u = getPropertyOwners().get(key);
//                if (u.getUsername().equals(username)) {
//                    isPropertyOwner = true;
//                    type = u.getUserID().replaceAll("\\d", "");
//                    found = true;
//                }
//                i++;
//            } while (!found);
//        }
//
//        if (!isCustomer && !isPropertyOwner) {
//            userKeys = new ArrayList<>(getEmployees().keySet());
//            i = 0;
//            do {
//                String key = userKeys.get(i);
//                User u = getEmployees().get(key);
//                if (u.getUsername().equals(username)) {
//                    type = u.getUserID().replaceAll("\\d", "");
//                    found = true;
//                }
//                i++;
//            } while (!found);
//        }

        return type;
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

        return id;
    }

    public int getUserID(String username) {
        int id = 0;

        String sql = "SELECT userid FROM users WHERE username='" + username + "'";
        try (Connection conn = dbConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next())
                id = rs.getInt("userid");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return id;
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

    public void updateCustomerDetails(String username, String email, String occupation, String income, List<String> preferredSuburbs, String oldUsername) throws SQLException {
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

        updatePreferredSuburbs(id, preferredSuburbs);
    }

    //needed?
    public void deleteUser(String username) {


    }

    public void registerPropertyOwner(String username, String email, String password, boolean vendor) throws SQLException {
        int id = 0;

        String sql = "INSERT INTO users (username, email, password) VALUES(?, ?, ?)";
        Connection conn = dbConnector.getConnection();
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
                id = rs.getInt(1);

                sql = "INSERT INTO propertyowners (userid, vendor) VALUES (?, ?)";
                pstmt = conn.prepareStatement(sql);

                pstmt.setInt(1, id);
                pstmt.setBoolean(2, vendor);
                pstmt.executeUpdate();
            }
        }
    }

    public void registerEmployee(String username, String email, String password, double salary, LocalDate hireDate, String etype, boolean parttime) throws SQLException {
        int id = 0;

        String sql = "INSERT INTO users (username, email, password) VALUES(?, ?, ?)";
        Connection conn = dbConnector.getConnection();
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
                id = rs.getInt(1);

                sql = "INSERT INTO employees (userid, salary, hiredate, etype, parttime) VALUES (?, ?, ?, ?, ?)";
                pstmt = conn.prepareStatement(sql);

                pstmt.setInt(1, id);
                pstmt.setDouble(2, salary);
                pstmt.setDate(3, Date.valueOf(hireDate));
                pstmt.setString(4, etype);
                pstmt.setBoolean(5, parttime);
                pstmt.executeUpdate();
            }
        }
    }

    public Map<String, User> getSalesPersons() {
        Map<String, User> salespersons = new HashMap<>();

        for (String key : getEmployees().keySet()) {
            if (key.startsWith("sales") || key.startsWith("property"))
                salespersons.putIfAbsent(key, getEmployees().get(key));
        }

        return salespersons;
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
        boolean found = false;
        User user = null;
        Map<String, User> map = getSalesPersons();

        Iterator iter = map.entrySet().iterator();
        while (!found && iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();

            SalesPerson salesPerson = (SalesPerson) entry.getValue();
//            Iterator<Property> iterator = salesPerson.getAssignedProperties().values().iterator();
//
//            while (!found && iterator.hasNext()) {
//                System.out.println("value= " + iterator.next());
//                if (p.getPropertyID().equals(iterator.next().getPropertyID())) {
//                    found = true;
//                    user = salesPerson;
//                }
//            }

            for (Property assigned : salesPerson.getAssignedProperties().values()) {
                if (p.getPropertyID().equals(assigned.getPropertyID())) {
                    found = true;
                    user = salesPerson;
                }
//                System.out.println("Salesperson " + salesPerson.getUsername() + " is assigned to property " + assigned.getPropertyID());
//                System.out.println(assigned.getPropertyID().equals(p.getPropertyID()));
            }
//            if (salesPerson.getAssignedProperties().containsValue(p)) {
//                found = true;
//                user = salesPerson;
//            }
        }

        return user;
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
}