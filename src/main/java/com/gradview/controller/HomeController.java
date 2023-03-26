package com.gradview.controller;

import java.io.FileNotFoundException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.gradview.service.CourseImportService;
import com.gradview.service.ProgramImportService;

@Controller
public class HomeController
{
	private static final Logger logger = LoggerFactory.getLogger( HomeController.class );

	@Autowired
	CourseImportService cis;
	@Autowired
	ProgramImportService pis;
	
	@GetMapping("/")
	public String rootRedirect(Model model, HttpServletResponse httpResponse ) throws FileNotFoundException
	{
		return this.displayHome(model, httpResponse);
	}
	
	/**
	 * Displays the home page.
	 * @throws FileNotFoundException
	 */
	@GetMapping( "/home" )
	public String displayHome( Model model, HttpServletResponse httpResponse ) throws FileNotFoundException
	{
		logger.info( "displayLogin: Has started at mapping '/home'." );
		//cis.importClassesWithoutRequisites();
		//pis.importProgramsWithoutClasses();
		logger.info( "displayLogin: Returning view 'home'." );
		return "home";
	}
}
