package main.model.Property;

import main.model.Proposal.ContractDuration;
import main.model.User.PropertyOwner.PropertyOwner;

import java.time.LocalDate;
import java.util.Set;

public class RentalProperty extends Property {
    private Set<ContractDuration> acceptedDurations;
    private LocalDate nextMaintenance;
    private LocalDate previousMaintenance;

    public RentalProperty(String address, String suburb, Capacity capacity, PropertyType propertyType, double price, PropertyOwner propertyOwner, Set<ContractDuration> acceptedDurations) throws DeactivatedPropertyException {
        super(address, suburb, capacity, propertyType, price, propertyOwner);
        this.acceptedDurations = acceptedDurations;
    }

    public LocalDate getNextMaintenance() {
        return nextMaintenance;
    }

    public LocalDate getPreviousMaintenance() {
        return previousMaintenance;
    }

    public void setNextMaintenance(LocalDate nextMaintenance) {
        this.nextMaintenance = nextMaintenance;
    }

    public void setPreviousMaintenance(LocalDate previousMaintenance) {
        this.previousMaintenance = previousMaintenance;
    }

    public Set<ContractDuration> getAcceptedDurations() {
        return acceptedDurations;
    }
}
