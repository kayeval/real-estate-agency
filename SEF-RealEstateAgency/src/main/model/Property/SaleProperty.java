package main.model.Property;

import main.model.User.PropertyOwner.PropertyOwner;

public class SaleProperty extends Property {

    public SaleProperty(String address, String suburb, Capacity capacity, PropertyType propertyType, double price, PropertyOwner propertyOwner) throws DeactivatedPropertyException {
        super(address, suburb, capacity, propertyType, price, propertyOwner);
    }

}
