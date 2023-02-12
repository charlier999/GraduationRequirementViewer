package com.gradview.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.gradview.data.dam.AccClassDAM;
import com.gradview.data.dao.AccClassDAO;
import com.gradview.model.AccClass;

@Service
public class CourseImportService 
{
    private static final Logger logger = LoggerFactory.getLogger(CourseImportService.class);
    //private static final boolean RUNIMPORT = true;
    private InputStream courseSteamA;
    // AccClass DAOs

    @Autowired
    private AccClassDAO accClassDAO;

    // @Autowired
    // private AccClassPrerequisiteDAO accClassPrerequisiteDAO;

    // @Autowired
    // private AccGeneralEducationClassesDAO accGeneralEducationClassesDAO;

    // @Autowired
    // private AccGeneralEducationCompetencyDAO accGeneralEducationCompetencyDAO;

    public void importClassesWithoutRequisites() throws FileNotFoundException
    {
        logger.info("importClassesWithoutRequisites: Starting");
        logger.info("importClassesWithoutRequisites: Retrieve lines from file");
        List<String> lines = this.retrieveLinesFromFile();
        logger.info("importClassesWithoutRequisites: Lines Retreived");
        logger.info("importClassesWithoutRequisites: Clump Lines");
        List<List<String>> clumpedLines = this.clumpLines(lines);
        logger.info("importClassesWithoutRequisites: Lines have been clumped");
        logger.info("importClassesWithoutRequisites: Convert clumpted lines to classes");
        List<AccClass> classes = this.clumpListToClassWithoutPrereq(clumpedLines);
        logger.info("importClassesWithoutRequisites: Clumps have been converted to classes");
        logger.info("importClassesWithoutRequisites: Converting classes to classDAMs");
        List<AccClassDAM> classesDAM = this.classModelListToDAMList(classes);
        logger.info("importClassesWithoutRequisites: Classes have been converted to classDAMs");
        logger.info("importClassesWithoutRequisites: Inserting classDAMs into the database");
        this.insertClassesToDB(classesDAM);
        logger.info("importClassesWithoutRequisites: All classDAMs have been inserted");
        logger.info("importClassesWithoutRequisites: Finished");
        
    }

    /**
     * Retireves the lines from the file.
     * @return List< String > of all lines from the file.
     * @throws FileNotFoundException
     */
    private List<String> retrieveLinesFromFile() throws FileNotFoundException
    {
        logger.info("retrieveLinesFromFile: Starting");
        this.courseSteamA = new FileInputStream(ResourceUtils.getFile("classpath:static/RawCourseDescriptions/CourseDescriptions_C.txt"));
        Scanner sc = new Scanner(this.courseSteamA);
        List<String> output = new ArrayList<>();
        logger.info("retrieveLinesFromFile: Iterating Through File");
        while(sc.hasNext())
        {
            output.add(sc.nextLine());
        }
        sc.close();
        logger.info("retrieveLinesFromFile: Iteration complete.");
        logger.info("retrieveLinesFromFile: Returning List of lines.");
        return output;
    }

    /**
     * Iterates through the inputed lines.
     * @param input The lines to iterate through.
     * @return List of Lists of clumps of strings.
     */
    private List<List<String>> clumpLines(List<String> input)
    {
        logger.info("clumpLines: Starting");
        List<List< String > > output = new ArrayList<List<String>>();
        ArrayList<String> clump = new ArrayList<>();
        int subCount = 0;
        logger.info("clumpLines: Iterating through input List");
        for(int i = 0; i < input.size(); i++)
        {
            // Split input iteration to char array.
            char[] chars = input.get(i).toCharArray();
            // If the 6th char is equal to ':''
            if(chars.length > 4)
            {
                if(subCount == 0) // This finds the 
                {
                    logger.info("clumpLines: Count " + i);
                    if(chars[7] == ':')
                    {
                        // Pull Course Number from char array
                        String line = "";
                        for(int j = 0; j < 7; j++)
                        {
                            line += chars[j];
                        }
                        // Add course number to clump
                        clump.add(line);
                    }
                    else
                    {
                        logger.info("null");
                    }
                }
                // If the first 4 chars are ' '
                if(subCount > 0 && chars[0] == ' ' && chars[1] == ' ' && chars[2] == ' ' && chars[3] == ' ' )
                {
                    // name clump line
                    if(subCount == 1)
                    {
                        // Add course name to clump
                        clump.add(input.get(i).substring(4));                
                    }
                    // credits clump line
                    if(subCount == 2)
                    {
                        // Add course credits to clump
                        clump.add(input.get(i).substring(4));               
                    }
                    // description clump line
                    if(subCount == 3)
                    {
                        // Add course description to clump
                        clump.add(input.get(i).substring(4));                
                    }
                    // prerequsite clump line
                    if(subCount == 4)
                    {
                        // Add course prerequiste to clump
                        clump.add(input.get(i).substring(4));               
                    }
                }
            }
            // If input line is empty
            if(input.get(i) == "")
            {
                // Add clump to output list
                output.add(clump);
                // Clear clump
                clump = new ArrayList<>();
                // Set sub count to -1 to let the iterator reach start again when ++ed
                subCount = -1;
            }
            // Iterate subcount
            subCount++;
        }
        logger.info("clumpLines: Returning output List<List<String>>");
        return output;
    }    

