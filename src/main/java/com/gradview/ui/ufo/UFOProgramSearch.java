package com.gradview.ui.ufo;

public class UFOProgramSearch 
{
    private String querry;
    private String tableHeader;
    
    public UFOProgramSearch( String querry, String tableHeader )
    {
        this.querry = querry;
        this.tableHeader = tableHeader;
    }

    public UFOProgramSearch(){}

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
