package main.model.User.Employee.SalesPerson;
import main.model.User.Employee.Employee;

public abstract class SalesPerson extends Employee {
	//private HashMap<String, Property> assignedProperties;
	//private HashMap<String, Inspection> scheduledInspections;
	
	public SalesPerson(String name, String email)
	{
		super(name, email);
	}
	/*
	public ArrayList<Property> getAssignedProperties()
	{
		return assignedProperties;
	}
	public void addProperty(Property property)
	{
		assignedProperties.add(property);
	}	
	public boolean scheduleInspection(Property property, Customer person)
	{
		//logic if either property or person does not exist
		return false;
	}
	public boolean deactivateListing(Property property)
	{
		//logic if property is null
		return false;
	}
	public void advertiseProperty(Property property)
	{
		//no functionality required 
	}
	*/
	public boolean contactUser(String userID)
	{
		//contact
		return false;
	}
	
	
}
