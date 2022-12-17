/**
 * 
 */
package com.gradview.data.dao;

/**
 * Represents the "acc-program-general-education-credits" table columns.
 * 
 * @author Charles Davis
 */
public class AccProgramGeneralEducationCreditsDAO
{
	/**
	 * The ID number of the Program.
	 */
	private int programID;
	/**
	 * The minimum number of general education credits required.
	 */
	private int minimum;
	/**
	 * The maximum number of general education credits required.
	 */
	private int maximum;

	/**
	 * @param programID - The ID number of the Program.
	 * @param minimum - The minimum number of general education credits required.
	 * @param maximum - The maximum number of general education credits required.
	 */
	public AccProgramGeneralEducationCreditsDAO( int programID, int minimum, int maximum )
	{
		super();
		this.programID = programID;
		this.minimum = minimum;
		this.maximum = maximum;
	}

	/**
	 * @return the programID
	 */
	public int getProgramID()
	{
		return programID;
	}

	/**
	 * @param programID the programID to set
	 */
	public void setProgramID( int programID )
	{
		this.programID = programID;
	}

	/**
	 * @return the minimum
	 */
	public int getMinimum()
	{
		return minimum;
	}

	/**
	 * @param minimum the minimum to set
	 */
	public void setMinimum( int minimum )
	{
		this.minimum = minimum;
	}

	/**
	 * @return the maximum
	 */
	public int getMaximum()
	{
		return maximum;
	}

	/**
	 * @param maximum the maximum to set
	 */
	public void setMaximum( int maximum )
	{
		this.maximum = maximum;
	}

}
