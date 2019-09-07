package main.model.Proposal;

import main.model.Property.Property;
import main.model.User.Customer.Customer;

import java.time.LocalDate;

public class Application extends Proposal {
    public Application(LocalDate submissionDate, double price, Property property, Customer customer) {
        super(submissionDate, price, property, customer);
    }

}
