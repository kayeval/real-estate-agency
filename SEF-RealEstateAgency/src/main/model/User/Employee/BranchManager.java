package main.model.User.Employee;

import main.model.Property.Property;
import main.model.User.Employee.SalesPerson.SalesPerson;
import main.model.User.InvalidEmailException;

import java.time.LocalDate;

public class BranchManager extends Employee {

    public BranchManager(String name, String email, LocalDate hireDate, double salary) throws InvalidEmailException {
        super(name, email, hireDate, salary);
    }

    public boolean approveHours(PartTimeEmployee partTimeEmployee) {
        return false;
    }

    public boolean disapproveHours(PartTimeEmployee partTimeEmployee) {
        return false;
    }

    public void assignProperty(Employee employee, Property property) {
        ((SalesPerson) employee).addProperty(property);
    }

    public boolean inspectDocuments(Property property) {
        return false;

    }


}
