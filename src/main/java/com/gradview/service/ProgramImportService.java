package com.gradview.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.gradview.data.dam.AccProgramClassesDAM;
import com.gradview.data.dam.AccProgramDAM;
import com.gradview.data.dao.AccProgramClassesDAO;
import com.gradview.data.dao.AccProgramDAO;
import com.gradview.data.dao.AccProgramElectivesCreditsDAO;
import com.gradview.data.dao.AccProgramGeneralEducationCreditsDAO;
import com.gradview.data.dao.AccProgramMajorCreditsDAO;
import com.gradview.data.dao.AccProgramTotalCreditsDAO;
import com.gradview.exception.NoRowsFoundException;
import com.gradview.model.AccClass;
import com.gradview.model.AccProgram;

@Service
public class ProgramImportService 
{
    private static final Logger logger = LoggerFactory.getLogger(ProgramImportService.class);
    //private static final boolean RUNIMPORT = true;
    private InputStream courseSteam;

    private ComponentLogger compLogger = new ComponentLogger("gradview.service.ProgramImportService");


    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private AccProgramDAO accProgramDAO;
    @Autowired
    private AccProgramClassesDAO accProgramClassesDAO;
    @Autowired
    private AccProgramElectivesCreditsDAO accProgramElectivesCreditsDAO;
    @Autowired
    private AccProgramGeneralEducationCreditsDAO accProgramGeneralEducationCreditsDAO;
    @Autowired
    private AccProgramMajorCreditsDAO accProgramMajorCreditsDAO;
    @Autowired
    private AccProgramTotalCreditsDAO accProgramTotalCreditsDAO;
    @Autowired
    private ClassService classService;
    @Autowired
    private CourseImportService courseImportService;

    
    /** 
     * @return List<LogMessage>
     */
    public List<LogMessage> getLogs()
    {
        return this.compLogger.getLogs();
    }

    public List<String> retrieveVertSliceFiles() throws IOException
    {
        logger.info("retrieveVertSliceFiles: Starting");
        logger.info("retrieveVertSliceFiles: Retrieveing Resources");
        Resource[] resources = applicationContext.getResources("classpath:static/VSlice/*");
        List<String> output = new ArrayList<>();
        for(int i = 0; i < resources.length; i++)
        {
            output.add(resources[i].getFilename());
        }
        logger.info("retrieveVertSliceFiles: Returning file names");
        return output;
    }

    public List<String> retrieveFormatedFiles() throws IOException
    {
        logger.info("retrieveFormatedFiles: Starting");
        logger.info("retrieveFormatedFiles: Retrieveing Resources");
        Resource[] resources = applicationContext.getResources("classpath:static/ProgramsWithClasses/*");
        List<String> output = new ArrayList<>();
        for(int i = 0; i < resources.length; i++)
        {
            output.add(resources[i].getFilename());
        }
        logger.info("retrieveFormatedFiles: Returning file names");
        return output;
    }

