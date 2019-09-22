package main.model.User.Customer;

import main.model.Property.DeactivatedPropertyException;
import main.model.Property.SoldPropertyException;
import main.model.Proposal.Proposal;
import main.model.Proposal.ProposalNotFoundException;
import main.model.User.InvalidEmailException;

public class Buyer extends Customer {
    public Buyer(String name, String email) throws InvalidEmailException {
        super(name, email);
    }

    @Override
    public void submitProposal(Proposal proposal) throws DeactivatedPropertyException, SoldPropertyException, ProposalNotFoundException {
        if (proposal.getProperty().isActive()) {
            if (proposal.getProperty().hasAcceptedProposal(proposal.getProposalID()))
                throw new SoldPropertyException();

            proposal.getProperty().addProposal(proposal);
        }
    }
}
