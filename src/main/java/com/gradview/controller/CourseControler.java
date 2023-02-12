package com.gradview.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.gradview.exception.NoRowsFoundException;
import com.gradview.model.AccClass;
import com.gradview.service.ClassService;

@Controller
public class CourseControler 
{
    private static final Logger logger = LoggerFactory.getLogger( CourseControler.class );
    @Autowired
    private ClassService classService;

    @GetMapping("/class/{number}")
    public String displayClass(@PathVariable("number") String number, Model model)
    {
        logger.info("displayClass: Has started at mapping `/class/{number}");
        logger.info("displayClass: Number: " + number + " requested to be viewed");
        try
        {
            List<AccClass> output = classService.getClassByNumber(number);
            model.addAttribute("classes", output);
            logger.info("displayClass: Class is being returned to view 'classes/view'");
            return "classes/view";
        }
        catch ( DataAccessException e )
        {
            logger.error("displayClass: DataAccessException occured");
            e.printStackTrace();
            return "error";
        }
        catch ( NoRowsFoundException e )
        {
            logger.warn("displayClass: NoRowsFoundException occured");
            e.printStackTrace();
            return "error";
        }
        catch ( Exception e )
        {
            logger.error("displayClass: Exception occured");
            e.printStackTrace();
            return "error";
        }
    }
}
