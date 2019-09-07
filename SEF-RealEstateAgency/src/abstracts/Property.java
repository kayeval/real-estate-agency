package abstracts;

import java.util.Date;

public abstract class Property {
	
	private String propertyID;
	private double price;
	private Date dateListed;
	private boolean isActive;
	//private HashMap<String, Proposal> proposals;
	//acceptedProposal
	private boolean documentsInspected;
	private String address; 
	private String suburb;
	//private Capacity capacity;
	//private Type type;
	//private PropertyOwner propertyOwner;
	
	public Property(String propertyID, double price)
	{
		this.propertyID = propertyID;
		this.price = price;
	}
	
	public double getPrice()
	{
		return this.price;
	}
	
	public String getPropertyID()
	{
		return this.propertyID;
	}

}
