/**
 * 
 */
package com.gradview.data.dao;

/**
 * Represents the "acc-program-total-credits" table columns.
 * 
 * @author Charles Davis
 */
public class AccProgramTotalCreditsDAO
{
	/**
	 * The ID number of the Program.
	 */
	private int programID;
	/**
	 * The number of total amount of credits the program requires.
	 */
	private int credits;

	/**
	 * @param programID - The ID number of the Program.
	 * @param credits - The number of total amount of credits the program requires.
	 */
	public AccProgramTotalCreditsDAO( int programID, int credits )
	{
		super();
		this.programID = programID;
		this.credits = credits;
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
