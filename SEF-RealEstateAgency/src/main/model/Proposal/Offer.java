package main.model.Proposal;

import main.model.Property.Property;
import main.model.User.User;

public class Offer extends Proposal {

    public Offer(double price, Property property) {
        super(price, property);
    }

    public User getApplicant() {
        return (User) getApplicants().values().toArray()[0];
    }
}
