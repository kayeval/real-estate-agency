package main.model.Proposal;

import main.model.Property.Property;
import main.model.User.User;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public abstract class Proposal {
    private String proposalID;
    private LocalDateTime submissionDate;
    private double price;
    private Property property;
    private Map<String, User> applicants;
    private boolean accepted;
    private boolean waitingForPayment;
    private boolean withdrawn;
    private boolean paid;
    private boolean pending;
    private LocalDateTime acceptDate;

    public Proposal(double price, Property property) {
        this.submissionDate = LocalDateTime.now(ZoneId.systemDefault());
        this.price = price;
        this.property = property;
        this.accepted = false;
        this.waitingForPayment = false;
        this.paid = false;
        this.withdrawn = false;
        this.pending = true;
        applicants = new HashMap<>();
    }

    public LocalDateTime getAcceptDate() {
        return acceptDate;
    }

    public void setAcceptDate(LocalDateTime acceptDate) {
        this.acceptDate = acceptDate;
    }

    public String getSubmissionDateFormatted() {
        return submissionDate.format(DateTimeFormatter.ofPattern("MM/dd/yy HH:mm"));
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }

    public boolean isPending() {
        return pending;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setApplicants(Map<String, User> applicants) {
        this.applicants = applicants;
    }

    public Map<String, User> getApplicants() {
        return applicants;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public void setWithdrawn(boolean withdrawn) {
        this.withdrawn = withdrawn;
    }

    public boolean isWithdrawn() {
        return withdrawn;
    }

    public void setWaitingForPayment(boolean waitingForPayment) {
        this.waitingForPayment = waitingForPayment;
    }

    public boolean getWaitingForPayment() {
        return waitingForPayment;
    }

    public void setProposalID(String proposalID) {
        this.proposalID = proposalID;
    }

    public double getPrice() {
        return price;
    }

    public LocalDateTime getSubmissionDate() {
        return submissionDate;
    }

    public Property getProperty() {
        return property;
    }

    public String getPropertyAddress() {
        return property.getAddress();
    }

    public String getProposalID() {
        return proposalID;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public void setSubmissionDate(LocalDateTime submissionDate) {
        this.submissionDate = submissionDate;
    }

    public String getStatus() {
        String status = "";

        if (isAccepted())
            status = "Accepted";
        else if (isPending())
            status = "Pending";
        else if (!isAccepted() && !isPending())
            status = "Rejected";

        return status;
    }
}
