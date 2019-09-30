package main.model.User.Employee.SalesPerson;

import main.model.User.InvalidEmailException;

public class SalesConsultant extends SalesPerson {

    public SalesConsultant(String name, String email, double salary) throws InvalidEmailException {
        super(name, email, salary);
    }
    
    /*
    public boolean viewMaintainenceReport(Property property)
    {
    	return false;
    }
    */

}
