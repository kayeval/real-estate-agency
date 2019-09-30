package main.model.User.Employee;

import main.model.User.InvalidEmailException;
import main.model.User.User;

public abstract class Employee extends User {
    private String hireDate;
    private double salary;

    public Employee(String name, String email, double salary) throws InvalidEmailException {
        super(name, email);
        this.salary = salary;
    }

    public String getHireDuration() {
        //requires logic to work out current date from hireDate
        return hireDate;
    }

    public double getSalary() {
        return salary;
    }
    
    public boolean setSalary(double salary) {
    	if (salary > 0)
    	{
    		this.salary = salary;
        	return true;
    	}
    	return false;
    }

}
