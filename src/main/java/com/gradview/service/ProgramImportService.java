package com.gradview.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.gradview.data.dao.AccProgramDAO;
import com.gradview.data.dao.AccProgramElectivesCreditsDAO;
import com.gradview.data.dao.AccProgramGeneralEducationCreditsDAO;
import com.gradview.data.dao.AccProgramMajorCreditsDAO;
import com.gradview.data.dao.AccProgramTotalCreditsDAO;
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
    private AccProgramElectivesCreditsDAO accProgramElectivesCreditsDAO;
    @Autowired
    private AccProgramGeneralEducationCreditsDAO accProgramGeneralEducationCreditsDAO;
    @Autowired
    private AccProgramMajorCreditsDAO accProgramMajorCreditsDAO;
    @Autowired
    private AccProgramTotalCreditsDAO accProgramTotalCreditsDAO;

    public List<LogMessage> getLogs()
    {
        return this.compLogger.getLogs();
    }

    public List<String> retrieveFormatedFiles() throws IOException
    {
        
        logger.info("retrieveFormatedFiles: Starting");
        logger.info("retrieveFormatedFiles: Retrieveing Resources");
        Resource[] resources = applicationContext.getResources("classpath:static/ProgramsNoClasses/*");
        List<String> output = new ArrayList<>();
        for(int i = 0; i < resources.length; i++)
        {
            output.add(resources[i].getFilename());
        }
        logger.info("retrieveFormatedFiles: Returning file names");
        return output;
    }

    public List<AccProgram> importProgramsWithoutClasses(String filename) throws FileNotFoundException
    {
        compLogger.clear();
        compLogger.info("importProgramsWithoutClasses", "Starting");
        compLogger.info("importProgramsWithoutClasses", "Retrieve lines from file");
        List<String> lines = this.retrieveLinesFromFile(filename);
        compLogger.info("importProgramsWithoutClasses", "Lines Retreived");
        compLogger.info("importProgramsWithoutClasses", "Clump Lines");
        List<List<String>> clumpedLines = this.clumpLines(lines);
        compLogger.info("importProgramsWithoutClasses", "Lines have been clumped");
        compLogger.info("importProgramsWithoutClasses", "Convert clumpted lines to programs");
        List<AccProgram> programs = this.clumpLinesToPrograms(clumpedLines);
        compLogger.info("importProgramsWithoutClasses", "Clumps have been converted to programs");
        compLogger.info("importProgramsWithoutClasses", "Inserting programs into the database");
        insertPrograms(programs);
        compLogger.info("importProgramsWithoutClasses", "All programs have been inserted");
        compLogger.info("importProgramsWithoutClasses", "Finished");
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
        this.courseSteam = new FileInputStream(ResourceUtils.getFile("classpath:static/ProgramsNoClasses/" + filename));
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
            if(input.get(i).length() == 0)
            {
                // Add clump to output list
                output.add(clump);
                // Create new clump list. If you don't, you get a bunch of the same clump 
                //  because a new memory assignment is needed for each clump.
                clump = new ArrayList<>();
            }
            else // If not blank line
            {
                // Add line to clump
                clump.add(removeIndentFromString(input.get(i)));
            }
        }
        compLogger.info("clumpLines", "Returning output List<List<String>>");
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
            AccProgram program = new AccProgram();
            for(int j = 0; j < input.get(i).size(); j++)
            {
                if(j == 0) // If first line
                {
                    // Set program name
                    program.setName(input.get(i).get(j));
                    // Set program level
                    program.setLevel(this.findProgramLevelInString(input.get(i).get(j)));
                    // Seach Name for BA of art or sci
                    boolean[] artSci = this.isBaArtOrSci(input.get(i).get(j));
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
                    program.setDescription(input.get(i).get(j));
                    // Iterate twice
                    j = j + 2;
                }
                else // line 2+
                {
                    // If program level is bachelors
                    if(program.getLevel().equals(AccProgram.LEVEL_BACHELOR))
                    {
                        // Get credit type
                        boolean[] creditType = this.isCreditType(input.get(i).get(j));
                        if(creditType[3]) // Program total 
                        {
                            // Set total minimum credits
                            program.setTotalMinCredits(this.getIntInString(input.get(i).get(j)));
                        }
                        else if(creditType[1]) // Major
                            {
                                // Set major credits
                                program.setMajorMinCredits(this.getIntInString(input.get(i).get(j)));
                                program.setMajorMaxCredits(program.getMajorMinCredits());
                            }
                        else // The other three
                        {
                            int[] creditRange = this.getLineCreditRange(input.get(i).get(j));
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
                        program.setTotalMinCredits(this.getIntInString(input.get(i).get(j)));
                    }
                    else
                    {
                        compLogger.warn("clumpLinesToPrograms", 
                            "Program `" + program.getName() + "`'s Program Level is not a known level.");
                    }
                }
            }
            output.add(program);

        }
        compLogger.info("clumpLinesToPrograms", "Returning");
        return output;
    }

    /**
     * Checks string for Credit type strings
     * @param input String to check for credit types
     * @return {genEd, major, elective, total}
     */
    private boolean[] isCreditType(String input)
    {
        logger.info("isCreditType: Starting");
        boolean[] output = {false, false, false, false};
        if(input.contains("General Education")) output[0] = true;
        if(input.contains("Major")) output[1] = true;
        if(input.contains("Electives")) output[2] = true;
        if(input.contains("Bachelor")) output[3] = true;
        logger.info("isCreditType: Returning "+ output[0] + "," + output[1] + "," + output[2] + "," + output[3]);
        return output;
    }

    /**
     * Checks string for BA in arts or science
     * @param input String to search thorugh
     * @return boolean[] {art,sci}
     */
    private boolean[] isBaArtOrSci(String input)
    {
        logger.info("isBaArtOrSci: Starting");
        boolean[] output =  {false,false};
        if(input.contains(AccProgram.BA_ART))
        {
            output = new boolean[] {true,false};
        }
        else if(input.contains(AccProgram.BA_SCI))
        {
            output = new boolean[] {false,true};
        }
        logger.info("isBaArtOrSci: Returning " + output[0] + "," + output[1]);
        return output;
    }

    /**
     * Finds what program level is contained within String.
     * @param input The string to find program level.
     * @return Found program level or null if not found.
     */
    private String findProgramLevelInString(String input)
    {
        logger.info("findProgramLevelInString: Starting");
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
        logger.info("removeIndentFromString: Starting");
        // Convert input string to chars
        char[] charaters = input.toCharArray();
        String output = null;
        output = input.trim();
        // for(int i = 0; i < charaters.length; i++)
        // {
        //     // If char is not ' '
        //     if(charaters[i] != ' ')
        //     {
        //         // Sets output to input substring begining index = iterator - 1
        //         output = input.substring((i--));
        //         // Break from loop
        //         logger.info("removeIndentFromString: end of indent found");
        //         break;
        //     }
        // }
        // If output was not filled
        if(output == null)
        {
            logger.info("removeIndentFromString: Returning " + input);
            return input;
        }
        logger.info("removeIndentFromString: Returning " + output);
        return output;
    }

    /**
     * Retrieves the credit range from the degree requirements entrie
     * @param input degree requirements entrie
     * @return int[2] {min, max}
     */
    private int[] getLineCreditRange(String input)
    {
        logger.info("getLineCreditRange: Starting Input: " + input);
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
        logger.info("getLineCreditRange: Returning");
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
        logger.info("getIntInString: Runs");
        return Integer.parseInt(input.replaceAll("\\D+", ""));
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
}
