package com.gradview.ui.ufo;

public class UFOFileSelect 
{
    private String filename;

    public UFOFileSelect( String filename )
    {
        this.filename = filename;
    }
    
    public UFOFileSelect(){}

    
    /** 
     * @return String
     */
    public String getFilename()
    {
        return filename;
    }

    public void setFilename( String filename )
    {
        this.filename = filename;
    }

    
}
