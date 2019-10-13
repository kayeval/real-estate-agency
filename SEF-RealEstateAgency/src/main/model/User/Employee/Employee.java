package main.model.User.Employee;

import main.model.User.InvalidEmailException;
import main.model.User.User;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;

public abstract class Employee extends User {
    private LocalDate hireDate;
    private double salary;

    public Employee(String username, String email, LocalDate hireDate, double salary) throws InvalidEmailException {
        super(username, email);
        this.hireDate = hireDate;
        setSalary(salary);
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public String getType() {
        return getUserID().replaceAll("\\d", "");
    }

    public Period getHireDuration() {
        return Period.between(hireDate, LocalDate.now(ZoneId.systemDefault()));
    }

    public double getSalary() {
        return salary;
    }

    public boolean setSalary(double salary) {
        if (salary > 0) {
            this.salary = salary;
            return true;
        }
        return false;
    }

}
