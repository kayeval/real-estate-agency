package main.model.User.Customer;

import main.model.Property.DeactivatedPropertyException;
import main.model.Property.SoldPropertyException;
import main.model.Proposal.InvalidContractDurationException;
import main.model.Proposal.PendingProposalException;
import main.model.Proposal.Proposal;
import main.model.Proposal.ProposalNotFoundException;
import main.model.User.InvalidEmailException;

public class Buyer extends Customer {
    public Buyer(String username, String email) throws InvalidEmailException {
        super(username, email);
    }

    @Override
    public void submitProposal(Proposal proposal) throws DeactivatedPropertyException, SoldPropertyException, ProposalNotFoundException, PendingProposalException, InvalidContractDurationException {
        if (proposal.getProperty().isActive()) {
            super.submitProposal(proposal);
        }
    }
}