    public List<AccProgram> importVertSliceProgram(String filename) throws FileNotFoundException
    {
        filename = "VSlice/" + filename;
        compLogger.clear();
        compLogger.info("importVertSliceProgram", "Starting");
        compLogger.info("importVertSliceProgram", "Retrieve lines from file");
        List<String> lines = this.retrieveLinesFromFile(filename);
        compLogger.info("importVertSliceProgram", "Lines Retreived");
        compLogger.info("importVertSliceProgram", "Clump Lines");
        List<List<String>> clumpedLines = this.clumpLines(lines);
        compLogger.info("importVertSliceProgram", "Lines have been clumped");
        compLogger.info("importVertSliceProgram", "Import Courses");
        this.courseImportService.insertClassesToDB(
            this.courseImportService.classModelListToDAMList(
                this.courseImportService.clumpListToClassWithoutPrereq(
                    this.pullCourseClumps(clumpedLines))
            ));
        compLogger.info("importVertSliceProgram", "Import Courses Complete");
        compLogger.info("importVertSliceProgram", "Import Courses' Prerequistes Start");
        try
        {
            this.courseImportService.importAllPrerequisitesFromClumps(this.pullCourseClumps(clumpedLines));
        }        
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        compLogger.info("importVertSliceProgram", "Import Courses' Prerequistes Finished");
        compLogger.info("importVertSliceProgram", "Pull Program From Clumps");
        List<AccProgram> programs = this.clumpLinesToPrograms(this.pullProgramClumps(clumpedLines));
        compLogger.info("importVertSliceProgram", "Clumps have been converted to programs");
        compLogger.info("importVertSliceProgram", "Inserting programs into the database");
        this.insertPrograms(programs);
        compLogger.info("importVertSliceProgram", "All programs have been inserted");
        compLogger.info("importVertSliceProgram", "Getting programIDs from inserted programs.");
        programs = this.updateProgramModelsWithIDs(programs);
        compLogger.info("importVertSliceProgram", "ProgramIDs have been found");
        compLogger.info("importVertSliceProgram", "Inserting program required classes Starting");
        this.insertRequiredPrograms(programs);
        compLogger.info("importVertSliceProgram", "Inserting program required classes Complete");
        compLogger.info("importVertSliceProgram", "Finished");
        return programs;
    }

    public List<AccProgram> importPrograms(String filename) throws FileNotFoundException
    {
        filename = "ProgramsWithClasses/" + filename;
        compLogger.clear();
        compLogger.info("importPrograms", "Starting");
        compLogger.info("importPrograms", "Retrieve lines from file");
        List<String> lines = this.retrieveLinesFromFile(filename);
        compLogger.info("importPrograms", "Lines Retreived");
        compLogger.info("importPrograms", "Clump Lines");
        List<List<String>> clumpedLines = this.clumpLines(lines);
        compLogger.info("importPrograms", "Lines have been clumped");
        compLogger.info("importPrograms", "Convert clumpted lines to programs");
        List<AccProgram> programs = this.clumpLinesToPrograms(clumpedLines);
        compLogger.info("importPrograms", "Clumps have been converted to programs");
        compLogger.info("importPrograms", "Inserting programs into the database");
        this.insertPrograms(programs);
        compLogger.info("importPrograms", "All programs have been inserted");
        compLogger.info("importPrograms", "Getting programIDs from inserted programs.");
        programs = this.updateProgramModelsWithIDs(programs);
        compLogger.info("importPrograms", "ProgramIDs have been found");
        compLogger.info("importPrograms", "Inserting program required classes Starting");
        this.insertRequiredPrograms(programs);
        compLogger.info("importPrograms", "Inserting program required classes Complete");
        compLogger.info("importPrograms", "Finished");
        return programs;
    }

    /**
     * Retireves the lines from the file.
     * @return List< String > of all lines from the file.
     * @throws FileNotFoundException
     */
    private List<String> retrieveLinesFromFile(String filename) throws FileNotFoundException
    {
        compLogger.info("retrieveLinesFromFile", "Starting");
        this.courseSteam = new FileInputStream(ResourceUtils.getFile("classpath:static/" + filename));
        Scanner sc = new Scanner(this.courseSteam);
        List<String> output = new ArrayList<>();
        compLogger.info("retrieveLinesFromFile", "Iterating Through File");
        while(sc.hasNext())
        {
            output.add(sc.nextLine());
        }
        sc.close();
        compLogger.info("retrieveLinesFromFile", "Iteration complete.");
        compLogger.info("retrieveLinesFromFile", "Returning List of lines.");
        return output;
    }

