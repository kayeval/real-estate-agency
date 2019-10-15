package main.model;

import main.model.Property.*;
import main.model.Proposal.ContractDuration;
import main.model.User.PropertyOwner.PropertyOwner;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class PropertyDBModel {
    private DBConnector dbConnector;
    private UserDBModel userDBModel;

    public PropertyDBModel() {
        dbConnector = new DBConnector();
    }

    public Map<String, Property> getProperties() {
        Map<String, Property> properties = new HashMap<>();

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
                            price, (PropertyOwner) userDBModel.getPropertyOwners().get("landlord" + propertyOwnerID), contractDurationSet);
                    p.setPropertyID("rental" + id);
                    p.setDocumentsInspected(inspected);
                    p.setActive(active);
                    properties.putIfAbsent(p.getPropertyID(), p);
                } else {
                    Property p = new SaleProperty(address, suburb, capacity, PropertyType.valueOf(propertyType.toUpperCase()), price,
                            (PropertyOwner) userDBModel.getPropertyOwners().get("vendor" + propertyOwnerID));
                    p.setPropertyID("sale" + id);
                    properties.putIfAbsent(p.getPropertyID(), p);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return properties;
    }

    public void addProperty(String address, String suburb, PropertyType propertyType, int baths, int cars, int beds, double price,
                            Set<ContractDuration> contractDurations, int userID) {

        String sql = "INSERT INTO properties (price, listeddate, active, address, " +
                "suburb, beds, baths, carspaces, " +
                "propertytype, listedby, rental, contractdurations, inspected) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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

            pstmt.setBoolean(12, false);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
    }

    public Map<String, Property> getSales() {
        Map<String, Property> sales = new HashMap<>();
        for (String key : getProperties().keySet()) {
            if (key.contains("sale")) sales.putIfAbsent(key, getProperties().get(key));
        }
        return sales;
    }

    public Map<String, Property> getRentals() {
        Map<String, Property> rentals = new HashMap<>();
        for (String key : getProperties().keySet()) {
            if (key.contains("rental")) rentals.putIfAbsent(key, getProperties().get(key));
        }
        return rentals;
    }

    public Map<String, Property> getAssignedProperties(int userID) {
        Map<String, Property> properties = new HashMap<>();
        int propertyID = 0;

        String sql = "SELECT * from properties WHERE assignedto=" + userID;
        try (Connection conn = dbConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                propertyID = rs.getInt("propertyid");
                if (rs.getBoolean("rental")) {
                    properties.putIfAbsent("rental" + propertyID, getProperties().get("rental" + propertyID));
                } else properties.putIfAbsent("sale" + propertyID, getProperties().get("sale" + propertyID));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return properties;
    }

    public void documentsInspected(String propertyID) {

    }

    public void assignProperty(String propertyID, String userID) {
        //remove non numeric from IDs
        int property = Integer.parseInt(propertyID.replaceAll("[^\\d.]", ""));
        int user = Integer.parseInt(userID.replaceAll("[^\\d.]", ""));

        String sql = "UPDATE properties SET assignedto=? WHERE propertyid=?";
        try (Connection connection = dbConnector.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, user);
            pstmt.setInt(2, property);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setUserDBModel(UserDBModel userDBModel) {
        this.userDBModel = userDBModel;
    }

    public Map<String, Property> getProperties(String type, int userID) {
        Map<String, Property> properties = new HashMap<>();

        if (type.equals("vendor") || type.equals("landlord"))
            for (String key : getProperties().keySet()) {
                if (getProperties().get(key).getPropertyOwner().getUserID().contains(userID + ""))
                    properties.putIfAbsent(key, getProperties().get(key));
            }
        else if (type.equals("salesconsultant") || type.equals("propertymanager")) {
            properties = getAssignedProperties(userID);
        }

        return properties;
    }

    public Map<String, Property> getPendingProperties() {
        Map<String, Property> properties = new HashMap<>();

        for (Property p : getProperties().values())
            if (!p.areDocumentsInspected())
                properties.putIfAbsent(p.getPropertyID(), p);

        return properties;
    }

    public Map<String, Property> getActiveProperties() {
        Map<String, Property> properties = new HashMap<>();

        for (Property p : getProperties().values()) {
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

        for (Property p : getProperties().values()) {
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

        for (Property p : getActiveProperties().values())
            if (p.getPropertyID().startsWith("sales"))
                properties.putIfAbsent(p.getPropertyID(), p);

        return properties;
    }

    public Map<String, Property> getActiveRentals() {
        Map<String, Property> properties = new HashMap<>();

        for (Property p : getActiveProperties().values())
            if (!p.getPropertyID().startsWith("sales"))
                properties.putIfAbsent(p.getPropertyID(), p);

        return properties;
    }

    public Map<String, Property> getInactiveProperties(String registeredUserType, int userID) {
        Map<String, Property> properties = new HashMap<>();

        if (registeredUserType.equals("buyer") || registeredUserType.equals("vendor")) {
            for (Property p : getProperties(registeredUserType, userID).values())
                if (p.getStatus().equals("Inactive"))
                    properties.putIfAbsent(p.getPropertyID(), p);
        } else if (registeredUserType.equals("salesconsultant") || registeredUserType.equals("propertymanager"))
            for (Property p : getAssignedProperties(userID).values())
                if (p.getStatus().equals("Inactive"))
                    properties.putIfAbsent(p.getPropertyID(), p);

        return properties;
    }

    public Map<String, Property> getPendingProperties(String registeredUserType, int userID) {
        Map<String, Property> properties = new HashMap<>();

        if (registeredUserType.equals("buyer") || registeredUserType.equals("vendor")) {
            for (Property p : getProperties(registeredUserType, userID).values())
                if (p.getStatus().equals("Pending"))
                    properties.putIfAbsent(p.getPropertyID(), p);
        } else if (registeredUserType.equals("salesconsultant") || registeredUserType.equals("propertymanager"))
            for (Property p : getAssignedProperties(userID).values())
                if (p.getStatus().equals("Pending"))
                    properties.putIfAbsent(p.getPropertyID(), p);

        return properties;
    }

    public Map<String, Property> getActiveProperties(String registeredUserType, int userID) {
        Map<String, Property> properties = new HashMap<>();
        if (registeredUserType.equals("buyer") || registeredUserType.equals("vendor")) {
            for (Property p : getProperties(registeredUserType, userID).values())
                if (p.getStatus().equals("Active"))
                    properties.putIfAbsent(p.getPropertyID(), p);
        } else if (registeredUserType.equals("salesconsultant") || registeredUserType.equals("propertymanager"))
            for (Property p : getAssignedProperties(userID).values())
                if (p.getStatus().equals("Active"))
                    properties.putIfAbsent(p.getPropertyID(), p);

        return properties;
    }

}
