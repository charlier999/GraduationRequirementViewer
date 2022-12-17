package com.gradview.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.gradview.exception.NoRowsFoundException;
import com.gradview.exception.TooManyRowsException;
import com.gradview.exception.UserNotFoundException;
import com.gradview.service.UserSecurityService;

@Controller
public class HomeController
{
	private static final Logger logger = LoggerFactory.getLogger( HomeController.class );

	@Autowired
	private UserSecurityService userSecurityService;

	/**
	 * Displays the home page.
	 * 
	 * @throws IOException
	 * @throws DataAccessException
	 * @throws NoRowsFoundException 
	 * @throws UserNotFoundException 
	 * @throws TooManyRowsException 
	 */
	@GetMapping( "/home" )
	public String displayHome( Model model, HttpServletResponse httpResponse ) throws DataAccessException, IOException, TooManyRowsException, UserNotFoundException, NoRowsFoundException
	{
		logger.info( "displayLogin: Has started at mapping '/home'." );
		model.addAttribute( "roles",  this.userSecurityService.getCurrentSessionUser().getUserRoles() );
		// Display the Login Form View
		logger.info( "displayLogin: Returning view 'home'." );
		return "home";
	}
}
