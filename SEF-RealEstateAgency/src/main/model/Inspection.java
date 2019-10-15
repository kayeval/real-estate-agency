package main.model;

import main.model.Property.Property;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class Inspection {
    private int inspectionID;
    private boolean cancelled;
    private LocalDateTime dueDate;
    private LocalDateTime dateCreated;
    private Property property;

    public Inspection(LocalDateTime dueDate) {
        this.dateCreated = LocalDateTime.now(ZoneId.systemDefault());
        this.dueDate = dueDate;
        cancelled = false;
    }

    public void setInspectionID(int inspectionID) {
        this.inspectionID = inspectionID;
    }

    public int getInspectionID() {
        return inspectionID;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public String getPropertyAddress() {
        return property.getAddress();
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
