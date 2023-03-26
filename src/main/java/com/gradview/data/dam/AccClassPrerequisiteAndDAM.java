package com.gradview.data.dam;

/**
 * Represents the "acc-class-prerequisite" table columns.
 * 
 * @author Charles Davis
 */
public class AccClassPrerequisiteAndDAM 
{
    /**
     * The ID number of the prerequisite.
     * Default = -1
     */
    private int prerequisiteID = -1;
    /**
     * The ID number of the class the the prerequisite applies to.
     * Default = -1
     */
    private int requiredClassID = -1;
    
    /**
     * Full Constructor
     * @param prerequisiteID	
     * @param requiredClassID
     */
    public AccClassPrerequisiteAndDAM( int prerequisiteID, int requiredClassID )
    {
        this.prerequisiteID	= prerequisiteID;
        this.requiredClassID = requiredClassID;
    }

    /**
     * Class only constructor
     * @param requiredClassID
     */
    public AccClassPrerequisiteAndDAM(int requiredClassID)
    {;
        this.requiredClassID = requiredClassID;
    }

    /**
     * Default Constructor
     */
    public AccClassPrerequisiteAndDAM(){}

    public int getPrerequisiteID()
    {
        return prerequisiteID;
    }

    public void setPrerequisiteID( int prerequisiteID )
    {
        this.prerequisiteID = prerequisiteID;
    }

    public int getRequiredClassID()
    {
        return requiredClassID;
    }

    public void setRequiredClassID( int requiredClassID )
    {
        this.requiredClassID = requiredClassID;
    }


    
}
