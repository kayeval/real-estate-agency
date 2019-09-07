package main.model.User.PropertyOwner;

public class NotListedPropertyException extends Exception {
    public NotListedPropertyException() {
        super("The property does not belong to the property owner.");
    }
}
