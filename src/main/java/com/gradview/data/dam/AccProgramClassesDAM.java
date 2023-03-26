/**
 * 
 */
package com.gradview.data.dam;

/**
 * Represents the "acc-program-classes" table columns.
 * 
 * @author Charles Davis
 */
public class AccProgramClassesDAM
{
	/**
	 * The ID number of the Program.
	 */
	private int programID;
	/**
	 * The ID number of the Class.
	 */
	private int classID;

	/**
	 * @param programID - The ID number of the Program.
	 * @param classID   - The ID number of the Class.
	 */
	public AccProgramClassesDAM( int programID, int classID )
	{
		super();
		this.programID = programID;
		this.classID = classID;
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

}
