package com.gradview.data.dam;

public class AccProgramElectivesCreditsDAM
{
	/**
	 * The ID number of the Program.
	 */
	private int programID;
	/**
	 * The minimum number of elective required.
	 */
	private int minimum;
	/**
	 * The maximum number of elective credits required.
	 */
	private int maximum;

	/**
	 * @param programID - The ID number of the Program.
	 * @param minimum - The minimum number of elective credits required.
	 * @param maximum - The maximum number of elective credits required.
	 */
	public AccProgramElectivesCreditsDAM( int programID, int minimum, int maximum )
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
