package main.model.Proposal;

public class ProposalNotFoundException extends Exception {
    public ProposalNotFoundException() {
        super("The proposal was not found.");
    }
}
