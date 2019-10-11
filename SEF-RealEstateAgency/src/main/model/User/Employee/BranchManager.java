package main.model.User.Employee;

import java.time.LocalDate;

import main.model.Property.Property;
import main.model.User.Employee.SalesPerson.SalesPerson;
import main.model.User.InvalidEmailException;

public class BranchManager extends Employee {

    public BranchManager(String name, String email, double salary) throws InvalidEmailException {
        super(name, email, salary);
    }

    public boolean hasHours(PartTimeEmployee partTimeEmployee) throws HoursNotFoundException {
    	if (partTimeEmployee.getHoursForMonth(LocalDate.now()) == 0.0)
    	{
    		throw new HoursNotFoundException();
    	}
    	return false;
    }

    public boolean reviewHours(PartTimeEmployee partTimeEmployee, boolean approval) {
    	partTimeEmployee.setHoursApproved(approval);
    	return true;
    }
    
    
    /*
    public boolean disapproveHours(PartTimeEmployee partTimeEmployee) {
    	//view hours 
    	//if hoursApproved == true
    	if (partTimeEmployee.isHoursApproved())
    	{
        	partTimeEmployee.setHoursApproved(false);
        	//successfully approved 
        	return true;
    	}
        return false;
    }
    */

    public void assignProperty(Employee employee, Property property) {
        ((SalesPerson) employee).addProperty(property);
    }

    public boolean inspectDocuments(Property property) {
        return false;

    }


}
 