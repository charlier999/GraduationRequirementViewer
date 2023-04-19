package com.gradview.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.gradview.model.Schedule;
import com.gradview.ui.ufo.UFOScheduleJsonImport;

@Controller
public class ScheduleController 
{
    private static final Logger logger = LoggerFactory.getLogger( ScheduleController.class );

    //#region
    // Display Maps -=- -=- -=- -=- -=- -=- -=- -=- -=- -=- -=- -=- -=- -=- -=-

    @GetMapping("/schedule")
    public String displayHome(Model model)
    {
        logger.info("displayHome: Has started at mapping /schedule");
        
        model.addAttribute("scheduleModel", new Schedule());
        logger.info("displayHome: Returning view schedule/home");
        return "schedule/home";
    }

    //#endregion

    //public String displayScheduleEdit(@PathVariable(""))



    //#region
    // Ajax Maps -=- -=- -=- -=- -=- -=- -=- -=- -=- -=- -=- -=- -=- -=- -=-

    @PostMapping("/ajax/schedule/importjson")
    public String ajaxImportJSON(@ModelAttribute String importJSON, Model model)
    {
        logger.info("ajaxImportJSON: Starting at /schedule/ajax/importjson");
        // Check to see if import string is blank
        if(importJSON.isBlank()) 
        {
            logger.warn("ajaxImportJSON: import string is blank.");
            logger.info("ajaxImportJSON: returning schedule/ajax/importJSON");
            model.addAttribute("scheduleImport", new UFOScheduleJsonImport());
            return "schedule/ajax/importJSON";
        }
        // Create Form Object and add it to attributes
        model.addAttribute("scheduleImport", new UFOScheduleJsonImport(importJSON));
        // Return ajax 
        logger.info("ajaxImportJSON: returning schedule/ajax/importJSON");
        return "schedule/ajax/importJSON";
    }
    //#endregion
}
