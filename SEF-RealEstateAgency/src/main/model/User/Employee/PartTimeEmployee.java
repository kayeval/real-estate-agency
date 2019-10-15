package main.model.User.Employee;

import main.model.User.WorkingHour;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

public class PartTimeEmployee {
    private Map<String, WorkingHour> workingHours;
    private boolean hoursApproved = false;

    public PartTimeEmployee() {
        workingHours = new HashMap<>();
    }

    public void inputHours(WorkingHour workingHour) {
        WorkingHour w = workingHours.putIfAbsent(workingHour.getID(), workingHour);

        if (w != null) {
            workingHours.replace(workingHour.getID(), workingHour);
        }
    }

    public double getHoursForMonth(LocalDate date) {
        double hours = .0;
        Month month = date.getMonth();
        String id;

        for (Map.Entry<String, WorkingHour> currentEntry : workingHours.entrySet()) {
            id = currentEntry.getKey();
            if (workingHours.get(id).getDate().getMonth() == month) {
                hours += currentEntry.getValue().getHours();
            }
        }

        return hours;
    }

    public Map<String, WorkingHour> getWorkingHours() {
        return workingHours;
    }

    public boolean isHoursApproved() {
        return hoursApproved;
    }

    public void setHoursApproved(boolean hoursApproved) {
        this.hoursApproved = hoursApproved;
    }
}
