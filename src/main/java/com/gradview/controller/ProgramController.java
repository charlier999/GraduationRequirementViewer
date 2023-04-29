package com.gradview.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.gradview.data.dam.AccProgramDAM;
import com.gradview.data.dao.AccProgramDAO;
import com.gradview.exception.NoRowsFoundException;
import com.gradview.model.AccClass;
import com.gradview.model.AccProgram;
import com.gradview.service.ClassService;
import com.gradview.service.ProgramChartGenService;
import com.gradview.service.ProgramImportService;
import com.gradview.service.ProgramService;
import com.gradview.ui.ufo.UFOFileSelect;
import com.gradview.ui.ufo.UFOProgramSearch;

@Controller
public class ProgramController 
{
    private static final Logger logger = LoggerFactory.getLogger( ProgramController.class );
    @Autowired
    private ProgramImportService programImportService;
    @Autowired
    private AccProgramDAO accProgramDAO;
    @Autowired
    private ProgramService programService;
    @Autowired
    private ClassService classService;
    @Autowired
    private ProgramChartGenService programChartGenService;
    
    @GetMapping("/program")
    public String displayHome(Model model)
    {
        logger.info("displayHome: Has started at mapping `/program`");
        model.addAttribute("searchVals", new UFOProgramSearch());

        List<AccProgram> output = new ArrayList<>();
        try
        {
            // Get list of programDAMs
            List<AccProgramDAM> programDAMs= accProgramDAO.search(AccProgramDAO.COL_ID ,"%");
            // Convert programDAMs to AccPrograms with only needed info
            for(AccProgramDAM programDAM : programDAMs)
            {
                List<AccProgram> programs = this.programService.getProgramsByName(programDAM.getName());
                for(AccProgram program : programs) output.add(program);
            }
            model.addAttribute("programs", output);
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
            model.addAttribute("programs", output);
            model.addAttribute("searchVals", new UFOProgramSearch());
        }
        catch ( Exception e )
        {
            logger.error("doSearch: Exception occured");
            e.printStackTrace();
            return "error";
        }

        logger.info("displayHome: Returning view programs/home");
        return "programs/home";       
    }

    @PostMapping("/program/search")
    public String doSearch(@ModelAttribute UFOProgramSearch search, Model model)
    {
        logger.info("doSearch: Has started at post mapping `/class/search`");
        List<AccProgram> output = new ArrayList<>();
        try
        {
            if(search.getTableHeader() == null)
            {
                search.setTableHeader(AccProgramDAO.COL_NAME);
                search.setQuerry(" ");
            }
            // Get list of programDAMs
            List<AccProgramDAM> programDAMs =  accProgramDAO.search(search.getTableHeader(), "%" + search.getQuerry() + "%");
            // Convert programDAMs to AccPrograms with only needed info
            for(AccProgramDAM programDAM : programDAMs)
            {
                List<AccProgram> programs = this.programService.getProgramsByName(programDAM.getName());
                for(AccProgram program : programs) output.add(program);
            }
            model.addAttribute("programs", output);
            model.addAttribute("searchVals", search);
            logger.info("doSearch: Programs are being returned to view 'programs/searchResults'");
            return "programs/searchResults";
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
            model.addAttribute("programs", output);
            model.addAttribute("searchVals", new UFOProgramSearch());
            logger.info("doSearch: Programs are being returned to view 'programs/searchResults'");
            return "programs/searchResults";
        }
        catch ( Exception e )
        {
            logger.error("doSearch: Exception occured");
            e.printStackTrace();
            return "error";
        }
    }

