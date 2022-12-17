/**
 * 
 */
package com.gradview.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.gradview.exception.UsernameTakenException;
import com.gradview.service.UserSecurityService;
import com.gradview.ui.ufo.RegistrationUFO;

/**
 * @author Charles Davis
 */
@Controller
public class UserRegistrationController
{
	private static final Logger logger = LoggerFactory.getLogger( UserRegistrationController.class );

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserSecurityService userSecurityService;

	/**
	 * Displays the registration page.
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping( "/registration" )
	public String displayRegistration( Model model )
	{
		logger.info( "displayRegistration: Has started at mapping '/registration' ." );
		// Display the Registration Form View
		model.addAttribute( "title", "Registration Form" );
		model.addAttribute( "newUser", new RegistrationUFO() );

		// Display registration.html view
		logger.info( "displayRegistration: Returning view 'loginRegistration/registration'." );
		return "loginRegistration/registration";
	}

	/**
	 * Uses the user inputed {@link User} to add to the database.
	 * 
	 * @param User          ({@link User}) - The {@link User} given by the user to
	 *                      enter into the database.
	 * @param bindingResult (BindingResult) - Error checking.
	 * @param model
	 * @return
	 */
	@PostMapping( "/registration/doRegistration" )
	public String doRegistration(@ModelAttribute RegistrationUFO user, BindingResult bindingResult, Model model )
	{
		
		logger.info( "doRegistration: Has started at mapping '/registration/doRegistration' ." );
		// Check for validation errors
		if ( bindingResult.hasErrors() )
		{
			logger.info( "doRegistration: Form data contains errors." );
			model.addAttribute( "user", user );
			logger.info( "doRegistration: Returning view 'loginRegistration/registration'." );
			return "loginRegistration/registration";
		}
		try
		{
			logger.info( "doRegistration: Adding user to database." );
			//user.password = this.passwordEncoder.encode( user.getPassword() );
			this.userSecurityService.addUser( user );
			logger.info( "doRegistration: Returning view 'loginRegistration/registrationSuccess'." );
			return "loginRegistration/login";
		}
		catch ( DataAccessException e )
		{
			logger.error( "doRegistration: DataAccessException occred. " + e.getLocalizedMessage() );
			model.addAttribute( "user", user );
			model.addAttribute( "invalid",
					"A server error ocured. Please try again. If persistant, contact site admins." );
			logger.info( "doRegistration: Returning view 'loginRegistration/registration'." );
			return "loginRegistration/registration";
		}
		catch ( UsernameTakenException e )
		{
			logger.info( "doRegistration: Entered username is already taken by another user." );
			model.addAttribute( "user", user );
			model.addAttribute( "invalid", "Username already taken." );
			logger.info( "doRegistration: Returning view 'loginRegistration/registration'." );
			return "loginRegistration/registration";
		}
	}

}
