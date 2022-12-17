/**
 * 
 */
package com.gradview.data.dao;

/**
 * Represents the "user-assigned-role" table columns.
 * 
 * @author Charles Davis
 */
public class UserAssignedRoleDAO
{
	/**
	 * The ID number of the User.
	 */
	private int userID;
	/**
	 * The ID number of the Role.
	 */
	private int roleID;
	
	/**
	 * @param userID - The ID number of the User.
	 * @param roleID - The ID number of the User.
	 */
	public UserAssignedRoleDAO( int userID, int roleID )
	{
		super();
		this.userID = userID;
		this.roleID = roleID;
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
	 * @return the roleID
	 */
	public int getRoleID()
	{
		return roleID;
	}

	/**
	 * @param roleID the roleID to set
	 */
	public void setRoleID( int roleID )
	{
		this.roleID = roleID;
	}
	
	
}
