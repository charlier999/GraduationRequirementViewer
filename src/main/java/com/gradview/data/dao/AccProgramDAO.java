/**
 * 
 */
package com.gradview.data.dao;

/**
 * Represents the "acc-program" table columns.
 * 
 * @author Charles Davis
 */
public class AccProgramDAO
{
	/**
	 * The ID number of the Program.
	 */
	private int id;
	/**
	 * The name of the Program.
	 */
	private String name;
	/**
	 * The description of the Program.
	 */
	private String description;
	/**
	 * The level of the Program.
	 */
	private String level;
	/**
	 * The program is a bachelors of arts.
	 */
	private boolean baOfArts;
	/**
	 * The program is a bachelors of science.
	 */
	private boolean baOfScience;

	/**
	 * @param id          - The ID number of the Program.
	 * @param name        - The name of the Program.
	 * @param description - The description of the Program.
	 * @param level       - The level of the Program.
	 * @param baOfArts    - The program is a bachelors of arts.
	 * @param baOfScience - The program is a bachelors of science.
	 */
	public AccProgramDAO( int id, String name, String description, String level, boolean baOfArts, boolean baOfScience )
	{
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.level = level;
		this.baOfArts = baOfArts;
		this.baOfScience = baOfScience;
	}

	/**
	 * @param name        - The name of the Program.
	 * @param description - The description of the Program.
	 * @param level       - The level of the Program.
	 * @param baOfArts    - The program is a bachelors of arts.
	 * @param baOfScience - The program is a bachelors of science.
	 */
	public AccProgramDAO( String name, String description, String level, boolean baOfArts, boolean baOfScience )
	{
		super();
		this.name = name;
		this.description = description;
		this.level = level;
		this.baOfArts = baOfArts;
		this.baOfScience = baOfScience;
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
	 * @return the level
	 */
	public String getLevel()
	{
		return level;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel( String level )
	{
		this.level = level;
	}

	/**
	 * @return the baOfArts
	 */
	public boolean isBaOfArts()
	{
		return baOfArts;
	}

	/**
	 * @param baOfArts the baOfArts to set
	 */
	public void setBaOfArts( boolean baOfArts )
	{
		this.baOfArts = baOfArts;
	}

	/**
	 * @return the baOfScience
	 */
	public boolean isBaOfScience()
	{
		return baOfScience;
	}

	/**
	 * @param baOfScience the baOfScience to set
	 */
	public void setBaOfScience( boolean baOfScience )
	{
		this.baOfScience = baOfScience;
	}

}
