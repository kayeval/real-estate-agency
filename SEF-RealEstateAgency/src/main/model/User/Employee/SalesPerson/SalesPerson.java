package main.model.User.Employee.SalesPerson;

import main.model.Inspection;
import main.model.Property.Property;
import main.model.User.Customer.Customer;
import main.model.User.Employee.Employee;
import main.model.User.InvalidEmailException;

import java.util.HashMap;

public abstract class SalesPerson extends Employee {
    private HashMap<String, Property> assignedProperties;
    private HashMap<String, Inspection> scheduledInspections;

    public SalesPerson(String name, String email) throws InvalidEmailException {
        super(name, email);
    }

    public HashMap<String, Property> getAssignedProperties() {
        return assignedProperties;
    }

    public void addProperty(Property property) {
        assignedProperties.put(property.getPropertyID(), property);
    }

    public boolean scheduleInspection(Property property, Customer person) {
        //logic if either property or person does not exist
        return false;
    }

    public boolean deactivateListing(Property property) {
        //logic if property is null
        return false;
    }

    public void advertiseProperty(Property property) {
        //no functionality required
    }

    public boolean contactUser(String userID) {
        //contact
        return false;
    }


}
