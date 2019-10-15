package main.model.User;

import java.time.LocalDate;

public class WorkingHour {
    private int id;
    private LocalDate date;
    private double hours;
    private boolean approved;

    public WorkingHour(LocalDate date, double hours) {
        this.date = date;
        this.hours = hours;
    }

    public void setID(int id) {
        this.id = id;
    }

    public int getID() {
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
