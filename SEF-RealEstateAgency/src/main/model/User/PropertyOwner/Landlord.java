package main.model.User.PropertyOwner;

import main.model.User.InvalidEmailException;

public class Landlord extends PropertyOwner {

    public Landlord(String username, String email) throws InvalidEmailException {
        super(username, email);
    }

}
