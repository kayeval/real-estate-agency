package main.model.Property;

import main.model.Proposal.ContractDuration;
import main.model.User.PropertyOwner.PropertyOwner;

import java.util.Set;

public class RentalProperty extends Property {
    private Set<ContractDuration> acceptedDurations;

    public RentalProperty(String address, String suburb, Capacity capacity, PropertyType propertyType, double price, PropertyOwner propertyOwner, Set<ContractDuration> acceptedDurations) throws DeactivatedPropertyException {
        super(address, suburb, capacity, propertyType, price, propertyOwner);
        this.acceptedDurations = acceptedDurations;
    }

    public Set<ContractDuration> getAcceptedDurations() {
        return acceptedDurations;
    }
}
