package main.model.User.Employee.SalesPerson;

import main.model.User.InvalidEmailException;

public class SalesConsultant extends SalesPerson {

    public SalesConsultant(String name, String email) throws InvalidEmailException {
        super(name, email);
    }
    
    /*
    public boolean viewMaintainenceReport(Property property)
    {
    	return false;
    }
    */

}
