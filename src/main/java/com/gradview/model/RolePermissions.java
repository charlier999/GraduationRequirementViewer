/**
 * 
 */
package com.gradview.model;

/**
 * The representation of permissions a role has.
 * 
 * @author Charles Davis
 */
public class RolePermissions
{
	/**
	 * The ID number of the role.
	 */
	private int roleID;
	/**
	 * The list of permission IDs the role has.
	 */
	private int[] permissionIDs;

	/**
	 * @param roleID
	 * @param permissionIDs
	 */
	public RolePermissions( int roleID, int[] permissionIDs )
	{
		super();
		this.roleID = roleID;
		this.permissionIDs = permissionIDs;
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

	/**
	 * @return the permissionIDs
	 */
	public int[] getPermissionIDs()
	{
		return permissionIDs;
	}

	/**
	 * @param permissionIDs the permissionIDs to set
	 */
	public void setPermissionIDs( int[] permissionIDs )
	{
		this.permissionIDs = permissionIDs;
	}

}
