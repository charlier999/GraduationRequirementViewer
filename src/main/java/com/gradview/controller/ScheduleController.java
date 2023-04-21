package com.gradview.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gradview.data.dam.AccClassDAM;
import com.gradview.data.dao.AccClassDAO;
import com.gradview.exception.NoRowsFoundException;
import com.gradview.model.AccClass;
import com.gradview.model.AccProgram;
import com.gradview.model.Schedule;
import com.gradview.model.ScheduleRow;
import com.gradview.service.ClassService;
import com.gradview.service.ProgramService;
import com.gradview.ui.ufo.UFONewScheduleRow;
import com.gradview.ui.ufo.UFOScheduleJsonImport;
import com.gradview.ui.uio.UIOCompareProgram;

@Controller
public class ScheduleController 
{
    private static final Logger logger = LoggerFactory.getLogger( ScheduleController.class );

    @Autowired
    AccClassDAO accClassDAO;
    @Autowired
    ClassService classService;
    @Autowired
    ProgramService programService;

    //#region Get Mappings

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

    @GetMapping("/schedule/compare")
    public String displayCompareProgram(Model model)
    {
        logger.info("displayCompareProgram: Has started at mapping /schedule/compare/{program}");
        logger.info("displayCompareProgram: No path variable included.");
        model.addAttribute("urlProgram", null);
        logger.info("displayCompareProgram: Returning view schedule/compare");
        return "schedule/compare";
    }
    
    @GetMapping("/schedule/compare/{urlProgram}")
    public String displayCompareProgram(@PathVariable String urlProgram, Model model)
    {
        logger.info("displayCompareProgram: Has started at mapping /schedule/compare/{program}");
        logger.info("displayCompareProgram: Path variable included.");
        model.addAttribute("urlProgram", urlProgram);
        logger.info("displayCompareProgram: Returning view schedule/compare");
        return "schedule/compare";
    }

    //#endregion

    //#region Post Mappings

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


    //#endregion Get Mappings

    //#region Ajax Maps

    @PostMapping("/ajax/schedule/importjson")
    public String ajaxImportJSON(@RequestParam String importJSON, Model model)
    {
        logger.info("ajaxImportJSON: Starting at /ajax/schedule/importjson");
        // Create Form Object and add it to attributes
        model.addAttribute("scheduleImport", 
            new UFOScheduleJsonImport(this.importJsonToSchedule(importJSON).toString()));
        // Return ajax 
        logger.info("ajaxImportJSON: returning schedule/ajax/importJSON");
        return "schedule/ajax/importJSON";
    }

    @PostMapping("/ajax/schedule/newschedulerow")
    public String ajaxScheduleNewRow(@RequestParam String importJSON, Model model)
    {
        logger.info("ajaxScheduleNewRow: Starting at /ajax/schedule/newschedulerow");
        model.addAttribute("ufoNewScheduleRow", 
            new UFONewScheduleRow(this.importJsonToSchedule(importJSON).toString()));
        model.addAttribute("courseOptions", this.courseNumberList());
        // Return ajax 
        logger.info("ajaxScheduleNewRow: returning schedule/ajax/newScheduleRow");
        return "schedule/ajax/newScheduleRow";
    }

    @PostMapping("/ajax/schedule/table")
    public String ajaxScheduleTable(@RequestParam String importJSON, Model model)
    {
        logger.info("ajaxScheduleTable: Starting at /ajax/schedule/newschedulerow");
        model.addAttribute("scheduleTable", this.importJsonToSchedule(importJSON));
        // Return ajax 
        logger.info("ajaxScheduleTable: returning schedule/ajax/scheduletable");
        return "schedule/ajax/scheduletable";
    }

    @GetMapping("/ajax/schedule/programSelectForm")
    public String ajaxProgramSelectForm(Model model)
    {
        logger.info("ajaxProgramSelectForm: Starting at /ajax/schedule/programSelectForm");
        List<String> programNames = new ArrayList<>();
        try
        {
            // TODO: Add getProgramNames to ProgramService.java
            // Get list of programs
            List<AccProgram> programs = this.programService.getProgramsByName("%");
            // Extract names from programs list
            for(AccProgram program : programs) programNames.add(program.getName());
        }
        catch ( DataAccessException e )
        {
            logger.error("ajaxProgramSelectForm: DataAccessException Occured. ", e);
            e.printStackTrace();
            return "error";
        }
        catch ( NoRowsFoundException e )
        {
            logger.info("ajaxProgramSelectForm: No programs found");
        }
        catch ( Exception e )
        {
            logger.error("ajaxProgramSelectForm: Exception Occured. ", e);
            e.printStackTrace();
            return "error";
        }
        model.addAttribute("programNames", programNames);
        logger.info("ajaxProgramSelectForm: returning schedule/ajax/selectProgramForm");
        return "schedule/ajax/selectProgramForm";
    }

