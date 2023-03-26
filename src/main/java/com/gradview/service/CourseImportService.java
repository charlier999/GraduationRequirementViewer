package com.gradview.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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

import com.gradview.data.dam.AccClassDAM;
import com.gradview.data.dam.AccClassPrerequisiteAndDAM;
import com.gradview.data.dam.AccClassPrerequisiteDAM;
import com.gradview.data.dam.AccClassPrerequisiteOrDAM;
import com.gradview.data.dao.AccClassDAO;
import com.gradview.data.dao.AccClassPrerequisiteAndDAO;
import com.gradview.data.dao.AccClassPrerequisiteDAO;
import com.gradview.data.dao.AccClassPrerequisiteOrDAO;
import com.gradview.exception.NoRowsFoundException;
import com.gradview.model.AccClass;

@Service
public class CourseImportService 
{
    private static final Logger logger = LoggerFactory.getLogger(CourseImportService.class);
    //private static final boolean RUNIMPORT = true;
   
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private AccClassDAO accClassDAO;

    @Autowired
    private AccClassPrerequisiteDAO accClassPrerequisiteDAO;

    @Autowired
    private AccClassPrerequisiteAndDAO accClassPrerequisiteAndDAO;

    @Autowired
    private AccClassPrerequisiteOrDAO accClassPrerequisiteOrDAO;

    // @Autowired
    // private AccGeneralEducationClassesDAO accGeneralEducationClassesDAO;

    // @Autowired
    // private AccGeneralEducationCompetencyDAO accGeneralEducationCompetencyDAO;

    public List<String> retrieveFormatedFiles() throws IOException
    {
        logger.info("retrieveFormatedFiles: Starting");
        logger.info("retrieveFormatedFiles: Retrieveing Resources");
        Resource[] resources = applicationContext.getResources("classpath:static/RawCourseDescriptions/*");
        List<String> output = new ArrayList<>();
        for(int i = 0; i < resources.length; i++)
        {
            output.add(resources[i].getFilename());
        }
        logger.info("retrieveFormatedFiles: Returning file names");
        return output;
    }

