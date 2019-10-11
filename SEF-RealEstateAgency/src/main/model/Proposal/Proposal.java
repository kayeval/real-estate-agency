package main.model.Proposal;

import main.model.Property.Property;
import main.model.User.Customer.Customer;

import java.time.LocalDate;

public abstract class Proposal {
    private String proposalID;
    private LocalDate submissionDate;
    private double price;
    private Customer customer;
    private Property property;
    private boolean accepted;

    public Proposal(LocalDate submissionDate, double price, Property property, Customer customer) {
        this.submissionDate = submissionDate;
        this.price = price;
        this.customer = customer;
        this.property = property;
        this.accepted = false;
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

    public LocalDate getSubmissionDate() {
        return submissionDate;
    }

    public Property getProperty() {
        return property;
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
}
