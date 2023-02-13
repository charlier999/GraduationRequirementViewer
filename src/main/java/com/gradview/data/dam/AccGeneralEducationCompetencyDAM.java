/**
 * 
 */
package com.gradview.data.dam;

/**
 * Represents the "acc-general-education-competency" table columns.
 * 
 * @author Charles Davis
 */
public class AccGeneralEducationCompetencyDAM
{
	/**
	 * The ID number of the General Education Competency.
	 */
	private int id;
	/**
	 * The name of the General Education Competency.
	 */
	private String name;
	/**
	 * The description of the General Education Competency.
	 */
	private String description;
	/**
	 * The minimum required credits for the General Education Competency.
	 */
	private int minimumCredits;
	/**
	 * The maximum required credits for the General Education Competency.
	 */
	private int maximumCredits;

	/**
	 * @param id             - The ID number of the General Education Competency.
	 * @param name           - The name of the General Education Competency.
	 * @param description    - The description of the General Education Competency.
	 * @param minimumCredits - The minimum required credits for the General
	 *                       Education Competency.
	 * @param maximumCredits - The maximum required credits for the General
	 *                       Education Competency.
	 */
	public AccGeneralEducationCompetencyDAM( int id, String name, String description, int minimumCredits,
			int maximumCredits )
	{
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.minimumCredits = minimumCredits;
		this.maximumCredits = maximumCredits;
	}

	/**
	 * @param name           - The name of the General Education Competency.
	 * @param description    - The description of the General Education Competency.
	 * @param minimumCredits - The minimum required credits for the General
	 *                       Education Competency.
	 * @param maximumCredits - The maximum required credits for the General
	 *                       Education Competency.
	 */
	public AccGeneralEducationCompetencyDAM( String name, String description, int minimumCredits, int maximumCredits )
	{
		super();
		this.name = name;
		this.description = description;
		this.minimumCredits = minimumCredits;
		this.maximumCredits = maximumCredits;
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
	 * @return the minimumCredits
	 */
	public int getMinimumCredits()
	{
		return minimumCredits;
	}

	/**
	 * @param minimumCredits the minimumCredits to set
	 */
	public void setMinimumCredits( int minimumCredits )
	{
		this.minimumCredits = minimumCredits;
	}

	/**
	 * @return the maximumCredits
	 */
	public int getMaximumCredits()
	{
		return maximumCredits;
	}

	/**
	 * @param maximumCredits the maximumCredits to set
	 */
	public void setMaximumCredits( int maximumCredits )
	{
		this.maximumCredits = maximumCredits;
	}

}
