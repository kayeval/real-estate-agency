package main.model.Proposal;

import main.model.Property.Property;
import main.model.Property.RentalProperty;
import main.model.User.Customer.Customer;

import java.time.LocalDate;

public class Application extends Proposal {
    private ContractDuration contractDuration;

    public Application(LocalDate submissionDate, double price, Property property, Customer customer, ContractDuration contractDuration) {
        super(submissionDate, price, property, customer);
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
}
