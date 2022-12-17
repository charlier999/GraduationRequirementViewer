/**
 * 
 */
package com.gradview.data.dao;

/**
 * Represents the "user-programs" table columns.
 * 
 * @author Charles Davis
 */
public class UserProgramsDAO
{
	/**
	 * The ID number of the User.
	 */
	private int userID;
	/**
	 * The ID of the Program.
	 */
	private int programID;

	/**
	 * @param userID    - The ID number of the User.
	 * @param programID - The ID of the Program.
	 */
	public UserProgramsDAO( int userID, int programID )
	{
		super();
		this.userID = userID;
		this.programID = programID;
	}

	/**
	 * @return the userID
	 */
	public int getUserID()
	{
		return userID;
	}

	/**
	 * @param userID the userID to set
	 */
	public void setUserID( int userID )
	{
		this.userID = userID;
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

}