    /**
     * Iterates through the inputed lines.
     * @param input The lines to iterate through.
     * @return List of Lists of clumps of strings.
     */
    private List<List<String>> clumpLines(List<String> input)
    {
        compLogger.info("clumpLines", "Starting");
        List<List< String > > output = new ArrayList<List<String>>();
        List<String> clump = new ArrayList<>();
        compLogger.info("clumpLines", "Iterating through input List");
        for(int i = 0; i < input.size(); i++)
        {
            logger.debug("clumpLines Iteration " + i);
            // If blank line
            if(this.removeComment(input.get(i)).length() == 0)
            {

                // Add clump to output list
                if(clump.size() > 0) output.add(clump);
                // Create new clump list. If you don't, you get a bunch of the same clump 
                //  because a new memory assignment is needed for each clump.
                clump = new ArrayList<>();
            }
            else // If not blank line
            {
                // Add line to clump
                clump.add(this.removeIndentFromString(this.removeComment(input.get(i))));
                if(i == input.size() -1) output.add(clump);
            }
        }

        compLogger.info("clumpLines", "Returning output List<List<String>>");
        return output;
    }

    private List<List<String>> pullProgramClumps(List<List<String>> input)
    {
        compLogger.info("pullProgramClumps", "Starting");
        List<List<String>> output = new ArrayList<>();
        for(int i = 0; i < input.size(); i++)
        {
            logger.debug("pullProgramClumps: Clump Iteration " + i);
            if(input.get(i).size() > 5) output.add(input.get(i));
            else if (input.get(i).size() == 1 && input.get(i).get(0).equals("Courses:")) break;
        }
        compLogger.info("pullProgramClumps", "Returning");
        return output;
    }

    private List<List<String>> pullCourseClumps(List<List<String>> input)
    {
        compLogger.info("pullCourseClumps", "Starting");
        List<List<String>> output = new ArrayList<>();
        Boolean isCoursesReached = false;
        for(int i = 0; i < input.size(); i++)
        {
            logger.debug("pullCourseClumps: Clump Iteration " + i);
            if(input.get(i).get(0).equals("Courses:")) isCoursesReached = true;
            else if(isCoursesReached) 
            {
                if(input.get(i).get(0).charAt(3) == '-')
                {
                    String courseNumber = input.get(i).get(0);
                    courseNumber = courseNumber.replace(":", "");
                    input.get(i).set(0, courseNumber);
                }
                output.add(input.get(i));
            }
        }
        compLogger.info("pullCourseClumps", "Returning");
        return output; 
    }

    /**
     * Takes a list of a list of strings containg Program information and converts it to a list of {@link AccProgram}s
     * @param input The list of a list of strings containg Program information.
     * @return List of {@link AccProgram}s
     */
    private List<AccProgram> clumpLinesToPrograms(List<List<String>> input)
    {
        compLogger.info("clumpLinesToPrograms", "Starting");
        List<AccProgram> output = new ArrayList<>();
        compLogger.info("clumpLinesToPrograms", "Iterating through input List");
        for(int i = 0; i < input.size(); i++)
        {
            logger.debug("clumpLinesToPrograms: Clump Iteration " + i);
            output.add(this.clumpToProgram(input.get(i)));
        }
        compLogger.info("clumpLinesToPrograms", "Returning");
        return output;
    }

