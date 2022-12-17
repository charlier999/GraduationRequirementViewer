/**
 * 
 */
package com.gradview.model;

import java.util.List;

import com.gradview.data.dao.UserAssignedRoleDAO;

/**
 * The representation of the user.
 * 
 * @author Charles Davis
 */
public class UserInfo
{
	/**
	 * The ID number of the user.
	 */
	private int id;
	/**
	 * The username of the user.
	 */
	private String username;
	/**
	 * The password of the user. HAHSED TEXT ONLY.
	 */
	private String password;
	/**
	 * The list of roles the user has.
	 */
	private List <UserAssignedRoleDAO> userRoles;
	/**
	 * The academic program being pursued by the user.
	 */
	private int pursuedAccProgramID;
	/**
	 * List of classes the user has taken.
	 */
	private List <Integer> takenClassesIDs;
	/**
	 * List of classes the user has planned to take.
	 */
	private List <Integer> scheduledClassesIDs;

	/**
	 * @param id                  - The ID number of the user.
	 * @param username            - The username of the user.
	 * @param password            - The password of the user. HAHSED TEXT ONLY.
	 * @param userRoles           - The list of roles the user has.
	 * @param pursuedAccProgramID - The academic program being pursued by the user.
	 * @param takenClassesIDs     - List of classes the user has taken.
	 * @param scheduledClassesIDs - List of classes the user has planned to take.
	 */
	public UserInfo( int id, String username, String password, List <UserAssignedRoleDAO> userRoles, int pursuedAccProgramID,
			List <Integer> takenClassesIDs, List <Integer> scheduledClassesIDs )
	{
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.userRoles = userRoles;
		this.pursuedAccProgramID = pursuedAccProgramID;
		this.takenClassesIDs = takenClassesIDs;
		this.scheduledClassesIDs = scheduledClassesIDs;
	}

	/**
	 * @param username            - The username of the user.
	 * @param password            - The password of the user. HAHSED TEXT ONLY.
	 * @param userRoles           - The list of roles the user has.
	 * @param pursuedAccProgramID - The academic program being pursued by the user.
	 * @param takenClassesIDs     - List of classes the user has taken.
	 * @param scheduledClassesIDs - List of classes the user has planned to take.
	 */
	public UserInfo( String username, String password, List <UserAssignedRoleDAO> userRoles, int pursuedAccProgramID, List <Integer> takenClassesIDs,
			List <Integer> scheduledClassesIDs )
	{
		super();
		this.username = username;
		this.password = password;
		this.userRoles = userRoles;
		this.pursuedAccProgramID = pursuedAccProgramID;
		this.takenClassesIDs = takenClassesIDs;
		this.scheduledClassesIDs = scheduledClassesIDs;
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
	 * @return the username
	 */
	public String getUsername()
	{
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername( String username )
	{
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword()
	{
		return password;
	}

	/**
	 * @param password the password to set. HASHED TEXT ONLY.
	 */
	public void setPassword( String password )
	{
		this.password = password;
	}

	/**
	 * @return the userRoles
	 */
	public List <UserAssignedRoleDAO> getUserRoles()
	{
		return userRoles;
	}

	/**
	 * @param userRoles the userRoles to set
	 */
	public void setUserRoles( List <UserAssignedRoleDAO> userRoles )
	{
		this.userRoles = userRoles;
	}

	/**
	 * @return the pursuedAccProgramID
	 */
	public int getPursuedAccProgramID()
	{
		return pursuedAccProgramID;
	}

	/**
	 * @param pursuedAccProgramID the pursuedAccProgramID to set
	 */
	public void setPursuedAccProgramID( int pursuedAccProgramID )
	{
		this.pursuedAccProgramID = pursuedAccProgramID;
	}

	/**
	 * @return the takenClassesIDs
	 */
	public List <Integer> getTakenClassesIDs()
	{
		return takenClassesIDs;
	}

	/**
	 * @param takenClassesIDs the takenClassesIDs to set
	 */
	public void setTakenClassesIDs( List <Integer> takenClassesIDs )
	{
		this.takenClassesIDs = takenClassesIDs;
	}

	/**
	 * @return the scheduledClassesIDs
	 */
	public List <Integer> getScheduledClassesIDs()
	{
		return scheduledClassesIDs;
	}

	/**
	 * @param scheduledClassesIDs the scheduledClassesIDs to set
	 */
	public void setScheduledClassesIDs( List <Integer> scheduledClassesIDs )
	{
		this.scheduledClassesIDs = scheduledClassesIDs;
	}

}
