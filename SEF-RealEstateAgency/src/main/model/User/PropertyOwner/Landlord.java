package main.model.User.PropertyOwner;

import main.model.User.InvalidEmailException;

public class Landlord extends PropertyOwner {

    public Landlord(String name, String email) throws InvalidEmailException {
        super(name, email);
    }

}
