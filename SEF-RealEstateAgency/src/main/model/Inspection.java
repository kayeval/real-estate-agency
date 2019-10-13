package main.model;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class Inspection {
    private String inspectionID;
    private boolean cancelled;
    private LocalDateTime dueDate;
    private LocalDateTime dateCreated;

    public Inspection(LocalDateTime dueDate) {
        this.dateCreated = LocalDateTime.now(ZoneId.systemDefault());
        this.dueDate = dueDate;
        cancelled = false;
    }

    public void setInspectionID(String inspectionID) {
        this.inspectionID = inspectionID;
    }

    public String getInspectionID() {
        return inspectionID;
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
