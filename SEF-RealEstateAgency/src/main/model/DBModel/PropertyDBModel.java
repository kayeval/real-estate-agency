package main.model.DBModel;

import main.model.Property.*;
import main.model.Proposal.ContractDuration;
import main.model.User.Employee.SalesPerson.SalesPerson;
import main.model.User.PropertyOwner.PropertyOwner;

import java.sql.Date;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class PropertyDBModel {
    private DBConnector dbConnector;
    private UserDBModel userDBModel;
    private Map<String, Property> allProperties;

    public PropertyDBModel() {
        dbConnector = new DBConnector();
    }

    public void loadPropertiesFromDB() {
        allProperties = getAllProperties();
    }

    public Map<String, Property> getAllProperties() {
        allProperties = new HashMap<>();

        String sql = "SELECT * from properties";
        try (Connection conn = dbConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            int id = 0;

            while (rs.next()) {
                id = rs.getInt("propertyid");
                String address = rs.getString("address");
                int baths = rs.getInt("baths");
                int cars = rs.getInt("carspaces");
                int beds = rs.getInt("beds");
                String propertyType = rs.getString("propertytype");
                double price = rs.getDouble("price");
                String suburb = rs.getString("suburb");
                boolean inspected = rs.getBoolean("inspected");
                boolean active = rs.getBoolean("active");
                int propertyOwnerID = rs.getInt("listedby");
                int assignedTo = rs.getInt("assignedto");
                LocalDateTime listedDate = rs.getTimestamp("listeddate").toLocalDateTime();
                ;

                //add list of contract durations to set
                Capacity capacity = new Capacity(cars, beds, baths);

                if (rs.getBoolean("rental")) {
                    String contractDurations = rs.getString("contractdurations");
                    Set<ContractDuration> contractDurationSet = new HashSet<>();
                    String[] split = contractDurations.split("\\s*,\\s*");

                    for (String s : split) {
                        if (s.equals("ONE_YEAR"))
                            contractDurationSet.add(ContractDuration.ONE_YEAR);
                        else if (s.equals("TWO_YEARS"))
                            contractDurationSet.add(ContractDuration.TWO_YEARS);
                        else contractDurationSet.add(ContractDuration.SIX_MONTHS);
                    }

                    Property p = new RentalProperty(address, suburb, capacity, PropertyType.valueOf(propertyType.toUpperCase()),
                            price, ((PropertyOwner) userDBModel.getUser(propertyOwnerID)), contractDurationSet);
                    p.getPropertyOwner().getListedProperties().putIfAbsent(p.getPropertyID(), p);

                    p.setPropertyID("rental" + id);
                    p.setDocumentsInspected(inspected);
                    p.setActive(active);
                    p.setDateListed(listedDate);

                    if (assignedTo != 0) {
                        p.setSalesPerson((SalesPerson) userDBModel.getUser(assignedTo));
                        p.getSalesPerson().getAssignedProperties().putIfAbsent(p.getPropertyID(), p);
                    }

                    ((RentalProperty) p).setNextMaintenance(rs.getDate("nextmaintenance").toLocalDate());
                    ((RentalProperty) p).setPreviousMaintenance(rs.getDate("prevmaintenance").toLocalDate());

                    allProperties.putIfAbsent(p.getPropertyID(), p);
                } else {
                    Property p = new SaleProperty(address, suburb, capacity, PropertyType.valueOf(propertyType.toUpperCase()), price,
                            ((PropertyOwner) userDBModel.getUser(propertyOwnerID)));
                    p.getPropertyOwner().getListedProperties().putIfAbsent(p.getPropertyID(), p);
                    p.setPropertyID("sale" + id);
                    p.setDocumentsInspected(inspected);
                    p.setActive(active);
                    p.setDateListed(listedDate);

                    if (assignedTo != 0) {
                        p.setSalesPerson((SalesPerson) userDBModel.getUser(assignedTo));
                        p.getSalesPerson().getAssignedProperties().putIfAbsent(p.getPropertyID(), p);
                    }

                    allProperties.putIfAbsent(p.getPropertyID(), p);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return allProperties;
    }

    public void addProperty(String address, String suburb, PropertyType propertyType, int baths, int cars, int beds, double price,
                            Set<ContractDuration> contractDurations, int userID) {

        String sql = "INSERT INTO properties (price, listeddate, active, address, " +
                "suburb, beds, baths, carspaces, " +
                "propertytype, listedby, rental, contractdurations, inspected, assignedto, prevmaintenance, nextmaintenance) " +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setObject(1, price, Types.DOUBLE);
            pstmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now(ZoneId.systemDefault())));
            pstmt.setBoolean(3, false);
            pstmt.setString(4, address);
            pstmt.setString(5, suburb);
            pstmt.setInt(6, beds);
            pstmt.setInt(7, baths);
            pstmt.setInt(8, cars);
            pstmt.setString(9, propertyType.toString());
            pstmt.setInt(10, userID);
            pstmt.setBoolean(11, contractDurations != null);

            if (contractDurations != null) {
                ArrayList<String> acceptedContractDurations = new ArrayList<>();
                for (ContractDuration contractDuration : contractDurations)
                    acceptedContractDurations.add(contractDuration.toString());
                pstmt.setString(12, String.join(",", acceptedContractDurations));
            } else pstmt.setString(12, "");

            pstmt.setBoolean(13, false);
            pstmt.setInt(14, 0);
            if (contractDurations != null) {
                Date now = Date.valueOf(LocalDate.now(ZoneId.systemDefault()));
                pstmt.setDate(15, now);
                pstmt.setDate(16, now);
            } else {
                pstmt.setNull(15, Types.DATE);
                pstmt.setNull(16, Types.DATE);
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        loadPropertiesFromDB();
    }

    public void updatePropertyDetails(String address, String suburb, PropertyType propertyType, int baths, int cars, int beds, double price,
                                      Set<ContractDuration> contractDurations, int userID, int propertyID) {
        String sql = "UPDATE properties SET address=?, suburb=?, propertytype=?, baths=?, carspaces=?, beds=?, price=?, listedby=?, contractdurations=? WHERE propertyid=?";

        try (Connection connection = dbConnector.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, address);
            pstmt.setString(2, suburb);
            pstmt.setString(3, propertyType.toString());
            pstmt.setInt(4, baths);
            pstmt.setInt(5, cars);
            pstmt.setInt(6, beds);
            pstmt.setDouble(7, price);
            pstmt.setInt(8, userID);

            if (contractDurations != null) {
                ArrayList<String> acceptedContractDurations = new ArrayList<>();
                for (ContractDuration contractDuration : contractDurations)
                    acceptedContractDurations.add(contractDuration.toString());
                pstmt.setString(9, String.join(",", acceptedContractDurations));
            } else pstmt.setString(9, "");

            pstmt.setInt(10, propertyID);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        loadPropertiesFromDB();
    }

    public Map<String, Property> getSales() {
        Map<String, Property> sales = new HashMap<>();
        for (String key : allProperties.keySet()) {
            if (key.contains("sale")) sales.putIfAbsent(key, allProperties.get(key));
        }
        return sales;
    }

    public Map<String, Property> getRentals() {
        Map<String, Property> rentals = new HashMap<>();
        for (String key : allProperties.keySet()) {
            if (key.contains("rental")) rentals.putIfAbsent(key, allProperties.get(key));
        }
        return rentals;
    }

//    public Map<String, Property> getAssignedProperties(int userID) {
//        Map<String, Property> properties = new HashMap<>();
//        int propertyID = 0;
//
//        String sql = "SELECT * from properties WHERE assignedto=" + userID;
//        try (Connection conn = dbConnector.getConnection();
//             Statement stmt = conn.createStatement();
//             ResultSet rs = stmt.executeQuery(sql)) {
//
//            while (rs.next()) {
//                propertyID = rs.getInt("propertyid");
//                if (rs.getBoolean("rental")) {
//                    properties.putIfAbsent("rental" + propertyID, properties.get("rental" + propertyID));
//                } else properties.putIfAbsent("sale" + propertyID, properties.get("sale" + propertyID));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return properties;
//    }

    public void setDocumentsInspected(Property property) {
        String sql = "UPDATE properties SET inspected=?, active=? WHERE propertyid=?";
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setBoolean(1, true);
            pstmt.setBoolean(2, true);
            pstmt.setInt(3, Integer.parseInt(property.getPropertyID().replaceAll("[^\\d.]", "")));

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        loadPropertiesFromDB();
    }

    public void deactivateListing(Property property) {
        String sql = "UPDATE properties SET active=? WHERE propertyid=?";
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setBoolean(1, false);
            pstmt.setInt(2, Integer.parseInt(property.getPropertyID().replaceAll("[^\\d.]", "")));

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        allProperties.get(property.getPropertyID()).deactivate();
    }

    public void assignProperty(String propertyID, String userID) {
        String sql = "UPDATE properties SET assignedto=? WHERE propertyid=?";
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, Integer.parseInt(userID.replaceAll("[^\\d.]", "")));
            pstmt.setInt(2, Integer.parseInt(propertyID.replaceAll("[^\\d.]", "")));

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        allProperties.get(propertyID).setSalesPerson((SalesPerson) userDBModel.getUser(Integer.parseInt(userID.replaceAll("[^\\d.]", ""))));
    }

    public void setUserDBModel(UserDBModel userDBModel) {
        this.userDBModel = userDBModel;

        loadPropertiesFromDB();
    }

    public Map<String, Property> getPendingProperties() {
        Map<String, Property> properties = new HashMap<>();

        for (Property p : allProperties.values())
            if (!p.areDocumentsInspected())
                properties.putIfAbsent(p.getPropertyID(), p);

        return properties;
    }

    public Map<String, Property> getActiveProperties() {
        Map<String, Property> properties = new HashMap<>();

        for (Property p : allProperties.values()) {
            try {
                if (p.isActive())
                    properties.putIfAbsent(p.getPropertyID(), p);
            } catch (DeactivatedPropertyException e) {
//                e.printStackTrace();
            }
        }

        return properties;
    }

    public Map<String, Property> getInactiveProperties() {
        Map<String, Property> properties = new HashMap<>();

        for (Property p : allProperties.values()) {
            try {
                if (!p.isActive())
                    properties.putIfAbsent(p.getPropertyID(), p);
            } catch (DeactivatedPropertyException e) {
//                e.printStackTrace();
            }
        }

        return properties;
    }

    public Map<String, Property> getActiveSales() {
        Map<String, Property> properties = new HashMap<>();

        for (Property p : getSales().values()) {
            try {
                if (p.isActive())
                    properties.putIfAbsent(p.getPropertyID(), p);
            } catch (DeactivatedPropertyException e) {
//                e.printStackTrace();
            }
        }

        return properties;
    }

    public Map<String, Property> getActiveRentals() {
        Map<String, Property> properties = new HashMap<>();

        for (Property p : getRentals().values()) {
            try {
                if (p.isActive())
                    properties.putIfAbsent(p.getPropertyID(), p);
            } catch (DeactivatedPropertyException e) {

            }
        }

        return properties;
    }

    public Map<String, Property> getProperties(String registeredUserType, int userID) {
        Map<String, Property> properties = new HashMap<>();

        if (registeredUserType.equals("vendor") || registeredUserType.equals("landlord")) {
            for (Property p : allProperties.values())
                if ((Integer.parseInt(p.getPropertyOwner().getUserID().replaceAll("[^\\d.]", ""))) == userID)
                    properties.putIfAbsent(p.getPropertyID(), p);
        } else if (registeredUserType.equals("salesconsultant") || registeredUserType.equals("propertymanager")) {
            for (Property p : allProperties.values())
                if (p.getSalesPerson() != null && p.getSalesPerson().getUserID().contains(userID + ""))
                    properties.putIfAbsent(p.getPropertyID(), p);
        }

        return properties;
    }

    public Map<String, Property> getInactiveProperties(String registeredUserType, int userID) {
        Map<String, Property> properties = new HashMap<>();

        if (registeredUserType.equals("landlord") || registeredUserType.equals("vendor")
                || registeredUserType.equals("salesconsultant") || registeredUserType.equals("propertymanager")) {
            for (Property p : getProperties(registeredUserType, userID).values()) {
                if (p.getStatus().equals("Inactive"))
                    properties.putIfAbsent(p.getPropertyID(), p);
            }
        } else
            for (Property p : allProperties.values())
                if (p.getStatus().equals("Inactive"))
                    properties.putIfAbsent(p.getPropertyID(), p);

        return properties;
    }

    public Map<String, Property> getPendingProperties(String registeredUserType, int userID) {
        Map<String, Property> properties = new HashMap<>();

        if (registeredUserType.equals("landlord") || registeredUserType.equals("vendor")
                || registeredUserType.equals("salesconsultant") || registeredUserType.equals("propertymanager")) {
            for (Property p : getProperties(registeredUserType, userID).values()) {
                if (p.getStatus().equals("Pending"))
                    properties.putIfAbsent(p.getPropertyID(), p);
            }
        } else
            for (Property p : allProperties.values())
                if (p.getStatus().equals("Pending"))
                    properties.putIfAbsent(p.getPropertyID(), p);


        return properties;
    }

    public Map<String, Property> getActiveProperties(String registeredUserType, int userID) {
        Map<String, Property> properties = new HashMap<>();

        if (registeredUserType.equals("landlord") || registeredUserType.equals("vendor")
                || registeredUserType.equals("salesconsultant") || registeredUserType.equals("propertymanager")) {
            for (Property p : getProperties(registeredUserType, userID).values()) {
                if (p.getStatus().equals("Active"))
                    properties.putIfAbsent(p.getPropertyID(), p);
            }

        } else
            for (Property p : allProperties.values())
                if (p.getStatus().equals("Active"))
                    properties.putIfAbsent(p.getPropertyID(), p);

        return properties;
    }

    public void updateMaintenance(LocalDate nextMaintenance, Property property) {
        String sql = "UPDATE properties SET prevmaintenance=?, nextmaintenance=? WHERE propertyid=?";

        try (Connection connection = dbConnector.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(((RentalProperty) property).getNextMaintenance()));
            pstmt.setDate(2, Date.valueOf(nextMaintenance));
            pstmt.setInt(3, Integer.parseInt(property.getPropertyID().replaceAll("[^\\d.]", "")));

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ((RentalProperty) allProperties.get(property.getPropertyID())).setPreviousMaintenance(((RentalProperty) property).getNextMaintenance());
        ((RentalProperty) allProperties.get(property.getPropertyID())).setNextMaintenance(nextMaintenance);
    }

    public Property getProperty(int id) {
        Property property = null;

        for (Map.Entry<String, Property> e : allProperties.entrySet()) {
            if (e.getKey().contains(id + "")) {
                //add to my result list
                property = e.getValue();
            }
        }

        return property;
    }
}
