package com.gradview.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gradview.model.Schedule;
import com.gradview.ui.ufo.UFONewScheduleRow;
import com.gradview.ui.ufo.UFOScheduleJsonImport;

@Controller
public class ScheduleController 
{
    private static final Logger logger = LoggerFactory.getLogger( ScheduleController.class );

    //#region
    // Display Maps -=- -=- -=- -=- -=- -=- -=- -=- -=- -=- -=- -=- -=- -=- -=- -=- -=- -=- -=- -=- -=- -=- -=-

    @GetMapping("/schedule")
    public String displayHome(Model model)
    {
        logger.info("displayHome: Has started at mapping /schedule");
        
        model.addAttribute("scheduleModel", new Schedule());
        logger.info("displayHome: Returning view schedule/home");
        return "schedule/home";
    }

    @GetMapping("/schedule/edit")
    public String displayEdit(Model model)
    {
        logger.info("displayEdit: Has started at mapping /schedule/edit");
        logger.info("displayEdit: Returning view schedule/edit");
        return "schedule/edit";
    }

    //#endregion

    //#region
    // POST Maps -=- -=- -=- -=- -=- -=- -=- -=- -=- -=- -=- -=- -=- -=- -=- -=- -=- -=- -=- -=- -=- -=- -=-

    @PostMapping("/schedule/importjson")
    public String postImportJSON(@ModelAttribute UFOScheduleJsonImport scheduleImport, Model model)
    {
        //{[0,CST-321,1]}
        logger.info("postImportJSON: Has started at mapping /schedule/importjson");
        // Pase Schedule from import string.
        Schedule output = Schedule.parse(scheduleImport.getQuery());
        // Check for defalt schedule
        if(output.equals(new Schedule()))
        {
            logger.info("postImportJSON: Schedule not found. Returning schedule home page.");
            // Return to home page
            return this.displayHome(model);
        }
        else
        {
            logger.info("postImportJSON: Schedule found. Updateing local storage");
            
            model.addAttribute("scheduleImport", output.toString());
            return "schedule/scripts/addScheduleToLocalStorage";
        }
    }

    @PostMapping("/schedule/edit/newRow")
    public String postNewScheduleRow(@ModelAttribute UFONewScheduleRow row, Model model)
    {
        logger.info("postNewScheduleRow: Has started at mapping /schedule/edit/newRow");
        Schedule output = Schedule.parse(row.schedule);
        // set the newRow id to the size of the schedule rows list.
        row.newRow.id = output.rows.size();
        // add the newRow to the schedule rows list
        output.rows.add(row.newRow);
        // add schedule string to attribute.
        model.addAttribute("scheduleImport", output.toString());
        logger.info("postImportJSON: Schedule found. Updateing local storage");
        return "schedule/scripts/editScheduleToLocalStorage";
    }
    //#endregion

    //public String displayScheduleEdit(@PathVariable(""))



    //#region
    // Ajax Maps -=- -=- -=- -=- -=- -=- -=- -=- -=- -=- -=- -=- -=- -=- -=- -=- -=- -=- -=- -=- -=- -=- -=-

    @PostMapping("/ajax/schedule/importjson")
    public String ajaxImportJSON(@RequestParam String importJSON, Model model)
    {
        logger.info("ajaxImportJSON: Starting at /ajax/schedule/importjson");
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

    @PostMapping("/ajax/schedule/newschedulerow")
    public String ajaxScheduleNewRow(@RequestParam String importJSON, Model model)
    {
        logger.info("ajaxScheduleNewRow: Starting at /ajax/schedule/newschedulerow");
        UFONewScheduleRow ufoOutput;
        // Check to see if import string is blank
        if(importJSON.isBlank()) 
        {
            logger.warn("ajaxScheduleNewRow: import string is blank.");
            ufoOutput = new UFONewScheduleRow(new Schedule().toString());
        }
        else
        {
            Schedule schedule = Schedule.parse(importJSON);
            if(schedule.equals(new Schedule())) ufoOutput = new UFONewScheduleRow(new Schedule().toString()); 
            ufoOutput = new UFONewScheduleRow(schedule.toString());
        }
        model.addAttribute("ufoNewScheduleRow", ufoOutput);
        // Return ajax 
        logger.info("ajaxScheduleNewRow: returning schedule/ajax/newScheduleRow");
        return "schedule/ajax/newScheduleRow";
    }

    @PostMapping("/ajax/schedule/table")
    public String ajaxScheduleTable(@RequestParam String importJSON, Model model)
    {
        logger.info("ajaxScheduleTable: Starting at /ajax/schedule/newschedulerow");
        Schedule output;
        // Check to see if import string is blank
        if(importJSON.isBlank()) 
        {
            logger.warn("ajaxScheduleTable: import string is blank.");
            output = new Schedule();
        }
        else
        {
            Schedule schedule = Schedule.parse(importJSON);
            if(schedule.equals(new Schedule())) output = new Schedule(); 
            output = schedule;
        }
        model.addAttribute("scheduleTable", output);
        // Return ajax 
        logger.info("ajaxScheduleTable: returning schedule/ajax/scheduletable");
        return "schedule/ajax/scheduletable";
    }


    //#endregion
}
