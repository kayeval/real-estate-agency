package main.model.User.Customer;

import main.model.Inspection;
import main.model.Property.DeactivatedPropertyException;
import main.model.Property.SoldPropertyException;
import main.model.Proposal.InvalidContractDurationException;
import main.model.Proposal.PendingProposalException;
import main.model.Proposal.Proposal;
import main.model.Proposal.ProposalNotFoundException;
import main.model.User.InvalidEmailException;
import main.model.User.User;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class Customer extends User {
    private Map<String, Proposal> proposals;
    private Set<String> preferredSuburbs;
    private Map<String, Inspection> scheduledInspections;

    public Customer(String name, String email) throws InvalidEmailException {
        super(name, email);
        proposals = new HashMap<>();
        scheduledInspections = new HashMap<>();
        preferredSuburbs = new HashSet<>();
    }

    public void addProposal(Proposal proposal) throws DeactivatedPropertyException {
        if (proposal.getProperty().isActive()) {
            Proposal p = proposals.putIfAbsent(proposal.getProposalID(), proposal);

            if (p != null) {
                proposals.replace(proposal.getProposalID(), proposal);
            }
        }
    }

    public void addInspection(Inspection inspection) {
        Inspection i = scheduledInspections.putIfAbsent(inspection.getInspectionID(), inspection);

        if (i != null) {
            scheduledInspections.replace(inspection.getInspectionID(), inspection);
        }
    }

    public void submitProposal(Proposal proposal) throws DeactivatedPropertyException, InvalidContractDurationException,
            SoldPropertyException, ProposalNotFoundException, PendingProposalException {
        proposal.getProperty().addProposal(proposal);
        addProposal(proposal);
    }

    public Map<String, Proposal> getProposals() {
        return proposals;
    }

    public Map<String, Inspection> getScheduledInspections() {
        return scheduledInspections;
    }

    public Set<String> getPreferredSuburbs() {
        return preferredSuburbs;
    }
}
