/**
 * 
 */
package com.gradview.data.dao;

/**
 * Represents the "user-scheduled-classes" table columns.
 * 
 * @author Charles Davis
 */
public class UserScheduledClassesDAO
{
	/**
	 * The ID number of the User.
	 */
	private int userID;
	/**
	 * The ID number of the Class.
	 */
	private int classID;

	/**
	 * @param userID  - The ID number of the User.
	 * @param classID - The ID number of the Class.
	 */
	public UserScheduledClassesDAO( int userID, int classID )
	{
		super();
		this.userID = userID;
		this.classID = classID;
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
