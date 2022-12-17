/**
 * 
 */
package com.gradview.data.dao;

/**
 * Represents the "permissions" table columns.
 * 
 * @author Charles Davis
 */
public class PermissionsDAO
{
	/**
	 * The ID number of the Permission.
	 */
	private int id;

	/**
	 * The name of the Permission.
	 */
	private String name;

	/**
	 * @param id - The ID number of the Permission.
	 * @param name - The name of the Permission.
	 */
	public PermissionsDAO( int id, String name )
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
