package main.model.Property;

import main.model.Proposal.Proposal;
import main.model.Proposal.ProposalNotFoundException;
import main.model.User.PropertyOwner.PropertyOwner;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

public abstract class Property {
    private String propertyID;
    private double price;
    private LocalDate dateListed;
    private boolean isActive;
    private Map<String, Proposal> pendingProposals;
    private Proposal acceptedProposal;
    private boolean documentsInspected;
    private String address;
    private String suburb;
    private Capacity capacity;
    private Type type;
    private PropertyOwner propertyOwner;

    public Property(String address, String suburb, Capacity capacity, Type type, double price, PropertyOwner propertyOwner) throws DeactivatedPropertyException {
        setPrice(price);
        setAddress(address);
        setSuburb(suburb);
        this.capacity = capacity;
        this.type = type;
        this.propertyOwner = propertyOwner;
        this.dateListed = LocalDate.now(ZoneId.systemDefault());
        pendingProposals = new HashMap<>();
        documentsInspected = false;
        isActive = true;
        propertyOwner.addProperty(this);
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public void addProposal(Proposal proposal) {
        Proposal p = pendingProposals.putIfAbsent(proposal.getProposalID(), proposal);

        if (p != null) {
            pendingProposals.replace(proposal.getProposalID(), proposal);
        }
    }

    public void setAcceptedProposal(Proposal proposal) {
        pendingProposals.clear();
        this.acceptedProposal = proposal;
    }

    public Proposal findProposal(String proposalID) throws ProposalNotFoundException {
        Proposal p = pendingProposals.get(proposalID);

        if (p == null)
            throw new ProposalNotFoundException();

        return p;
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

    public Type getType() {
        return type;
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

    public Proposal getAcceptedProposal() {
        return acceptedProposal;
    }

    public Map<String, Proposal> getPendingProposals() {
        return pendingProposals;
    }

    public void deactivate() {
        this.isActive = false;
    }
}
