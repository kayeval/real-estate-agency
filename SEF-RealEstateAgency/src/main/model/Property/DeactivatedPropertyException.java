package main.model.Property;

public class DeactivatedPropertyException extends Exception {
    public DeactivatedPropertyException() {
        super("The property is inactive.");
    }
}
