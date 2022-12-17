/**
 * 
 */
package com.gradview.data.dao;

/**
 * Represents the "user-roles" table columns.
 * 
 * @author Charles Davis
 */
public class UserRolesDAO
{
	/**
	 * The ID number of the Role.
	 */
	private int id;
	
	/**
	 * The name of the Role.
	 */
	private String name;

	/**
	 * @param - id The ID number of the Role.
	 * @param name - THe name of the Role. 
	 */
	public UserRolesDAO( int id, String name )
	{
		super();
		this.id = id;
		this.name = name;
	}

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
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName( String name )
	{
		this.name = name;
	}
	
	
	
	
}
