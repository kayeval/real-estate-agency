package main.model.User.Employee;

public class HoursNotFoundException extends Exception {
	public HoursNotFoundException() {
		super("No hours inputted for this month.");
	}
}
