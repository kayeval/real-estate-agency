package main.model.User.Customer;

import main.model.Property.DeactivatedPropertyException;
import main.model.Property.SoldPropertyException;
import main.model.Proposal.*;
import main.model.User.InvalidEmailException;

public class Renter extends Customer {
    private double income;
    private String occupation;

    public Renter(String name, String email, double income, String occupation) throws InvalidEmailException {
        super(name, email);
        setIncome(income);
        setOccupation(occupation);
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public double getIncome() {
        return income;
    }

    public String getOccupation() {
        return occupation;
    }

    @Override
    public void submitProposal(Proposal proposal) throws DeactivatedPropertyException, SoldPropertyException,
            InvalidContractDurationException, ProposalNotFoundException, PendingProposalException {

        if (proposal.getProperty().isActive() && ((Application) proposal).hasAcceptableContractDuration()) {
            if (proposal.getProperty().getProposal() != null) {
                if (!proposal.getProperty().getPropertyID().equals(proposal.getProposalID()))
                    throw new PendingProposalException();
            }

            super.submitProposal(proposal);
        }
    }
}
