package main.model.User.Employee;

import java.util.ArrayList;
import java.util.HashMap;
import main.model.BankAccount;
import main.model.Property.Property;
import main.model.User.PropertyOwner.PropertyOwner;

public class BranchAdmin extends Employee {

    public BranchAdmin(String name, String email) {
        super(name, email);
    }
    
    public boolean receiveDocuments(String document)
    {
    	return false;
    }
    public String scanDocuments()
    {
    	return null;
    }
    public boolean collectRent(Property property, BankAccount account)
    {
    	return false;
    }
    public double calculateManagementFee(ArrayList<PropertyOwner> propertyOwners, ArrayList<Property> listings)
    {
    	//needs logic
    	return (Double) null;
    }
    public boolean runPayroll(HashMap<String, Employee> employees, HashMap<String, Property> listings, BankAccount account)
    {
    	payEmployees(employees);
    	payCommission(listings, account);
    	payLandlords(listings);
    	return false;
    }
    public boolean payEmployees(HashMap<String, Employee> employees)
    {
    	return false;
    }
    public boolean payCommission(HashMap<String, Property> listings, BankAccount account)
    {
    	return false;
    }
    public boolean payLandlords(HashMap<String, Property> listings)
    {
    	return false;
    }
    public boolean creditPropertyManagement(HashMap<String, Property> listings, BankAccount account)
    {
    	return false;
    }
    

}
