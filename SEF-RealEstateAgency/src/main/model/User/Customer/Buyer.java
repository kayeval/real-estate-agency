package main.model.User.Customer;

import main.model.Property.DeactivatedPropertyException;
import main.model.Property.SoldPropertyException;
import main.model.Proposal.InvalidContractDurationException;
import main.model.Proposal.PendingProposalException;
import main.model.Proposal.Proposal;
import main.model.Proposal.ProposalNotFoundException;
import main.model.User.InvalidEmailException;

public class Buyer extends Customer {
    public Buyer(String name, String email) throws InvalidEmailException {
        super(name, email);
    }

    @Override
    public void submitProposal(Proposal proposal) throws DeactivatedPropertyException, SoldPropertyException {
        if (proposal.getProperty().isActive()) {
            if (proposal.getProperty().getAcceptedProposal() != null)
                throw new SoldPropertyException();

            Proposal p = getProposals().putIfAbsent(proposal.getProposalID(), proposal);

            if (p != null) {
                getProposals().replace(proposal.getProposalID(), proposal);
            }

            proposal.getProperty().addProposal(proposal);
        }
    }
}
