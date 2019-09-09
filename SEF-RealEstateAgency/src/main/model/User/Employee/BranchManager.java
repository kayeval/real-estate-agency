package main.model.User.Employee;

import main.model.Property.Property;
import main.model.User.Employee.SalesPerson.SalesPerson;

public class BranchManager extends Employee {

    public BranchManager(String name, String email) {
    	super(name, email);
    }
    
    public boolean approveHours(PartTimeEmployee partTimeEmployee)
    {
    	return false;
    }
    public boolean disapproveHours(PartTimeEmployee partTimeEmployee)
    {
    	return false;
    }
    public void assignProperty(Employee employee, Property property)
    {
    	((SalesPerson)employee).addProperty(property);
    }
    public boolean inspectDocuments(Property property) 
    {
		return false;
    	
    }
    
    

}
