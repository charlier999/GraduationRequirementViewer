/**
 * 
 */
package com.gradview.data.dao;

/**
 * Represents the "acc-class-prerequisite" table columns.
 * 
 * @author Charles Davis
 */
public class AccClassPrerequisiteDAO
{
	/**
	 * The ID of the Class.
	 */
	private int classID;
	/**
	 * The ID of the Class required to take the Class.
	 */
	private int requireClassID;

	/**
	 * @param classID        - The ID of the Class.
	 * @param requireClassID - The ID of the Class required to take the Class.
	 */
	public AccClassPrerequisiteDAO( int classID, int requireClassID )
	{
		super();
		this.classID = classID;
		this.requireClassID = requireClassID;
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

	/**
	 * @return the requireClassID
	 */
	public int getRequireClassID()
	{
		return requireClassID;
	}

	/**
	 * @param requireClassID the requireClassID to set
	 */
	public void setRequireClassID( int requireClassID )
	{
		this.requireClassID = requireClassID;
	}

}
