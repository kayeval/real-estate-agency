package main.model.Property;

import main.model.User.PropertyOwner.PropertyOwner;

import java.time.LocalDate;

public class SaleProperty extends Property {
    private LocalDate nextMaintenance;
    private LocalDate previousMaintenance;

    public SaleProperty(String address, String suburb, Capacity capacity, PropertyType propertyType, double price, PropertyOwner propertyOwner) throws DeactivatedPropertyException {
        super(address, suburb, capacity, propertyType, price, propertyOwner);
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
}