    /**
     * Converts a string clump to a Program Object
     * @param clump
     * @return
     */
    private AccProgram clumpToProgram(List<String> clump)
    {
        AccProgram program = new AccProgram();
        List<Integer> requiredCourses = new ArrayList<>();
        Boolean reachedCourseList = false;
        for(int j = 0; j < clump.size(); j++)
        {
            if(j == 0) // If first line
            {
                // Set program name
                program.setName(clump.get(j));
                // Set program level
                program.setLevel(this.findProgramLevelInString(clump.get(j)));
                // Seach Name for BA of art or sci
                boolean[] artSci = this.isBaArtOrSci(clump.get(j));
                // Set Program Ba of Art
                program.setBaOfArts(artSci[0]);
                // Set Program Ba of Sci
                program.setBaOfScience(artSci[1]);
                // Iterate once
                j++;
            }
            if(j == 1) // If second line
            {
                // Set program description
                program.setDescription(clump.get(j));
                // Iterate twice
                j = j + 2;
            }
            if(j > 3 && !reachedCourseList)
            {
                // If program level is bachelors
                if(program.getLevel().equals(AccProgram.LEVEL_BACHELOR))
                {
                    // Get credit type
                    boolean[] creditType = this.isCreditType(clump.get(j));
                    if(creditType[3]) // Program total 
                    {
                        // Set total minimum credits
                        program.setTotalMinCredits(this.getIntInString(clump.get(j)));
                    }
                    else if(creditType[1]) // Major
                    {
                        // Set major credits
                        logger.info("clumpToProgram: clump["+j+"]: " + clump.get(j));
                        program.setMajorMinCredits(this.getIntInString(clump.get(j)));
                        program.setMajorMaxCredits(program.getMajorMinCredits());
                    }
                    else // The other three
                    {
                        int[] creditRange = this.getLineCreditRange(clump.get(j));
                        if(creditType[0]) // Gen ed
                        {
                            // Set gen ed credits
                            program.setGenEdMinCredits(creditRange[0]);
                            program.setGenEdMaxCredits(creditRange[1]);
                        }
                        
                        else if(creditType[2]) // Elective
                        {
                            // Set elective credits
                            program.setElectiveMinCredits(creditRange[0]);
                            program.setElectiveMaxCredits(creditRange[1]);
                        }
                    }
                }
                else if( // Program is other levels
                program.getLevel().equals(AccProgram.LEVEL_GRAD_CERT) ||
                program.getLevel().equals(AccProgram.LEVEL_BRIDGE2MASTER) ||
                program.getLevel().equals(AccProgram.LEVEL_DOCTOR) ||
                program.getLevel().equals(AccProgram.LEVEL_MASTER) ||
                program.getLevel().equals(AccProgram.LEVEL_MINOR)
                )
                {
                    // Set gen ed credits
                    program.setGenEdMinCredits(0);
                    program.setGenEdMaxCredits(0);
                    // Set major credits
                    program.setMajorMinCredits(0);
                    program.setMajorMaxCredits(0);
                    // Set elective credits
                    program.setElectiveMinCredits(0);
                    program.setElectiveMaxCredits(0);
                    // Set total minimum credits
                    program.setTotalMinCredits(this.getIntInString(clump.get(j)));
                }
                else
                {
                    compLogger.warn("clumpLinesToPrograms", 
                    "Program `" + program.getName() + "`'s Program Level is not a known level.");
                }
            }
            else if(reachedCourseList)
            {
                //Pull class ids from list and assign it to program object
                this.getRequiredClassIDFromString(clump.get(j));
                requiredCourses.add(this.getRequiredClassIDsFromString(clump.get(j))[0]);
            }
            if(clump.get(j).equals("Required Major Courses")) reachedCourseList = true;
        }
        Object[] uncastArr = requiredCourses.toArray();
        int[] intArr = new int[uncastArr.length];
        for(int i = 0; i < uncastArr.length; i++)
        {
            intArr[i] = (int) uncastArr[i];
        }
        program.setRequiredMajorClasses(intArr);
        return program;
    }

    /**
     * 
     * @param input
     * @return
     */
    private int[] getRequiredClassIDsFromString(String input)
    {
        List<Integer> output = new ArrayList<>();
        List<String> courseNumbers = this.getRequiredClassIDFromString(input);
        // Iterate through splits
        for(int i = 0; i < courseNumbers.size(); i++)
        {
            try
            {
                // Get basic class info from course number
                List<AccClass> classes = this.classService.getBasicClassByNumber(courseNumbers.get(i));
                // Loop thorugh found classes
                for (AccClass accClass : classes) 
                {
                    // Add class id to output list
                    output.add(accClass.getId());
                }
            }
            catch ( DataAccessException e )
            {
                compLogger.error("getRequiredClassIDsFromString", courseNumbers.get(i) + " caused DataAccessException. Message: " + e.getMessage());
            }
            catch ( NoRowsFoundException e )
            {
                compLogger.warn("getRequiredClassIDsFromString", courseNumbers.get(i) + " not in database.");
            }
            catch ( Exception e )
            {
                compLogger.error("getRequiredClassIDsFromString", courseNumbers.get(i) + " caused Exception. Message: " + e.getMessage());
            }
        }
        Integer[] res = output.toArray(new Integer[output.size()]);
        int[] finalRes = new int[res.length];
        for(int i = 0; i <res.length; i++)
        {
            finalRes[i] = res[i].intValue();
        }
        return finalRes;
    } 

