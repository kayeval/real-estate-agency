package main.model.User.Employee.SalesPerson;

import main.model.User.InvalidEmailException;

import java.time.LocalDate;

public class PropertyManager extends SalesPerson {

    public PropertyManager(String username, String email, LocalDate hireDate, double salary) throws InvalidEmailException {
        super(username, email, hireDate, salary);
    }
    
    /*
    public boolean viewLegalDocuments(Property property)
    {
    	return false;
    }
    */

}
