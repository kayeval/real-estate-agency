package main.model.User.Employee;

import main.model.User.InvalidEmailException;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

public class PartTimeEmployee extends Employee {
    private Map<LocalDate, Double> workingHours;
    private boolean hoursApproved = false;

    public PartTimeEmployee(String name, String email, double salary) throws InvalidEmailException {
        super(name, email, salary);
        workingHours = new HashMap<>();
    }

    public void inputHours(LocalDate date, double hours) {
        Double h = workingHours.putIfAbsent(date, hours);

        if (h != null) {
            workingHours.replace(date, hours);
        }
    }

    public double getHoursForMonth(LocalDate date) {
        double hours = .0;
        Month month = date.getMonth();
        LocalDate dateEntry;

        for (Map.Entry<LocalDate, Double> currentEntry : workingHours.entrySet()) {
            dateEntry = (LocalDate) currentEntry.getKey();
            if (dateEntry.getMonth() == month) {
                hours += (double) currentEntry.getValue();
            }
        }

        return hours;
    }

    public Map<LocalDate, Double> getWorkingHours() {
        return workingHours;
    }

	public boolean isHoursApproved() {
		return hoursApproved;
	}

	public void setHoursApproved(boolean hoursApproved) {
		this.hoursApproved = hoursApproved;
	}
	
	//test message 
}