    private List<String> getRequiredClassIDFromString(String input)
    {
        List<String> output = new ArrayList<>();
        Pattern regexPattern = Pattern.compile("[a-zA-Z]{3}-\\d{3}(:|[a-zA-Z]{1,2}:|)");
        Matcher matcher = regexPattern.matcher(input);
        while(matcher.find()) output.add(input.substring(matcher.start(), matcher.end()));
        return output;
    }

    /**
     * Checks string for Credit type strings
     * @param input String to check for credit types
     * @return {genEd, major, elective, total}
     */
    private boolean[] isCreditType(String input)
    {
        logger.debug("isCreditType: Starting");
        boolean[] output = {false, false, false, false};
        if(input.contains("General Education")) output[0] = true;
        if(!input.contains("Required Major Courses"))
            if(input.contains("Major")) output[1] = true;
        if(input.contains("Electives")) output[2] = true;
        if(input.contains("Bachelor")) output[3] = true;
        logger.debug("isCreditType: Returning "+ output[0] + "," + output[1] + "," + output[2] + "," + output[3]);
        return output;
    }

    /**
     * Checks string for BA in arts or science
     * @param input String to search thorugh
     * @return boolean[] {art,sci}
     */
    private boolean[] isBaArtOrSci(String input)
    {
        logger.debug("isBaArtOrSci: Starting");
        boolean[] output =  {false,false};
        if(input.contains(AccProgram.BA_ART))
        {
            output = new boolean[] {true,false};
        }
        else if(input.contains(AccProgram.BA_SCI))
        {
            output = new boolean[] {false,true};
        }
        logger.debug("isBaArtOrSci: Returning " + output[0] + "," + output[1]);
        return output;
    }

    /**
     * Finds what program level is contained within String.
     * @param input The string to find program level.
     * @return Found program level or null if not found.
     */
    private String findProgramLevelInString(String input)
    {
        logger.debug("findProgramLevelInString: Starting");
        if(input.contains(AccProgram.LEVEL_BACHELOR))
        {
            return AccProgram.LEVEL_BACHELOR;
        }
        else if(input.contains(AccProgram.LEVEL_MINOR))
        {
            return AccProgram.LEVEL_MINOR;
        }
        else if(input.contains(AccProgram.LEVEL_GRAD_CERT))
        {
            return AccProgram.LEVEL_GRAD_CERT;
        }
        else if(input.contains(AccProgram.LEVEL_BRIDGE2MASTER))
        {
            return AccProgram.LEVEL_BRIDGE2MASTER;
        }
        else if(input.contains(AccProgram.LEVEL_MASTER))
        {
            return AccProgram.LEVEL_MASTER;
        }
        else if(input.contains(AccProgram.LEVEL_DOCTOR))
        {
            return AccProgram.LEVEL_DOCTOR;
        }
        else
        {
            compLogger.warn("findProgramLevelInString", "Input `" + input + "` is not a known program level.");
            return null;
        }
    }

    /**
     * Removes indent spaces from the front of a String
     * @param input The String to remove indent spaces from
     * @return Input string without indent spaces
     */
    private String removeIndentFromString(String input)
    {
        logger.debug("removeIndentFromString: Starting");
        String output = null;
        output = input.trim();
        // If output was not filled
        if(output == null)
        {
            logger.debug("removeIndentFromString: Returning " + input);
            return input;
        }
        logger.debug("removeIndentFromString: Returning " + output);
        return output;
    }

