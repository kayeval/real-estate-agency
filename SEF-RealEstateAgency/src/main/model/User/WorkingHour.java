package main.model.User;

import java.time.LocalDate;

public class WorkingHour {
    private String id;
    private LocalDate date;
    private double hours;
    private boolean approved;

    public WorkingHour(LocalDate date, double hours) {
        this.date = date;
        this.hours = hours;
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getID() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getHours() {
        return hours;
    }

    public String getStatus() {
        String status = "";

        if (approved)
            status = "Approved";
        else status = "Pending";

        return status;
    }
}
