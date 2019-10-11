package main.model.Proposal;

public class PendingProposalException extends Exception {
    public PendingProposalException() {
        super("Another proposal was submitted for this property.");
    }
}