    /**
     * Retrieves the credit range from the degree requirements entrie
     * @param input degree requirements entrie
     * @return int[2] {min, max}
     */
    private int[] getLineCreditRange(String input)
    {
        logger.debug("getLineCreditRange: Starting Input: " + input);
        // Get the index of the "-" in input string
        int dashInd = input.indexOf("-");
        if(dashInd < 0) return new int[] {0, 0};
        // Retrieve string value of credit minimum 
        String creditMinS = input.substring((dashInd-2), dashInd);
        // Retrieve string value of credit maximum 
        String creditMaxS = input.substring((dashInd+1), (dashInd+3));
        // Convert Min String to int
        int creditMinI = Integer.parseInt(creditMinS.replaceAll("\\s", ""));
        // Convert Max String to int
        int creditMaxI = Integer.parseInt(creditMaxS.replaceAll("\\s", ""));
        // Create output array
        int[] output = {creditMinI, creditMaxI};
        // Return output
        logger.debug("getLineCreditRange: Returning");
        return output;
    }

    /**
     * Finds int within string and retuns it.
     * @param input
     * @return
     * @implNote It removes all non digits from string. So if there are two seperate numbers in input, 
     *  they will be attached to eachother. EXMP: "404APPLE404" returns 404404.
     */
    private int getIntInString(String input)
    {
        logger.debug("getIntInString: Runs");
        try
        {
            return Integer.parseInt(input.replaceAll("\\D+", ""));
        }
        catch (NumberFormatException ex)
        {
            logger.error("getIntInString: Number format excpetion occured with input: " + input);
            throw ex;
        }
    }


    private void insertPrograms(List<AccProgram> input)
    {
        compLogger.info("insertPrograms","Starting");
        compLogger.info("insertPrograms","Iterating thorugh program list");
        for(int i = 0; i <input.size(); i++)
        {
            logger.info("insertPrograms: Inserting program " + i);
            try
            {
                if(!this.validateProgram(input.get(i)))
                {
                    compLogger.warn("insertPrograms", 
                    "Invalid Program. Program Iteration: "+ i);
                }
                // Insert base program
                accProgramDAO.create(input.get(i).toProgramDAM());
                // Check 
                // Retreive program ID by name
                input.get(i).setId(
                    accProgramDAO.search(AccProgramDAO.COL_NAME, input.get(i).getName())
                    .get(0).getId());
                // Insert gen-ed credits
                accProgramGeneralEducationCreditsDAO.create(input.get(i).toGenEdCredDAM());
                // Insert Major credits
                accProgramMajorCreditsDAO.create(input.get(i).toMajorCredDAM());
                // Insert Elective credits
                accProgramElectivesCreditsDAO.create(input.get(i).toElectivCredeDAM());
                // Insert total credits
                accProgramTotalCreditsDAO.create(input.get(i).toTotalCredDAM());
            }
            catch ( DataAccessException e )
            {
                compLogger.error("insertPrograms","Data Access Exception Occured on Program `"+ input.get(i).getName() + "`. Message: " + e.getMessage());
                logger.error("insertPrograms: Data Access Exception Occured. Printing Stack Trace");
                e.printStackTrace();
            }
            catch ( Exception e )
            {
                compLogger.error("insertPrograms","Exception Occured.");
                logger.error("insertPrograms: Exception Occured. Printing Stack Trace");
                e.printStackTrace();
            }
        }
        compLogger.info("insertPrograms","Finished");
    }

    private boolean validateProgram(AccProgram program)
    {
        if(program == null)
        {
            compLogger.warn("validateProgram", "Program is null.");
            return false;
        }
        else 
        {
            boolean critNullFound = false;
            if(program.getName() == null)
            {
                critNullFound = true;
                compLogger.warn("validateProgram", "Program is null.");
            }
            if(program.getDescription() == null)
            {
                critNullFound = true;
                compLogger.warn("validateProgram", "Program is null.");
            }
            if(critNullFound)
            {
                compLogger.warn("validateProgram", "Program contains null values. Program: " + program.toString());
                return false;
            }
        }
        return true;
    }

