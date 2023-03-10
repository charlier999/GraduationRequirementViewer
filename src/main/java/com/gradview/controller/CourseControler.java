package com.gradview.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

import com.gradview.data.dao.AccClassDAO;
import com.gradview.exception.NoRowsFoundException;
import com.gradview.model.AccClass;
import com.gradview.service.ClassService;
import com.gradview.service.CourseImportService;
import com.gradview.ui.ufo.UFOClassSearch;
import com.gradview.ui.ufo.UFOFileSelect;;

@Controller
public class CourseControler 
{
    private static final Logger logger = LoggerFactory.getLogger( CourseControler.class );
    @Autowired
    private ClassService classService;
    @Autowired
    private CourseImportService courseImportService;

    /**
     * Displays the class home page.
     * @param model
     * @return
     */
    @GetMapping("/class")
    public String displayHome(Model model)
    {
        logger.info("displayHome: Has started at mapping `/class");
        model.addAttribute("searchVals", new UFOClassSearch());
        logger.info("displayHome: Returning view classes/home");
        return "classes/home";       
    }

    @PostMapping("/class/search")
    public String doSearch(@ModelAttribute UFOClassSearch search, Model model)
    {
        logger.info("doSearch: Has started at post mapping `/class/search`");
        List<AccClass> output = new ArrayList<>();
        try
        {
            output = classService.search(search);
            model.addAttribute("classes", output);
            model.addAttribute("searchVals", search);
            logger.info("doSearch: Classes are being returned to view 'classes/searchResults'");
            return "classes/searchResults";
        }
        catch ( DataAccessException e )
        {
            logger.error("doSearch: DataAccessException occured");
            e.printStackTrace();
            return "error";
        }
        catch ( NoRowsFoundException e )
        {
            logger.warn("doSearch: NoRowsFoundException occured");
            model.addAttribute("classes", output);
            model.addAttribute("searchVals", new UFOClassSearch());
            logger.info("doSearch: Classes are being returned to view 'classes/searchResults'");
            return "classes/searchResults";
        }
        catch ( Exception e )
        {
            logger.error("doSearch: Exception occured");
            e.printStackTrace();
            return "error";
        }
    }
    
    /**
     * Displays the class page from the url paramater number.
     * @param number the class number to retrieve class from.
     * @param model
     * @return view
     */
    @GetMapping("/class/number/{number}")
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

    @PostMapping("/class/import")
    public String doClassImport(@ModelAttribute UFOFileSelect fileSelect, Model model)
    {
        logger.info("doClassImport: Has started at mapping `/class/import");
        List<String> fileNames = new ArrayList<>();
        List<AccClass> classes = new ArrayList<>();
        // Retreive list of files
        try
        {
            
            // Retrive list of formated files
            fileNames = courseImportService.retrieveFormatedFiles();
            // Import selected file
            courseImportService.importClassesWithoutRequisites(fileSelect.getFilename());
            // Pull letter segement
            char[] chars = fileSelect.getFilename().toCharArray();
            // 
            classes = classService.search(new UFOClassSearch(String.valueOf(chars[19]) + "__-___", AccClassDAO.COL_NUMBER));
            
        }
        catch ( IOException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch ( DataAccessException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch ( NoRowsFoundException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch ( Exception e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // attach list of files to model
        model.addAttribute("filenames", fileNames);
        model.addAttribute("classes", classes);
        model.addAttribute("formObject", fileSelect);
        logger.info("doClassImport: Returning view classes/importTool");
        return "classes/importTool"; 
    }

    @GetMapping("/class/import")
    public String displayClassImportPage(Model model)
    {
        logger.info("displayClassImportPage: Has started at mapping `/class/import");
        List<String> fileNames = new ArrayList<>();
        // Retreive list of files
        try
        {
            fileNames = courseImportService.retrieveFormatedFiles();
        }
        catch ( IOException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // attach list of files to model
        model.addAttribute("filenames", fileNames);
        model.addAttribute("formObject", new UFOFileSelect());
        logger.info("displayClassImportPage: Returning view classes/importTool");
        return "classes/importTool"; 
    }
}
