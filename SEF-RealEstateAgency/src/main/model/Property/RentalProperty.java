package main.model.Property;

import main.model.User.PropertyOwner.PropertyOwner;

public class RentalProperty extends Property {

    public RentalProperty(String address, String suburb, Capacity capacity, Type type, double price, PropertyOwner propertyOwner) {
    	super(address, suburb, capacity, type, price, propertyOwner);
    }

}
