package com.gradview.data.dam;

public class UserDAM
{
    private int id;
    private String username;
    private String password;
    private boolean enabled;

    public UserDAM( int id, String username, String password, boolean enabled )
    {
        super();
        this.id = id;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
    }

    public UserDAM( String username, String password )
    {
        super();
        this.username = username;
        this.password = password;
        this.enabled = true;
    }

    
    /** 
     * @return int
     */
    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername( String username )
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword( String password )
    {
        this.password = password;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled( boolean enabled )
    {
        this.enabled = enabled;
    }

}
