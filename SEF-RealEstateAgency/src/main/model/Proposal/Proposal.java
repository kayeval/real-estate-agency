package main.model.Proposal;

import main.model.Property.Property;
import main.model.User.Customer.Customer;

import java.time.LocalDateTime;
import java.time.ZoneId;

public abstract class Proposal {
    private String proposalID;
    private LocalDateTime submissionDate;
    private double price;
    private Customer customer;
    private Property property;
    private boolean accepted;
    private boolean waitingForPayment;

    public Proposal(double price, Property property, Customer customer) {
        this.submissionDate = LocalDateTime.now(ZoneId.systemDefault());
        this.price = price;
        this.customer = customer;
        this.property = property;
        this.accepted = false;
        this.waitingForPayment = false;
    }

    public void setWaitingForPayment(boolean waitingForPayment) {
        this.waitingForPayment = waitingForPayment;
    }

    public boolean isWaitingForPayment() {
        return waitingForPayment;
    }

    public void setProposalID(String proposalID) {
        this.proposalID = proposalID;
    }

    public Customer getCustomer() {
        return customer;
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
}
