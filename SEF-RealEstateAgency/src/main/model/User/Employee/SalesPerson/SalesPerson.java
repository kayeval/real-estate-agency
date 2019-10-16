package main.model.User.Employee.SalesPerson;

import main.model.Inspection;
import main.model.Property.DeactivatedPropertyException;
import main.model.Property.Property;
import main.model.Proposal.ExpiredProposalException;
import main.model.Proposal.Proposal;
import main.model.Proposal.ProposalNotFoundException;
import main.model.User.Customer.Customer;
import main.model.User.Employee.Employee;
import main.model.User.InvalidEmailException;
import main.model.User.PropertyOwner.NotListedPropertyException;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

public abstract class SalesPerson extends Employee {
    private Map<String, Property> assignedProperties;
    private Map<String, Inspection> scheduledInspections;

    public SalesPerson(String username, String email, LocalDate hireDate, double salary) throws InvalidEmailException {
        super(username, email, hireDate, salary);
        assignedProperties = new HashMap<>();
        scheduledInspections = new HashMap<>();
    }

    public Map<String, Property> getAssignedProperties() {
        return assignedProperties;
    }


    public void assignProperty(Property property) {
        assignedProperties.put(property.getPropertyID(), property);
    }

    public boolean scheduleInspection(Property property, Customer person) {
        //logic if either property or person does not exist
        return false;
    }

    public void deactivateListing(Property property) {
        property.deactivate();
    }

    public void advertiseProperty(Property property) {
        //no functionality required
    }

    public boolean contactUser(String userID) {
        //contact
        return false;
    }

    public void acceptProposal(Proposal proposal) throws ProposalNotFoundException, ExpiredProposalException,
            NotListedPropertyException, DeactivatedPropertyException {
        //must check if the property has been assigned to this employee

        if (proposal.getProperty().hasProposal(proposal.getProposalID()) && proposal.getProperty().isActive() && assignedProperties.get(proposal.getProperty().getPropertyID()).getPropertyOwner().isListedProperty(proposal.getProperty())) {
            LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
            if (Duration.between(proposal.getSubmissionDate(), now).toDays() > 3) {
                throw new ExpiredProposalException();
            }

            proposal.setAccepted(true);
            deactivateListing(proposal.getProperty()); //once a proposal has been accepted, will the property be set as deactivated?
        }
    }

    public boolean rejectProposal(Proposal proposal) throws NotListedPropertyException, ProposalNotFoundException {
        if (proposal.getProperty().hasProposal(proposal.getProposalID()) && assignedProperties.get(proposal.getProperty().getPropertyID()).getPropertyOwner().isListedProperty(proposal.getProperty())) {
            proposal.getProperty().setProposal(null);
            return true;
        }
        return false;
    }

    public int getTotalAssignments() {
        return assignedProperties.size();
    }

}
