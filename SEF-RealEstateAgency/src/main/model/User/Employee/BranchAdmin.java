package main.model.User.Employee;

import main.model.BankAccount;
import main.model.Property.Property;
import main.model.User.InvalidEmailException;
import main.model.User.PropertyOwner.PropertyOwner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class BranchAdmin extends Employee {

    public BranchAdmin(String username, String email, LocalDate hireDate, double salary) throws InvalidEmailException {
        super(username, email, hireDate, salary);
    }

    public boolean receiveDocuments(String document) {
        return false;
    }

    public String scanDocuments() {
        return null;
    }

    public boolean collectRent(Property property, BankAccount account) {
        return false;
    }

    public double calculateManagementFee(ArrayList<PropertyOwner> propertyOwners, ArrayList<Property> listings) {
        //needs logic
        return (Double) null;
    }

    public boolean runPayroll(HashMap<String, Employee> employees, HashMap<String, Property> listings, BankAccount account) {
        //payEmployees(employees);
        payCommission(listings, account);
        payLandlords(listings);
        return false;
    }

    public boolean payAllEmployees(HashMap<String, Employee> employees) {

        for (Entry<String, Employee> entry : employees.entrySet()) {
            try {
                payEmployee(entry.getValue());
            } catch (HoursNotFoundException | HoursNotApprovedException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    public boolean payEmployee(Employee employee) throws HoursNotFoundException, HoursNotApprovedException {
        boolean valid = true;

        if (employee.getPartTimeEmployee() != null) {
            if (employee.getPartTimeEmployee().getHoursForMonth(LocalDate.now()) <= 0.0) {
                throw new HoursNotFoundException();
            }

            if (employee.getPartTimeEmployee().isHoursApproved() == false) {
                throw new HoursNotApprovedException();
            }

            double salary = employee.getSalary();
            double fullTimeHours = 152.00; //TODO: CHANGE?

            if (employee.getPartTimeEmployee().isHoursApproved()) {
                salary *= (employee.getPartTimeEmployee().getHoursForMonth(LocalDate.now()) / fullTimeHours);
                //payroll functionality
                valid = true;
            }
        } else {
            double salary = employee.getSalary();
            //payroll functionality
        }

        return valid;
    }

    public boolean payCommission(HashMap<String, Property> listings, BankAccount account) {
        return false;
    }

    public boolean payLandlords(HashMap<String, Property> listings) {
        return false;
    }

    public boolean creditPropertyManagement(HashMap<String, Property> listings, BankAccount account) {
        return false;
    }


}