    @PostMapping("/ajax/schedule/programCompare")
    public String ajaxProgramCompare(@RequestParam String importJSON, @RequestParam String programName, Model model)
    {
        logger.info("ajaxProgramCompare: Starting at /ajax/schedule/programCompare");
        // Convert importJSON to Schedule
        model.addAttribute("scheduleCompare", this.importJsonToSchedule(importJSON));
        // Output Values
        List<UIOCompareProgram> comparePrograms = new ArrayList<>();
        // Temp values
        List<AccProgram> programs = new ArrayList<>();
        List<List<AccClass>> passedClasses = new ArrayList<>();
        List<List<AccClass>> requriredClasses = new ArrayList<>();
        try
        {
            programs = this.programService.getProgramsByName(programName);
            // Pull courses from schedule3
            for(ScheduleRow row : this.importJsonToSchedule(importJSON).rows)
            {
                if(row.isPassed)
                {
                    // Get Class info
                    List<AccClass> tempAccClasses = this.classService.getBasicClassByNumber(row.courseNumber);
                    // Add all retirved classes to refrence list
                    passedClasses.add(tempAccClasses);
                }
            }
            // Pull courses from programs
            for(AccProgram program : programs)
            {
                // Get Class info
                List<AccClass> tempAccClasses = this.classService.getBasicClassesByClassIDs(
                    Arrays.stream(program.getRequiredMajorClasses()).boxed()
                    .collect(Collectors.toList()));
                // Add all retirved classes to refrence list
                requriredClasses.add(tempAccClasses);
            }
        }
        catch ( DataAccessException e )
        {
            logger.error("ajaxProgramCompare: DataAccessException Occured. ", e);
            e.printStackTrace();
            return "error";
        }
        catch ( NoRowsFoundException e )
        {
            logger.info("ajaxProgramCompare: No programs found");
        }
        catch ( Exception e )
        {
            logger.error("ajaxProgramCompare: Exception Occured. ", e);
            e.printStackTrace();
            return "error";
        }
        // Create UI Objects
        for(int count = 0; count < programs.size(); count++)
        {
            comparePrograms.add(
                new UIOCompareProgram(programs.get(count), 
                passedClasses.get(count), requriredClasses.get(count)));
            logger.info("ajaxProgramCompare: UIOCompareProgram["+count+"]"+comparePrograms.get(count));
        }

        model.addAttribute("programsCompare", comparePrograms);
        // Return ajax 
        logger.info("ajaxProgramCompare: returning schedule/ajax/programCompare");
        return "schedule/ajax/programCompare"; 
    }

    //#endregion Ajax Maps

    //#region Helper Functions
    /**
     * Returns a List of of course numbers.
     * @return List of of course numbers.
     */
    private List<String> courseNumberList()
    {
        List<String> classNumbers = new ArrayList<>();  
        List< AccClassDAM > classList = new ArrayList<>();
        try
        {
            // Get list of classes
            classList = this.accClassDAO.getAll();
        }
        catch ( DataAccessException e )
        {
            logger.error("courseNumberList: DataAccessException Occured", e);
            e.printStackTrace();
        }
        catch ( NoRowsFoundException e )
        {
            logger.warn("courseNumberList: NoRowsFoundException Occured", e);
        }
        catch ( Exception e )
        {
            logger.error("courseNumberList: Exception Occured", e);
            e.printStackTrace();
        }
        // Pull list class numbers from class list
        for(AccClassDAM iClass : classList) classNumbers.add(iClass.getNumber());
        return classNumbers;
    }

    /**
     * Converts the importJSON String to {@link Schedule}
     * @param importJSON the import JSON String to convert to {@link Schedule}
     * @return The converted {@link Schedule}
     */
    private Schedule importJsonToSchedule(String importJSON)
    {
        Schedule output;
        // Check to see if import string is blank
        if(importJSON.isBlank()) 
        {
            output = new Schedule();
        }
        else
        {
            Schedule schedule = Schedule.parse(importJSON);
            if(schedule.equals(new Schedule())) output = new Schedule(); 
            output = schedule;
        }
        return output;
    }
    
    /**
     * Remove duplicate {@link AccClass classes} by classID from list of {@link AccClass classes}.
     * @param classes List of {@link AccClass classes} to have duplicate {@link AccClass classes} removed from.
     * @return List of {@link AccClass classes} with duplicate {@link AccClass classes} removed.
     */
    private List<AccClass> removeDuplicatClasses(List<AccClass> classes)
    {
        // Loop through all courses
        for(int i = 0; i < classes.size(); i++)
        {
            // Loop through all courses
            for(int j = 0; j < classes.size(); j++)
            {
                // If first loop's class id equals second loop's class id
                // AND first loop iteration does not equal second loop iteration 
                // Remove duplicate using second loop's iteration value
                if(classes.get(i).getId() == classes.get(j).getId() && i != j) classes.remove(j);
            }
        }
        return classes;
    }
    //#endregion Helper Functions
}
