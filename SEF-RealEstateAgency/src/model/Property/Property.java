package model.Property;

import model.User.PropertyOwner.PropertyOwner;
import model.Proposal.Proposal;

import java.util.Date;
import java.util.HashMap;

public abstract class Property {
    private String propertyID;
    private double price;
    private Date dateListed;
    private boolean isActive;
    private HashMap<String, Proposal> proposals;
    private Proposal acceptedProposal;
    private boolean documentsInspected;
    private String address;
    private String suburb;
    private Capacity capacity;
    private Type type;
    private PropertyOwner propertyOwner;

    public Property(double price) {
        setPrice(price);
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice() {
        return this.price;
    }

    public String getPropertyID() {
        return this.propertyID;
    }

}
