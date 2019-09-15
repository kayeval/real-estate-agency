package main.model.Proposal;

public class ExpiredProposalException extends Exception {
    public ExpiredProposalException() {
        super("This offer or application has already expired.");
    }
}
