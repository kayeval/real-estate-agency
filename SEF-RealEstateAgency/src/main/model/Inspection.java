package main.model;

import main.model.Property.Property;
import main.model.User.User;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Inspection {
    private String inspectionID;
    private boolean cancelled;
    private LocalDateTime dueDate;
    private LocalDateTime dateCreated;
    private Property property;
    private User customer;

    public Inspection(LocalDateTime dueDate) {
        this.dateCreated = LocalDateTime.now(ZoneId.systemDefault());
        this.dueDate = dueDate;
        cancelled = false;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public User getCustomer() {
        return customer;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setInspectionID(String inspectionID) {
        this.inspectionID = inspectionID;
    }

    public String getInspectionID() {
        return inspectionID;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public Property getProperty() {
        return property;
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

    public String getDueDateFormatted() {
        return dueDate.format(DateTimeFormatter.ofPattern("MM/dd/yy"));
    }

    public String getDueTimeFormatted() {
        return dueDate.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    public String getCustomerName() {
        return customer.getUsername();
    }

    public String getStatus() {
        String status = "";

        if (!isCancelled() && dueDate.isAfter(LocalDateTime.now(ZoneId.systemDefault())))
            status = "Scheduled";
        else if (!isCancelled() && dueDate.isBefore(LocalDateTime.now(ZoneId.systemDefault())))
            status = "Completed";
        else if (isCancelled())
            status = "Cancelled";

        return status;
    }
}
