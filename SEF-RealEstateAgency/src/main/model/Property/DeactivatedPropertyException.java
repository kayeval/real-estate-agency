package main.model.Property;

public class DeactivatedPropertyException extends Exception {
    public DeactivatedPropertyException() {
        super("This property is inactive.");
    }
}
