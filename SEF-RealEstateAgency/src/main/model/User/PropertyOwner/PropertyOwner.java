package main.model.User.PropertyOwner;

import main.model.Property.DeactivatedPropertyException;
import main.model.Property.Property;
import main.model.Proposal.ExpiredProposalException;
import main.model.Proposal.Proposal;
import main.model.Proposal.ProposalNotFoundException;
import main.model.User.InvalidEmailException;
import main.model.User.User;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class PropertyOwner extends User {
    private Map<String, Property> listedProperties;

    public PropertyOwner(String name, String email) throws InvalidEmailException {
        super(name, email);
        listedProperties = new HashMap<>();
    }

    public void addProperty(Property property) throws DeactivatedPropertyException {
        if (property.isActive()) {
            Property p = listedProperties.putIfAbsent(property.getPropertyID(), property);

            if (p != null) {
                listedProperties.replace(property.getPropertyID(), property);
            }
        }
    }

    public boolean editProperty(Property property) throws NotListedPropertyException, DeactivatedPropertyException {
        if (property.isActive() && isListedProperty(property)) {
            //todo
            return true;
        }
        return false;
    }

    public ArrayList<Property> getProperties() {
        return new ArrayList<>(listedProperties.values());
        return new ArrayList<>(listedProperties.values());
    }

    public void acceptProposal(Proposal proposal) throws ProposalNotFoundException, ExpiredProposalException,
            NotListedPropertyException, DeactivatedPropertyException {
        if (proposal.getProperty().hasProposal(proposal.getProposalID()) && proposal.getProperty().isActive() && isListedProperty(proposal.getProperty())) {
            LocalDate now = LocalDate.now(ZoneId.systemDefault());
            Period period = Period.between(proposal.getSubmissionDate(), now);
            if (period.getDays() > 3) {
                throw new ExpiredProposalException();
            }

            proposal.setAccepted(true);
            proposal.getCustomer().addProperty(proposal.getProperty());
            //Q: once a proposal has been accepted, will the property be set as deactivated?
        }
    }

    public boolean rejectProposal(Proposal proposal) throws NotListedPropertyException, ProposalNotFoundException {
        if (proposal.getProperty().hasProposal(proposal.getProposalID()) && isListedProperty(proposal.getProperty())) {
            proposal.setAccepted(false);
            return true;
        }
        return false;
    }

    public Property findProperty(Property property) {
        return listedProperties.get(property.getPropertyID());
    }

    public boolean isListedProperty(Property property) throws NotListedPropertyException {
        if (findProperty(property) == null)
            throw new NotListedPropertyException();

        return true;
    }
}
