package main.model.Proposal;

public class ExpiredProposalException extends Exception {
    public ExpiredProposalException() {
        super("The offer or application has already expired.");
    }
}
