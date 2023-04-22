/**
 * 
 */
package com.gradview.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


/**
 * For misc. services that only controller methods would need to use.
 * 
 * @author Charles Davis
 */
@Controller
public class MiscServicesController
{
	private static final Logger logger = LoggerFactory.getLogger( MiscServicesController.class );

	@GetMapping("/test")
	public String displayTestingHome(Model model)
	{
		logger.info( "displayLogin: Has started at mapping '/home'." );
		//cis.importClassesWithoutRequisites();
		//pis.importProgramsWithoutClasses();
		logger.info( "displayLogin: Returning view 'home'." );
		return "test/home";
	}
	
	@GetMapping("/ajax/navbar")
	public String ajaxNavBar(Model model)
	{
		logger.info( "ajaxNavBar: Has started at mapping '/ajax/navbar'." );
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth.getPrincipal().equals("anonymousUser")) model.addAttribute("isAuthenticated", false);
		else model.addAttribute("isAuthenticated", auth.isAuthenticated());
		return "ajax/navbar";
	}
	
}
