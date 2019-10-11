package main.model.Property;

import main.model.Proposal.Proposal;
import main.model.Proposal.ProposalNotFoundException;
import main.model.User.PropertyOwner.PropertyOwner;

import java.time.LocalDate;
import java.time.ZoneId;

public abstract class Property {
    private String propertyID;
    private double price;
    private LocalDate dateListed;
    private boolean isActive;
    private Proposal proposal;
    private boolean documentsInspected;
    private String address;
    private String suburb;
    private Capacity capacity;
    private PropertyType propertyType;
    private PropertyOwner propertyOwner;

    public Property(String address, String suburb, Capacity capacity, PropertyType propertyType, double price, PropertyOwner propertyOwner) throws DeactivatedPropertyException {
        setPrice(price);
        setAddress(address);
        setSuburb(suburb);
        this.capacity = capacity;
        this.propertyType = propertyType;
        this.propertyOwner = propertyOwner;
        this.dateListed = LocalDate.now(ZoneId.systemDefault());
        documentsInspected = false;
        isActive = true;
        propertyOwner.addProperty(this);
    }

    public void setPropertyID(String propertyID) {
        this.propertyID = propertyID;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public void addProposal(Proposal proposal) {
        this.proposal = proposal;
    }

    public boolean hasProposal(String proposalID) throws ProposalNotFoundException {
        if (!proposal.getProposalID().equals(proposalID))
            throw new ProposalNotFoundException();

        return true;
    }

    public boolean hasAcceptedProposal(String proposalID) throws ProposalNotFoundException {
        if (proposal != null)
            return hasProposal(proposalID) && proposal.isAccepted();
        return false;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice() {
        return this.price;
    }

    public LocalDate getDateListed() {
        return dateListed;
    }

    public String getSuburb() {
        return suburb;
    }

    public String getAddress() {
        return address;
    }

    public Capacity getCapacity() {
        return capacity;
    }

    public PropertyType getPropertyType() {
        return propertyType;
    }

    public PropertyOwner getPropertyOwner() {
        return propertyOwner;
    }

    public String getPropertyID() {
        return this.propertyID;
    }

    public boolean isActive() throws DeactivatedPropertyException {
        if (!isActive)
            throw new DeactivatedPropertyException();

        return true;
    }

    public boolean areDocumentsInspected() {
        return documentsInspected;
    }

    public Proposal getProposal() {
        return proposal;
    }

    public void deactivate() {
        this.isActive = false;
    }

    public void setProposal(Proposal proposal) {
        this.proposal = proposal;
    }
}
