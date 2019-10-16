package main.model.Proposal;

import main.model.Property.Property;
import main.model.Property.RentalProperty;

public class Application extends Proposal {
    private ContractDuration contractDuration;

    public Application(double price, Property property, ContractDuration contractDuration) {
        super(price, property);
        setContractDuration(contractDuration);
    }

    public void setContractDuration(ContractDuration contractDuration) {
        this.contractDuration = contractDuration;
    }

    public boolean hasAcceptableContractDuration() throws InvalidContractDurationException {
        if (!((RentalProperty) getProperty()).getAcceptedDurations().contains(contractDuration))
            throw new InvalidContractDurationException();

        return true;
    }

    public ContractDuration getContractDuration() {
        return contractDuration;
    }
}
