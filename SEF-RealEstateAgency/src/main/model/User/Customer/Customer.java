package main.model.User.Customer;

import main.model.Inspection;
import main.model.Property.DeactivatedPropertyException;
import main.model.Property.Property;
import main.model.Property.SoldPropertyException;
import main.model.Proposal.InvalidContractDurationException;
import main.model.Proposal.Proposal;
import main.model.Proposal.ProposalNotFoundException;
import main.model.User.InvalidEmailException;
import main.model.User.User;

import java.util.HashMap;
import java.util.Map;

public abstract class Customer extends User {
    private Map<String, Proposal> proposals;
    private Map<String, Property> properties;
    private Map<String, Inspection> scheduledInspections;
    private String accountNo;

    public Customer(String name, String email) throws InvalidEmailException {
        super(name, email);
        proposals = new HashMap<>();
        properties = new HashMap<>();
        scheduledInspections = new HashMap<>();
    }

    public void addProposal(Proposal proposal) throws DeactivatedPropertyException {
        if (proposal.getProperty().isActive()) {
            Proposal p = proposals.putIfAbsent(proposal.getProposalID(), proposal);

            if (p != null) {
                proposals.replace(proposal.getProposalID(), proposal);
            }
        }
    }

    public void addProperty(Property property) {
        Property p = properties.putIfAbsent(property.getPropertyID(), property);

        if (p != null) {
            properties.replace(property.getPropertyID(), property);
        }
    }

    public void addInspection(Inspection inspection) {
        Inspection i = scheduledInspections.putIfAbsent(inspection.getInspectionID(), inspection);

        if (i != null) {
            scheduledInspections.replace(inspection.getInspectionID(), inspection);
        }
    }

    public abstract void submitProposal(Proposal proposal) throws DeactivatedPropertyException, InvalidContractDurationException, SoldPropertyException, ProposalNotFoundException;

    public Map<String, Proposal> getProposals() {
        return proposals;
    }

    public Map<String, Inspection> getScheduledInspections() {
        return scheduledInspections;
    }

    public Map<String, Property> getProperties() {
        return properties;
    }

    public String getAccountNo() {
        return accountNo;
    }
}
