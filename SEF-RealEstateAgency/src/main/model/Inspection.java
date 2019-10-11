package main.model;

import java.time.LocalDate;

public class Inspection {
    private String inspectionID;
    private boolean cancelled;
    private LocalDate date;

    public Inspection(LocalDate date) {
        this.date = date;
        cancelled = false;
    }

    public void setInspectionID(String inspectionID) {
        this.inspectionID = inspectionID;
    }

    public String getInspectionID() {
        return inspectionID;
    }

    public LocalDate getDate() {
        return date;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
