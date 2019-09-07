package main.model.User.PropertyOwner;

import main.model.Property.DeactivatedPropertyException;
import main.model.Property.Property;
import main.model.Proposal.ExpiredProposalException;
import main.model.Proposal.Proposal;
import main.model.Proposal.ProposalNotFoundException;
import main.model.User.User;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class PropertyOwner extends User {
    private Map<String, Property> listedProperties;

    public PropertyOwner(String name, String email) {
        super(name, email);
        listedProperties = new HashMap<>();
    }

    public void addProperty(Property property) throws NotListedPropertyException, DeactivatedPropertyException {
        if (isActiveProperty(property) && isListedProperty(property)) {
            Property p = listedProperties.putIfAbsent(property.getPropertyID(), property);

            if (p != null) {
                listedProperties.replace(property.getPropertyID(), property);
            }
        }
    }

    public boolean editProperty(Property property) throws NotListedPropertyException, DeactivatedPropertyException {
        if (isActiveProperty(property) && isListedProperty(property)) {
            //todo
            return true;
        }
        return false;
    }

    public ArrayList<Property> getProperties() {
        return new ArrayList<Property>(listedProperties.values());
    }

    public boolean acceptProposal(Proposal proposal) throws ExpiredProposalException, NotListedPropertyException, DeactivatedPropertyException {
        if (isActiveProperty(proposal.getProperty()) && isListedProperty(proposal.getProperty())) {
            LocalDate now = LocalDate.now(ZoneId.systemDefault());
            Period period = Period.between(now, proposal.getSubmissionDate());

            if (period.getDays() > 3) {
                throw new ExpiredProposalException();
            }

            proposal.getProperty().setAcceptedProposal(proposal);

            return true;
        }
        return false;
    }

    public boolean rejectProposal(Proposal proposal) throws NotListedPropertyException, ProposalNotFoundException {
        if (isListedProperty(proposal.getProperty())) {
            Proposal toRemove = proposal.getProperty().findProposal(proposal.getProposalID());
            proposal.getProperty().getPendingProposals().remove(toRemove.getProposalID());
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

    public boolean isActiveProperty(Property property) throws DeactivatedPropertyException {
        if (!property.isActive())
            throw new DeactivatedPropertyException();

        return true;
    }
}
