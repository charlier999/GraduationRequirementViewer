/**
 * 
 */
package com.gradview.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Displays the Login page
 * @author Charles Davis
 *
 */
@Controller
public class UserLoginController
{
	private static final Logger logger = LoggerFactory.getLogger( UserLoginController.class );
	
	/**
	 * Displays the login page.
	 */
	@GetMapping( "/login" )
	public String displayLogin(Model model)
	{
		logger.info( "displayLogin: Has started at mapping '/login'." );
		// Display the Login Form View
		logger.info( "displayLogin: Returning view 'loginRegistration/login'." );
		return "loginRegistration/login";
	}
}
