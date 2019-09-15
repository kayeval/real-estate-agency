package main.model.Property;

public class SoldPropertyException extends Exception {
    public SoldPropertyException() {
        super("This property has already been sold.");
    }
}
