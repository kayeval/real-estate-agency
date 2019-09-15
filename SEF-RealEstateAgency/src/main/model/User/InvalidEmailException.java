package main.model.User;

public class InvalidEmailException extends Exception {
    public InvalidEmailException() {
        super("Invalid email format given.");
    }
}
