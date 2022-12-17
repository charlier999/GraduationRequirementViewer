package com.gradview.data.dao;

/**
 * Represents the "users" table columns.
 * 
 * @author Charles Davis
 */
public class UsersDAO
{
	/**
	 * The ID number of the User.
	 */
	private int id;

	/**
	 * The username of the User.
	 */
	private String username;

	/**
	 * The password of the user.
	 * (HASH REQUIRED)
	 */
	private String password;

	/**
	 * Constructor: Full
	 * 
	 * @param id       - The ID number of the user.
	 * @param username - The username of the user.
	 * @param password - The password of the user. (HASH REQUIRED)
	 */
	public UsersDAO( int id, String username, String password )
	{
		super();
		this.id = id;
		this.username = username;
		this.password = password;
	}

	/**
	 * Constructor: ID-less
	 * 
	 * @param username - The username of the user.
	 * @param password - The password of the user. (HASH REQUIRED)
	 */
	public UsersDAO( String username, String password )
	{
		super();
		this.username = username;
		this.password = password;
	}
	
	public UsersDAO() {}

	/**
	 * @return the id
	 */
	public int getId()
	{
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId( int id )
	{
		this.id = id;
	}

	/**
	 * @return the username
	 */
	public String getUsername()
	{
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername( String username )
	{
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword()
	{
		return password;
	}

	/**
	 * @param password the password to set. (HASH REQUIRED)
	 */
	public void setPassword( String password )
	{
		this.password = password;
	}

}
