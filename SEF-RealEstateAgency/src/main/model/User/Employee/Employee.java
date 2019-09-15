package main.model.User.Employee;

import main.model.User.InvalidEmailException;
import main.model.User.User;

public abstract class Employee extends User {
    private String hireDate;
    private double salary;

    public Employee(String name, String email) throws InvalidEmailException {
        super(name, email);
    }

    public String getHireDuration() {
        //requires logic to work out current date from hireDate
        return hireDate;
    }

    public double getSalary() {
        return salary;
    }

}
