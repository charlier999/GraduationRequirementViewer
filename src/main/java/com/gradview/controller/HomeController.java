package com.gradview.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController
{
	private static final Logger logger = LoggerFactory.getLogger( HomeController.class );
	
	/**
	 * Displays the home page.
	 */
	@GetMapping( "/home" )
	public String displayHome(Model model)
	{
		logger.info( "displayLogin: Has started at mapping '/home'." );
		// Display the Login Form View
		logger.info( "displayLogin: Returning view 'home'." );
		return "home";
	}
}
