/**
 * 
 */
package com.gradview.model;

import java.util.ArrayList;
import java.util.List;

import com.gradview.data.dam.AccClassDAM;

/**
 * The representation of the academic class.
 * 
 * @author Charles Davis
 */
public class AccClass
{
	/**
	 * The ID number of the class.
	 */
	private int id;
	/**
	 * The name of the class.
	 */
	private String name;
	/**
	 * The description of the class.
	 */
	private String description;
	/**
	 * The number the class represents. Example: EXP-404
	 */
	private String number;
	/**
	 * The number of credits the class awards.
	 */
	private int credits;
	/**
	 * The class is a general education class. if false, sets genEdCompID to -1,
	 * baOfArts to FALSE, and baOfScience to FALSE.
	 */
	private boolean isGenEd;
	/**
	 * The ID number representing the general education competency. isGenED:TRUE is
	 * required or is auto set to -1.
	 */
	private int genEdCompID;
	/**
	 * The class is general education class for the bachelors of arts. isGenED:TRUE
	 * is required or is auto set to FALSE.
	 */
	private boolean baOfArts;
	/**
	 * The class is general education class for the bachelors of science.
	 * isGenED:TRUE is required or is auto set to FALSE.
	 */
	private boolean baOfScience;
	/**
	 * Contains the prerequsites for the class
	 */
	private List<AccClassPrerequisite> prerequisites;

	/**
	 * @param id                   - The ID number of the class.
	 * @param name                 - The name of the class.
	 * @param description          - The description of the class.
	 * @param number               - The number the class represents.
	 * @param credits              - The class is a general education class.
	 * @param isGenEd              - The class is a general education class. if
	 *                             false, sets genEdCompID to -1, baOfArts to FALSE,
	 *                             and baOfScience to FALSE.
	 * @param genEdCompID          - The ID number representing the general
	 *                             education competency. isGenED:TRUE is required or
	 *                             is auto set to -1.
	 * @param baOfArts             - The class is general education class for the
	 *                             bachelors of science. isGenED:TRUE is required or
	 *                             is auto set to FALSE.
	 * @param baOfScience          - The class is general education class for the
	 *                             bachelors of arts. isGenED:TRUE is required or is
	 *                             auto set to FALSE.
	 */
	public AccClass( int id, String name, String description, String number, int credits, boolean isGenEd,
			int genEdCompID,  boolean baOfArts, boolean baOfScience )
	{
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.number = number;
		this.credits = credits;
		this.isGenEd = isGenEd;
		this.genEdCompID = genEdCompID;
		this.baOfArts = baOfArts;
		this.baOfScience = baOfScience;
		this.prerequisites = new ArrayList<>();

		this.checkGenEdProps();
	}

	/**
	 * @param name                 - The name of the class.
	 * @param description          - The description of the class.
	 * @param number               - The number the class represents.
	 * @param credits              - The class is a general education class.
	 * @param isGenEd              - The class is a general education class. if
	 *                             false, sets genEdCompID to -1, baOfArts to FALSE,
	 *                             and baOfScience to FALSE.
	 * @param genEdCompID          - The ID number representing the general
	 *                             education competency. isGenED:TRUE is required or
	 *                             is auto set to -1.
	 * @param baOfArts             - The class is general education class for the
	 *                             bachelors of science. isGenED:TRUE is required or
	 *                             is auto set to FALSE.
	 * @param baOfScience          - The class is general education class for the
	 *                             bachelors of arts. isGenED:TRUE is required or is
	 *                             auto set to FALSE.
	 */
	public AccClass( String name, String description, String number, int credits, boolean isGenEd, int genEdCompID,
			boolean baOfArts, boolean baOfScience )
	{
		super();
		this.name = name;
		this.description = description;
		this.number = number;
		this.credits = credits;
		this.isGenEd = isGenEd;
		this.genEdCompID = genEdCompID;
		this.baOfArts = baOfArts;
		this.baOfScience = baOfScience;
		this.prerequisites = new ArrayList<>();

		this.checkGenEdProps();
	}

	public AccClass()
	{
		this.prerequisites = new ArrayList<>();
	}

	/**
	 * Converts {@link AccClass} to {@link AccClassDAM}
	 * @return {@link AccClassDAM}
	 */
	public AccClassDAM toDAM()
	{
		return new AccClassDAM( this.getName(),  this.getDescription(),  this.getNumber(),  this.getCredits());
	}

	/**
	 * Checks to see if isGenEd is false and sets genEdCompID to -1, baOfArts to
	 * FALSE, and baOfScience to FALSE.
	 */
	private void checkGenEdProps()
	{
		if ( !this.isGenEd )
		{
			this.genEdCompID = -1;
			this.baOfArts = false;
			this.baOfScience = false;
		}
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

	/**
	 * @return the isGenEd
	 */
	public boolean isGenEd()
	{
		return isGenEd;
	}

	/**
	 * NOTE: if false, sets genEdCompID to -1, baOfArts to FALSE, and baOfScience to
	 * FALSE.
	 * 
	 * @param isGenEd the isGenEd to set
	 */
	public void setGenEd( boolean isGenEd )
	{
		this.isGenEd = isGenEd;
		this.checkGenEdProps();
	}

	/**
	 * @return the genEdCompID
	 */
	public int getGenEdCompID()
	{
		return genEdCompID;
	}

	/**
	 * @param genEdCompID the genEdCompID to set
	 */
	public void setGenEdCompID( int genEdCompID )
	{
		this.genEdCompID = genEdCompID;
		this.checkGenEdProps();
	}

	/**
	 * @return the baOfArts
	 */
	public boolean isBaOfArts()
	{
		return baOfArts;
	}

	/**
	 * NOTE: isGenED:TRUE is required or is auto set to FALSE.
	 * 
	 * @param baOfArts the baOfArts to set
	 */
	public void setBaOfArts( boolean baOfArts )
	{
		this.baOfArts = baOfArts;
		this.checkGenEdProps();
	}

	/**
	 * @return the baOfScience
	 */
	public boolean isBaOfScience()
	{
		return baOfScience;
	}

	/**
	 * NOTE: isGenED:TRUE is required or is auto set to FALSE.
	 * 
	 * @param baOfScience the baOfScience to set.
	 */
	public void setBaOfScience( boolean baOfScience )
	{
		this.baOfScience = baOfScience;
		this.checkGenEdProps();
	}

	public List< AccClassPrerequisite > getPrerequisites()
	{
		return prerequisites;
	}

	public void setPrerequisites( List< AccClassPrerequisite > prerequisites )
	{
		this.prerequisites = prerequisites;
	}

}
