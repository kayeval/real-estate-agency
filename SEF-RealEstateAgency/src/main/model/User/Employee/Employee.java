package main.model.User.Employee;

import main.model.User.InvalidEmailException;
import main.model.User.User;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;

public abstract class Employee extends User {
    private LocalDate hireDate;
    private double salary;

    public Employee(String name, String email, LocalDate hireDate, double salary) throws InvalidEmailException {
        super(name, email);
        this.hireDate = hireDate;
        setSalary(salary);
    }

    public Period getHireDuration() {
        //requires logic to work out current date from hireDate
        return Period.between(hireDate, LocalDate.now(ZoneId.systemDefault()));
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public double getSalary() {
        return salary;
    }

}
