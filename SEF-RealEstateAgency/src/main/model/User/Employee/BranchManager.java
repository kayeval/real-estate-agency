package main.model.User.Employee;

import main.model.Property.Property;
import main.model.User.Employee.SalesPerson.SalesPerson;
import main.model.User.InvalidEmailException;

import java.time.LocalDate;

public class BranchManager extends Employee {

    public BranchManager(String username, String email, LocalDate hireDate, double salary) throws InvalidEmailException {
        super(username, email, hireDate, salary);
    }

    public boolean hasHours(PartTimeEmployee partTimeEmployee) throws HoursNotFoundException {
        if (partTimeEmployee.getHoursForMonth(LocalDate.now()) == 0.0) {
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
        ((SalesPerson) employee).assignProperty(property);
    }

    public boolean inspectDocuments(Property property) {
        if (!property.areDocumentsInspected())
        {
            property.setDocumentsInspected(true);
        }
        return true;
    }
}
 