    @GetMapping("/program/name/{name}")
    public String displayProgram(@PathVariable("name") String name, Model model)
    {
        logger.info("displayProgram: Has started at mapping `/program/name/{name}");
        List<AccProgram> output = new ArrayList<>();
        List<AccClass> classes = new ArrayList<>();
        String programChartData = "[['Error In Generating Chart Occured']]";
        logger.info("displayProgram: " + name + " requested to be viewed");
        try
        {
            output = programService.getProgramsByName(name);
            if(output != null)
            {
                for( int i = 0; i < output.size(); i++)
                {

                    int[] classIDsInt = output.get(i).getRequiredMajorClasses();
                    Integer[] classIDsInteger = Arrays.stream(classIDsInt).boxed().toArray(Integer[]::new);
                    List<Integer> classIDList = new ArrayList<Integer>(Arrays.asList(classIDsInteger));
                    Set<Integer> set = new HashSet<>(classIDList);
                    classIDList.clear();
                    classIDList.addAll(set);
                    classes = this.classService.getBasicClassesByClassIDs(classIDList);
                    classes = this.sortClassList(classes); 
                    classIDsInt = classIDList.stream().mapToInt(j -> j).toArray();
                    //Arrays.sort(classIDsInt);
                    output.get(i).setRequiredMajorClasses(classIDsInt);
                }
                programChartData = this.programChartGenService.genOrgChartDataByProgramModel(output.get(0));
            }
            else
            {
                logger.info("displayProgram: " + name + " cannot be found");
            }

        }
        catch ( DataAccessException e )
        {
            logger.error("displayProgram: DataAccessException occured");
            e.printStackTrace();
            return "error";
        }
        catch ( NoRowsFoundException e )
        {
            logger.warn("displayProgram: NoRowsFoundException occured");
            return this.displayHome(model);
        }
        catch ( Exception e )
        {
            logger.error("displayProgram: Exception occured");
            e.printStackTrace();
            return "error";
        }
        
        
        model.addAttribute("programChartData", programChartData);
        model.addAttribute("programs", output);
        model.addAttribute("reqClasses", classes);
        logger.info("displayProgram: Program is being returned to view 'programs/view'");
        return "programs/view";
    }

    @GetMapping("/program/import")
    public String displayProgramImportPage(Model model)
    {
        logger.info("displayProgramImportPage: Has started at mapping `/program/import");
        if(SecurityContextHolder.getContext().getAuthentication()
			.getPrincipal().equals("anonymousUser")) return "home";
        List<String> fileNames = new ArrayList<>();
        // Retreive list of files
        try
        {
            fileNames = programImportService.retrieveVertSliceFiles();
        }
        catch ( IOException e )
        {
            e.printStackTrace();
            return "error";
        }
        // attach list of files to model
        model.addAttribute("filenames", fileNames);
        model.addAttribute("formObject", new UFOFileSelect());
        logger.info("displayProgramImportPage: Returning view program/importTool");
        return "programs/importTool"; 
    }

    @PostMapping("/program/import")
    public String doProgramImport(@ModelAttribute UFOFileSelect fileSelect, Model model)
    {
        logger.info("doProgramImport: Has started at mapping `/program/import");
        if(SecurityContextHolder.getContext().getAuthentication()
			.getPrincipal().equals("anonymousUser")) return "home";
        List<String> fileNames = new ArrayList<>();
        List<AccProgram> programs = new ArrayList<>();
        // Retreive list of files
        try
        {
            
            // Retrive list of formated files
            fileNames = programImportService.retrieveVertSliceFiles();
            // Import selected file
            //programs = programImportService.importPrograms(fileSelect.getFilename());
            programs = programImportService.importVertSliceProgram(fileSelect.getFilename());
            
        }
        catch ( IOException e )
        {
            logger.error("doProgramImport: IOException Occured: ", e);
            e.printStackTrace();
        }
        catch ( DataAccessException e )
        {
            logger.error("doProgramImport: DataAccessException Occured: ", e);
            e.printStackTrace();
        }
        catch ( Exception e )
        {
            logger.error("doProgramImport: Exception Occured: ", e);
            e.printStackTrace();
        }
        // attach list of files to model
        model.addAttribute("filenames", fileNames);
        model.addAttribute("programs", programs);
        model.addAttribute("formObject", fileSelect);
        model.addAttribute("logs", this.programImportService.getLogs());
        logger.info("doProgramImport: Returning view program/importTool");
        return "programs/importTool"; 
    }







    /**
     * Sorts the {@link AccClass Class} List alabeticlay
     * @param classes
     * @return
     */
    private List<AccClass> sortClassList(List<AccClass> classes)
    {
        Collections.sort(classes, new Comparator<AccClass>()
        {
            @Override
            public int compare(AccClass class1, AccClass class2)
            {
                return class1.getNumber().compareTo(class2.getNumber());
            }
        });
        return classes;
    }
}
