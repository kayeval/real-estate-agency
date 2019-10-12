package main.model.User.Employee;

public class HoursNotApprovedException extends Exception {
    public HoursNotApprovedException() {
        super("Hours have not been approved");
    }


}
