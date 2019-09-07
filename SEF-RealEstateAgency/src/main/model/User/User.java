package main.model.User;

public abstract class User {
	private String userID;
	private String name;
	private String email;
	
	public User(String name, String email)
	{
		this.name = name;
		this.email = email;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public void setEmail(String email)
	{
		this.email = email;
	}
	public String getUserID()
	{
		return this.userID;
	}
	public String getName()
	{
		return this.name;
	}
	public String getEmail()
	{
		return this.email;
	}



}