    /**
     * Updates programs with id numbers found in the databas
     * @param programs The list of programs to update id numbers of
     * @return The list of updated programs
     */
    private List<AccProgram> updateProgramModelsWithIDs(List<AccProgram> programs)
    {
        compLogger.info("updateProgramModelsWithIDs","Starting");
        compLogger.info("updateProgramModelsWithIDs","Iterating through programs");
        for(int programCount = 0; programCount < programs.size(); programCount++)
        {
            AccProgram tempProgram = programs.get(programCount);
            try
            {
                List< AccProgramDAM > foundPrograms = this.accProgramDAO.search(AccProgramDAO.COL_NAME, tempProgram.getName());
                if(foundPrograms.size() > 1) // If more then one program found
                {
                    compLogger.warn("updateProgramModelsWithIDs",
                        "More then one program returned with the name of : " + tempProgram.getName());
                }
                else // If one program is found
                {
                    // Set tempProgram's ID to id found in search.
                    tempProgram.setId(foundPrograms.get(0).getId());
                }
            }
            catch ( DataAccessException e )
            {
                compLogger.error("updateProgramModelsWithIDs",
                        "DataAccessException occured with Program: `" + tempProgram.getName() + "` Message: " + e.getMessage());
            }
            catch ( NoRowsFoundException e )
            {
                compLogger.warn("updateProgramModelsWithIDs",
                        "More then one program returned with the name of : " + tempProgram.getName());
            }
            catch ( Exception e )
            {
                compLogger.error("updateProgramModelsWithIDs",
                    "Exception occured with Program: `" + tempProgram.getName() + "` Message: " + e.getMessage());
            }
            programs.set(programCount, tempProgram);
        }
        // Return found IDs
        return programs;
    }


    private void insertRequiredPrograms(List<AccProgram> programs)
    {
        compLogger.info("insertRequiredPrograms","Starting");
        for (AccProgram accProgram : programs) 
        {
            int[] reqClassIDs = accProgram.getRequiredMajorClasses();
            if(reqClassIDs == null)
            {
                compLogger.warn("insertRequiredPrograms",
                    "Program `" + accProgram.getName() + "` has no required Classes.");
            }
            else
            {
                List<AccProgramClassesDAM> programClassesDAMs = new ArrayList<>();
                for (int classID : reqClassIDs) 
                {
                    AccProgramClassesDAM tempClass = new AccProgramClassesDAM(accProgram.getId(), classID);
                    programClassesDAMs.add(tempClass);
                }
                for (AccProgramClassesDAM programClassesDAM : programClassesDAMs)
                {
                    try
                    {
                        this.accProgramClassesDAO.create(programClassesDAM);
                    }
                    catch ( DataAccessException e )
                    {
                        compLogger.error("insertRequiredPrograms",
                            "Data Access Exception Occured on Program Class`"
                            + programClassesDAM.getProgramID() + " " 
                            + programClassesDAM.getClassID() +"`. Message: " + e.getMessage());
                        logger.error("insertRequiredPrograms: Data Access Exception Occured. Printing Stack Trace");
                        e.printStackTrace();
                    }
                    catch ( Exception e )
                    {
                        compLogger.error("insertRequiredPrograms",
                            "Exception Occured on Program Class`"
                            + programClassesDAM.getProgramID() + " " 
                            + programClassesDAM.getClassID() +"`. Message: " + e.getMessage());
                        logger.error("insertRequiredPrograms: Exception Occured. Printing Stack Trace");
                        e.printStackTrace();
                    }
                }
            }
        }
        compLogger.info("insertRequiredPrograms","Finished");
    }

    /**
     * Removes the comment from a String.
     * @param input The string containing a comment.
     * @return The inputed string without the comment.
     */
    private String removeComment(String input)
    {
        int commentIndex = input.indexOf("//");
        if (commentIndex != -1) input = input.substring(0, commentIndex);
        return input;
    }
}
