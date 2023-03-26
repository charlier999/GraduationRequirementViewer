package com.gradview.data.dam;

/**
 * Represents the "acc-class-prerequisite" table columns.
 * 
 * @author Charles Davis
 */
public class AccClassPrerequisiteDAM 
{
    /**
     * The ID number of the prerequisite.
     * Default = -1
     */
    private int id = -1;
    /**
     * The ID number of the class the the prerequisite applies to.
     * Default = -1
     */
    private int classID = -1;
    
    /**
     * Full Constructor
     * @param id
     * @param classID
     */
    public AccClassPrerequisiteDAM( int id, int classID )
    {
        this.id = id;
        this.classID = classID;
    }

    /**
     * Class only constructor
     * @param classID
     */
    public AccClassPrerequisiteDAM(int classID)
    {;
        this.classID = classID;
    }

    /**
     * Default Constructor
     */
    public AccClassPrerequisiteDAM(){}

    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    public int getClassID()
    {
        return classID;
    }

    public void setClassID( int classID )
    {
        this.classID = classID;
    }

    
}
