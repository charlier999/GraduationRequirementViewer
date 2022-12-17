/**
 * 
 */
package com.gradview.data.dao;

/**
 * Represents the "role-permissions" table columns.
 * 
 * @author Charles Davis
 */
public class RolePermissionsDAO
{
	/**
	 * The ID number of the Permission.
	 */
	private int permissionID;

	/**
	 * The ID number of the Role.
	 */
	private int roleID;

	/**
	 * @param permissionID - The ID number of the Permission.
	 * @param roleID - The ID number of the Role.
	 */
	public RolePermissionsDAO( int permissionID, int roleID )
	{
		super();
		this.permissionID = permissionID;
		this.roleID = roleID;
	}

	/**
	 * @return the permissionID
	 */
	public int getPermissionID()
	{
		return permissionID;
	}

	/**
	 * @param permissionID the permissionID to set
	 */
	public void setPermissionID( int permissionID )
	{
		this.permissionID = permissionID;
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
