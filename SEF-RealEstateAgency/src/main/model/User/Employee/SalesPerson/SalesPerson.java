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

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.HashMap;

public abstract class SalesPerson extends Employee {
    private HashMap<String, Property> assignedProperties;
    private HashMap<String, Inspection> scheduledInspections;

    public SalesPerson(String name, String email, LocalDate hireDate, double salary) throws InvalidEmailException {
        super(name, email, hireDate, salary);
    }

    public HashMap<String, Property> getAssignedProperties() {
        return assignedProperties;
    }

    public void addProperty(Property property) {
        assignedProperties.put(property.getPropertyID(), property);
    }

    public boolean scheduleInspection(Property property, Customer person) {
        //logic if either property or person does not exist
        return false;
    }

    public boolean deactivateListing(Property property) {
        //logic if property is null
        return false;
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
        if (proposal.getProperty().hasProposal(proposal.getProposalID()) && proposal.getProperty().isActive() && assignedProperties.get(proposal.getProperty().getPropertyID()).getPropertyOwner().isListedProperty(proposal.getProperty())) {
            LocalDate now = LocalDate.now(ZoneId.systemDefault());
            Period period = Period.between(proposal.getSubmissionDate(), now);
            if (period.getDays() > 3) {
                throw new ExpiredProposalException();
            }

            proposal.setAccepted(true);
            proposal.getProperty().deactivate(); //once a proposal has been accepted, will the property be set as deactivated?
        }
    }

    public boolean rejectProposal(Proposal proposal) throws NotListedPropertyException, ProposalNotFoundException {
        if (proposal.getProperty().hasProposal(proposal.getProposalID()) && assignedProperties.get(proposal.getProperty().getPropertyID()).getPropertyOwner().isListedProperty(proposal.getProperty())) {
//            proposal.setAccepted(false);
            proposal.getProperty().setProposal(null);
            return true;
        }
        return false;
    }
}
