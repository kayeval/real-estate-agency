package main.model.User.Employee.SalesPerson;

import main.model.User.InvalidEmailException;

import java.time.LocalDate;

public class SalesConsultant extends SalesPerson {

    public SalesConsultant(String username, String email, LocalDate hireDate, double salary) throws InvalidEmailException {
        super(username, email, hireDate, salary);
    }
    
    /*
    public boolean viewMaintainenceReport(Property property)
    {
    	return false;
    }
    */

}
