package com.gradview.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
public class AccClassPrerequisite 
{
    public int id;
    public int rootClassID;
    /**
     * And = True; Or = False
     */
    public boolean andOr;
    public List<Integer> classIDs;

    public AccClassPrerequisite( int id, int rootClassID, boolean andOr, List< Integer > classIDs )
    {
        this.id = id;
        this.rootClassID = rootClassID;
        this.andOr = andOr;
        this.classIDs = classIDs;
    }

    public AccClassPrerequisite()
    {
        this.id = -1;
        this.rootClassID = -1;
        this.classIDs = new ArrayList<>();
    }

    

    
}