    public void importPrerequisites(String filename) throws FileNotFoundException
    {
        logger.info("importPrerequisites: Starting");

        logger.info("importPrerequisites: Retrieve lines from file");
        List<String> lines = this.retrieveLinesFromFile(filename);
        logger.info("importPrerequisites: Lines Retreived");

        logger.info("importPrerequisites: Clump Lines");
        List<List<String>> clumpedLines = this.clumpLines(lines);
        logger.info("importPrerequisites: Lines have been clumped");
        
        logger.info("importPrerequisites: Import Prerequistes Started");
        try
        {
            this.importAllPrerequisitesFromClumps(clumpedLines);
        }
        catch ( Exception e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        logger.info("importPrerequisites: Import Prerequistes Finished");
    }

    public void importClasses(String fileName) throws FileNotFoundException
    {
        logger.info("importClasses: Starting");

        logger.info("importClasses: Retrieve lines from file");
        List<String> lines = this.retrieveLinesFromFile(fileName);
        logger.info("importClasses: Lines Retreived");

        logger.info("importClasses: Clump Lines");
        List<List<String>> clumpedLines = this.clumpLines(lines);
        logger.info("importClasses: Lines have been clumped");

        logger.info("importClasses: Convert clumpted lines to classes");
        List<AccClass> classes = this.clumpListToClassWithoutPrereq(clumpedLines);
        logger.info("importClasses: Clumps have been converted to classes");

        logger.info("importClasses: Converting classes to classDAMs");
        List<AccClassDAM> classesDAM = this.classModelListToDAMList(classes);
        logger.info("importClasses: Classes have been converted to classDAMs");

        logger.info("importClasses: Inserting classDAMs into the database");
        this.insertClassesToDB(classesDAM);
        logger.info("importClasses: All classDAMs have been inserted");
        logger.info("importClasses: Finished");
    }

    /**
     * Retireves the lines from the file.
     * @return List< String > of all lines from the file.
     * @throws FileNotFoundException
     */
    private List<String> retrieveLinesFromFile(String fileName) throws FileNotFoundException
    {
        logger.info("retrieveLinesFromFile: Starting");
         
        Scanner sc = new Scanner(new FileInputStream(ResourceUtils.getFile("classpath:static/RawCourseDescriptions/" + fileName)));
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

    /**
     * Imports prerequistes for classe
     * @param input
     * @throws Exception
     * @throws DataAccessException
     */
    private void importAllPrerequisitesFromClumps(List<List<String>> input) throws DataAccessException, Exception
    {
        logger.info("importPrerequisites: Starting");
        // Iterate through input list
        logger.info("importPrerequisites: Iterating through input list");
        for(int i = 0; i < input.size(); i++)
        {
            this.importPrerequisites(input.get(i));
        }
        logger.info("importPrerequisites: Iterating finished.");
    }

    /**
     * Imports Prerequisites from list of strings
     * @param input
     * @throws Exception
     * @throws DataAccessException
     */
    private void importPrerequisites(List<String> input) throws DataAccessException, Exception
    {
        if(this.clumpContainsPrerequistes(input))
        {
            String classNumber = input.get(0); // Course Number
            List<String> classNumbers = new ArrayList<>();
            // Check for class existing
            if(this.doesClassExist(classNumber))
            {
                boolean nextClassOr = false;
                boolean nextClassAnd = false;
                int count = 0;
                // Loop until period
                while(input.get(4).charAt(count) != '.')
                {
                    // If white space
                    if(input.get(4).charAt(count) == ' ') count++;
                    // If the first character of a class number
                    if(Character.isUpperCase(input.get(4).charAt(count)))
                    {
                        // Pull class number from sting and add it to the list
                        classNumbers.add(input.get(4).substring(count, (count + 6)));
                        // Move count up by six
                        count = count + 6;
                        if(nextClassAnd) // if next class And flag true
                        {
                            this.insertAndPrerequisets(classNumbers, classNumber);
                            classNumbers.clear(); // Clear out list of class numbers
                            nextClassAnd = false;
                        }
                        if(nextClassOr) // if next class or flag true
                        {
                            this.insertOrPrerequisets(classNumbers, classNumber);
                            classNumbers.clear(); // Clear out list of class numbers
                            nextClassOr = false;
                        }
                    }
                    // If comma
                    if(input.get(4).charAt(count) == ',') count++;
                    // If ;
                    if(input.get(4).charAt(count) == ';')
                    {
                        nextClassAnd = false;
                        nextClassOr = false;
                    }
                    // If or
                    if(input.get(4).substring(count, (count + 1)).equals("or"))
                    {
                        // Iterate past or
                        count = count + 2;
                        nextClassOr = true;
                    }
                    // If and
                    if(input.get(4).substring(count, (count + 2)).equals("and"))
                    {
                        // Iterate past or
                        count = count + 3;
                        nextClassAnd = true;
                    }

                }
            }
        }
    }

    /**
     * Checks to see if the class exists
     * @param classNumber
     * @return
     */
    private boolean doesClassExist(String classNumber)
    {
        try
        {
            return this.accClassDAO.search(AccClassDAO.COL_NUMBER, classNumber).size() > 0;
        }
        catch ( DataAccessException e )
        {
            e.printStackTrace();
            return false;
        }
        catch ( NoRowsFoundException e )
        {
            return false;
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks to see if the clump has prerequistes
     * @param input
     * @return
     */
    private boolean clumpContainsPrerequistes(List<String> input)
    {
        if(input.size() == 5) // If input has prerequiste line
        {
            String prerequisteCheck = input.get(4).substring(0, 13);
            if(prerequisteCheck.equals("Prerequisites:") 
                || prerequisteCheck.equals("Prerequisite: "))
                return true; 
        }
        return false;
    }

    /**
     * Inserts prerequisites into the And table linked to the root class
     * @param prereqs List of prerequisits to link to root class
     * @param rootClass class to link prerequisites to
     * @return result
     * @throws DataAccessException
     * @throws Exception
     */
    private boolean insertAndPrerequisets(List<String> prereqs, String rootClass) throws DataAccessException, Exception
    {
        // Get class 
        AccClassDAM tempRootClass = this.getClassDAM(rootClass);
        int prerequsitID = this.accClassPrerequisiteDAO.create(new AccClassPrerequisiteDAM(tempRootClass.getId()));
        if(prerequsitID != -1) return false;// If insert key not found.
    
        // Loop through prereqs
        for(int i = 0; i < prereqs.size(); i++)
        {
            // Get class 
            AccClassDAM tempClass = this.getClassDAM(prereqs.get(i));
            if(tempClass != null) // If Not null
            {
                // Insert Prerequsite And DAM
                this.accClassPrerequisiteAndDAO.create(new AccClassPrerequisiteAndDAM(prerequsitID, tempClass.getId()));
            }
        }
        return true;  
    }

    /**
     * Inserts prerequisites into the or table linked to the root class
     * @param prereqs List of prerequisits to link to root class
     * @param rootClass class to link prerequisites to
     * @return result
     * @throws DataAccessException
     * @throws Exception
     */
    private boolean insertOrPrerequisets(List<String> prereqs, String rootClass)  throws DataAccessException, Exception
    {
        // Get class 
        AccClassDAM tempRootClass = this.getClassDAM(rootClass);
        int prerequsitID = this.accClassPrerequisiteDAO.create(new AccClassPrerequisiteDAM(tempRootClass.getId()));
        if(prerequsitID != -1) return false;// If insert key not found.
    
        // Loop through prereqs
        for(int i = 0; i < prereqs.size(); i++)
        {
            // Get class 
            AccClassDAM tempClass = this.getClassDAM(prereqs.get(i));
            if(tempClass != null) // If Not null
            {
                // Insert Prerequsite And DAM
                this.accClassPrerequisiteOrDAO.create(new AccClassPrerequisiteOrDAM(prerequsitID, tempClass.getId()));
            }
        }
        return true; 
    }

    /**
     * Gets the ClassDAM from the database
     * @param classNumber
     * @return
     */
    private AccClassDAM getClassDAM(String classNumber)
    {
        try
        {
            return this.accClassDAO.search(AccClassDAO.COL_NUMBER, classNumber).get(0);
        }
        catch ( DataAccessException e )
        {
            e.printStackTrace();
            return null;
        }
        catch ( NoRowsFoundException e )
        {
            return null;
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            return null;
        }
    }
}
