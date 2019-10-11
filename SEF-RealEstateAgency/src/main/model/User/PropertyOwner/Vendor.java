package main.model.User.PropertyOwner;

import main.model.User.InvalidEmailException;

public class Vendor extends PropertyOwner {

    public Vendor(String username, String email) throws InvalidEmailException {
        super(username, email);
    }

}
