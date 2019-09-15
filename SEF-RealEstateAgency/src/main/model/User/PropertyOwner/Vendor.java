package main.model.User.PropertyOwner;

import main.model.User.InvalidEmailException;

public class Vendor extends PropertyOwner {

    public Vendor(String name, String email) throws InvalidEmailException {
        super(name, email);
    }

}
