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
import org.springframework.web.bind.annotation.PostMapping;

import com.gradview.exception.NoRowsFoundException;
import com.gradview.model.AccProgram;
import com.gradview.service.ProgramImportService;
import com.gradview.ui.ufo.UFOFileSelect;

@Controller
public class ProgramController 
{
    private static final Logger logger = LoggerFactory.getLogger( ProgramController.class );
    @Autowired
    private ProgramImportService programImportService;
    
    @GetMapping("/program/import")
    public String displayProgramImportPage(Model model)
    {
        logger.info("displayClassImportPage: Has started at mapping `/program/import");
        List<String> fileNames = new ArrayList<>();
        // Retreive list of files
        try
        {
            fileNames = programImportService.retrieveFormatedFiles();
        }
        catch ( IOException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // attach list of files to model
        model.addAttribute("filenames", fileNames);
        model.addAttribute("formObject", new UFOFileSelect());
        logger.info("displayClassImportPage: Returning view program/importTool");
        return "programs/importTool"; 
    }

    @PostMapping("/program/import")
    public String doProgramImport(@ModelAttribute UFOFileSelect fileSelect, Model model)
    {
        logger.info("doProgramImport: Has started at mapping `/program/import");
        List<String> fileNames = new ArrayList<>();
        List<AccProgram> programs = new ArrayList<>();
        // Retreive list of files
        try
        {
            
            // Retrive list of formated files
            fileNames = programImportService.retrieveFormatedFiles();
            // Import selected file
            programs = programImportService.importProgramsWithoutClasses(fileSelect.getFilename());
            
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
        catch ( Exception e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // attach list of files to model
        model.addAttribute("filenames", fileNames);
        model.addAttribute("programs", programs);
        model.addAttribute("formObject", fileSelect);
        logger.info("doProgramImport: Returning view program/importTool");
        return "programs/importTool"; 
    }
}