    /**
     * Takes a list of a list of strings containg Course information and converts it to a list of {@link AccClass}es
     * @param input The list of a list of strings containg Course information.
     * @return List of {@link AccClass}es
     */
    private List<AccClass> clumpListToClassWithoutPrereq(List<List<String>> input)
    {
        logger.info("clumpListToClassWithoutPrereq: Starting");
        List<AccClass> output = new ArrayList<>();
        // Iterate through input list
        logger.info("clumpListToClassWithoutPrereq: Iterating through input list");
        for(int i = 0; i < input.size(); i++)
        {
            // Add Class to output list.
            output.add(clumpToClassWithoutPrereq(input.get(i)));
        }
        logger.info("clumpListToClassWithoutPrereq: Iterating finished.");
        logger.info("clumpListToClassWithoutPrereq: Returning Course List");
        return output;
    }

    /**
     * Takes a List of strings containing Course Information to be converted to a {@link AccClass}
     * @param input The list of strings containing Course Information
     * @return {@link AccClass} of inputed strings
     */
    private AccClass clumpToClassWithoutPrereq(List<String> input)
    {
        // Create output class
        AccClass output = new AccClass();
        // Set class properties
        output.setNumber(input.get(0)); // Course Number
        output.setName(input.get(1)); // Course Name
        // If credits are < 10
        if(input.get(2).length() == 9)
        {
            // Retrieve the first charater
            String subCredit = input.get(2).substring(0, 1);
            // Convert first charater to int
            int credit = Integer.parseInt(subCredit);
            // Set credits in class
            output.setCredits(credit);
        }
        // If credits are >= 10 
        else if(input.get(2).length() == 10)
        {
            // Retrieve the first two charaters
            String subCredit = input.get(2).substring(0, 2);
            // Convert first two charaters to int
            int credit = Integer.parseInt(subCredit);
            // Set credits in class
            output.setCredits(credit);  
        }
        // Set the description
        output.setDescription(input.get(3) );
        // Check clump size
        if(input.size() == 5) // If clump has prereqs
        {
            // Amend the description with prerequisites added to it.
            output.setDescription(output.getDescription() + " " + input.get(4) );
        }
        return output;
    }

    /**
     * Iterates through a list of {@link AccClass}es and converts them to {@link AccClassDAM}es.
     * @param input list of {@link AccClass}es to be converted to {@link AccClassDAM}es.
     * @return list of {@link AccClassDAM}es.
     */
    private List<AccClassDAM> classModelListToDAMList(List<AccClass> input)
    {
        logger.info("classModelToDAM: Starting");
        // Create output list.
        List<AccClassDAM> output = new ArrayList<>();
        // Iterate through input list
        logger.info("classModelToDAM: Iterating through input list");
        for(int i = 0; i < input.size(); i++)
        {
            output.add(input.get(i).toDAM());
        }
        logger.info("classModelToDAM: Iteration Complete");
        logger.info("classModelToDAM: Returning AccClassDAM");
        return output;
    }

    /**
     * Inserts all classes in input list into the database.
     * @param input The list of {@link AccClassDAM}s to be instered into the database.
     */
    private void insertClassesToDB(List<AccClassDAM> input)
    {
        logger.info("insertClassesToDB: Starting");
        // Iterate through classes
        logger.info("insertClassesToDB: Iterating through input list");
        for(int i = 0; i < input.size(); i++)
        {
            try
            {
                accClassDAO.create(input.get(i));
            }
            catch ( DataAccessException e )
            {
                logger.error("insertClassesToDB: Data Access Exception Occured. Printing Stack Trace");
                e.printStackTrace();
            }
            catch ( Exception e )
            {
                logger.error("insertClassesToDB: Exception Occured. Printing Stack Trace");
                e.printStackTrace();
            }
        }
        logger.info("insertClassesToDB: All classes inserted");
        logger.info("insertClassesToDB: Finished");
    }


}
