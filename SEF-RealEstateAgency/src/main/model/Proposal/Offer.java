package main.model.Proposal;

import main.model.Property.Property;
import main.model.User.Customer.Customer;

public class Offer extends Proposal {

    public Offer(double price, Property property, Customer customer) {
        super(price, property, customer);
    }

}
