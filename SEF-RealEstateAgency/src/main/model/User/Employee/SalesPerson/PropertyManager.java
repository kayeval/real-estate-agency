package main.model.User.Employee.SalesPerson;

import main.model.User.InvalidEmailException;

public class PropertyManager extends SalesPerson {

    public PropertyManager(String name, String email, double salary) throws InvalidEmailException {
        super(name, email, salary);
    }
    
    /*
    public boolean viewLegalDocuments(Property property)
    {
    	return false;
    }
    */

}
