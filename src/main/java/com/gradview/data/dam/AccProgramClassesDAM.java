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

	private String classNumber;

	/**
	 * @param programID - The ID number of the Program.
	 * @param classID   - The ID number of the Class.
	 */
	public AccProgramClassesDAM( int programID, int classID, String classNumber )
	{
		super();
		this.programID = programID;
		this.classID = classID;
		this.classNumber = classNumber;
	}

	public AccProgramClassesDAM() 
	{
		super();
		this.programID = -1;
		this.classID = -1;
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

	public String getClassNumber()
	{
		return classNumber;
	}

	public void setClassNumber( String classNumber )
	{
		this.classNumber = classNumber;
	}

}
