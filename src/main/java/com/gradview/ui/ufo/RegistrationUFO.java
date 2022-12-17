/**
 * 
 */
package com.gradview.ui.ufo;

import com.gradview.data.dao.UsersDAO;

/**
 * The HTML form object for the registration page.
 * 
 * @author Charles Davis
 */
public class RegistrationUFO extends UsersDAO
{

	/**
	 * The username the user entered in the form.
	 */
	public String username;
	/**
	 * The password the user entered in the from. THIS IS PLAIN TEXT.
	 */
	public String password;
	
	public RegistrationUFO( String username, String password )
	{
		super( username, password );
	}

	public RegistrationUFO()
	{
		// TODO Auto-generated constructor stub
	}
}
