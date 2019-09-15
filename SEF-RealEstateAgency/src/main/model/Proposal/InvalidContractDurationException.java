package main.model.Proposal;

public class InvalidContractDurationException extends Exception {
    public InvalidContractDurationException() {
        super("This contract duration is not among the choices given by the landlord.");
    }
}
