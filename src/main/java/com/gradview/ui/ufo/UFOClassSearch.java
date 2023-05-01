package com.gradview.ui.ufo;

public class UFOClassSearch 
{
    private String querry;
    private String tableHeader;
    
    public UFOClassSearch( String querry, String tableHeader )
    {
        this.querry = querry;
        this.tableHeader = tableHeader;
    }

    public UFOClassSearch(){}

    
    /** 
     * @return String
     */
    public String getQuerry()
    {
        return querry;
    }

    public void setQuerry( String querry )
    {
        this.querry = querry;
    }

    public String getTableHeader()
    {
        return tableHeader;
    }

    public void setTableHeader( String tableHeader )
    {
        this.tableHeader = tableHeader;
    }
    
    
}
