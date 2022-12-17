/**
 * 
 */
package com.gradview.data.dao;

/**
 * Represents the "acc-general-education-classes" table columns.
 * 
 * @author Charles Davis
 */
public class AccGeneralEducationClassesDAO
{
	/**
	 * The ID of the General Education Class.
	 */
	private int classID;
	/**
	 * The ID of the Competency for the General Education Class.
	 */
	private int competencyID;
	/**
	 * The Class is required for Bachelors of Arts.
	 */
	private boolean baOfArts;
	/**
	 * The Class is required for Bachelors of Science.
	 */
	private boolean baOfScience;

	/**
	 * @param classID      - The ID of the General Education Class.
	 * @param competencyID - The ID of the Competency for the General Education
	 *                     Class.
	 * @param baOfArts     - The Class is required for Bachelors of Arts.
	 * @param baOfScience  - The Class is required for Bachelors of Science.
	 */
	public AccGeneralEducationClassesDAO( int classID, int competencyID, boolean baOfArts, boolean baOfScience )
	{
		super();
		this.classID = classID;
		this.competencyID = competencyID;
		this.baOfArts = baOfArts;
		this.baOfScience = baOfScience;
	}

	/**
	 * @return the classID
	 */
	public int getClassID()
	{
		return classID;
	}

	/**
	 * @param classID the classID to set
	 */
	public void setClassID( int classID )
	{
		this.classID = classID;
	}

	/**
	 * @return the competencyID
	 */
	public int getCompetencyID()
	{
		return competencyID;
	}

	/**
	 * @param competencyID the competencyID to set
	 */
	public void setCompetencyID( int competencyID )
	{
		this.competencyID = competencyID;
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
