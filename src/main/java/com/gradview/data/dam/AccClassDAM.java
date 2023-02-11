/**
 * 
 */
package com.gradview.data.dam;

/**
 * Represents the "acc-class" table columns.
 * 
 * @author Charles Davis
 */
public class AccClassDAM
{
	/**
	 * The ID number of the Class.
	 */
	private int id;
	/**
	 * The name of the Class.
	 */
	private String name;
	/**
	 * The description of the Class.
	 */
	private String description;
	/**
	 * The number of the Class.
	 */
	private String number;
	/**
	 * The number of credits the Class awards.
	 */
	private int credits;

	/**
	 * @param id          - The ID number of the Class.
	 * @param name        - The name of the Class.
	 * @param description - The description of the Class.
	 * @param number      - The number of the Class.
	 * @param credits     - The number of credits the Class awards.
	 */
	public AccClassDAM( int id, String name, String description, String number, int credits )
	{
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.number = number;
		this.credits = credits;
	}

	/**
	 * @param name        - The name of the Class.
	 * @param description - The description of the Class.
	 * @param number      - The number of the Class.
	 * @param credits     - The number of credits the Class awards.
	 */
	public AccClassDAM( String name, String description, String number, int credits )
	{
		super();
		this.name = name;
		this.description = description;
		this.number = number;
		this.credits = credits;
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

	/**
	 * @return the description
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription( String description )
	{
		this.description = description;
	}

	/**
	 * @return the number
	 */
	public String getNumber()
	{
		return number;
	}

	/**
	 * @param number the number to set
	 */
	public void setNumber( String number )
	{
		this.number = number;
	}

	/**
	 * @return the credits
	 */
	public int getCredits()
	{
		return credits;
	}

	/**
	 * @param credits the credits to set
	 */
	public void setCredits( int credits )
	{
		this.credits = credits;
	}

}
