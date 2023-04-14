package com.gradview.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
    
    private ComponentLogger compLogger = new ComponentLogger("gradview.service.CourseImportService");
   
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

    public List<LogMessage> importPrerequisites(String filename) throws FileNotFoundException
    {
        this.compLogger.clear(); // Clear comp logs
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
        return this.compLogger.getLogs();
    }

    public List<LogMessage> importClasses(String fileName) throws FileNotFoundException
    {
        this.compLogger.clear(); // Clear comp logs
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
        return this.compLogger.getLogs();
    }

    /**
     * Retireves the lines from the file.
     * @return List< String > of all lines from the file.
     * @throws FileNotFoundException
     */
    private List<String> retrieveLinesFromFile(String fileName) throws FileNotFoundException
    {
        this.compLogger.info("retrieveLinesFromFile", "Starting");
         
        Scanner sc = new Scanner(new FileInputStream(ResourceUtils.getFile("classpath:static/RawCourseDescriptions/" + fileName)));
        List<String> output = new ArrayList<>();
        this.compLogger.info("retrieveLinesFromFile","Iterating Through File");
        while(sc.hasNext())
        {
            output.add(sc.nextLine());
        }
        sc.close();
        this.compLogger.info("retrieveLinesFromFile","Iteration complete.");
        this.compLogger.info("retrieveLinesFromFile","Returning List of lines.");
        return output;
    }

    /**
     * Iterates through the inputed lines.
     * @param input The lines to iterate through.
     * @return List of Lists of clumps of strings.
     */
    private List<List<String>> clumpLines(List<String> input)
    {
        this.compLogger.info("clumpLines","Starting");
        List<List< String > > output = new ArrayList<List<String>>();
        ArrayList<String> clump = new ArrayList<>();
        int subCount = 0;
        this.compLogger.info("clumpLines","Iterating through input List");
        for(int i = 0; i < input.size(); i++)
        {
            // Split input iteration to char array.
            char[] chars = input.get(i).toCharArray();
            // If the 6th char is equal to ':''
            if(chars.length > 4)
            {
                if(subCount == 0) // This finds the 
                {
                    //this.compLogger.info("clumpLines","Count " + i);
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
                        String line = "";
                        for(int j = 0; j < chars.length; j++)
                        {
                            line += chars[j];
                        }
                        this.compLogger.warn("clumpLines", "Count " + i + " Has an misformated course number: `" + line + "`");
                        // Attempt cleanup
                        line = line.substring(0, 7) + ":";
                        this.compLogger.warn("clumpLines", "Count " + i + " Course Number cleanup Attempt: `" + line + "`");
                        clump.add(line);
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
        this.compLogger.info("clumpLines","Returning output List<List<String>>");
        return output;
    }    

    /**
     * Takes a list of a list of strings containg Course information and converts it to a list of {@link AccClass}es
     * @param input The list of a list of strings containg Course information.
     * @return List of {@link AccClass}es
     */
    public List<AccClass> clumpListToClassWithoutPrereq(List<List<String>> input)
    {
        this.compLogger.info("clumpListToClassWithoutPrereq","Starting");
        List<AccClass> output = new ArrayList<>();
        // Iterate through input list
        this.compLogger.info("clumpListToClassWithoutPrereq","Iterating through input list");
        for(int i = 0; i < input.size(); i++)
        {
            // Add Class to output list.
            output.add(clumpToClassWithoutPrereq(input.get(i)));
        }
        this.compLogger.info("clumpListToClassWithoutPrereq","Iterating finished.");
        this.compLogger.info("clumpListToClassWithoutPrereq","Returning Course List");
        return output;
    }

    /**
     * Takes a List of strings containing Course Information to be converted to a {@link AccClass}
     * @param input The list of strings containing Course Information
     * @return {@link AccClass} of inputed strings
     */
    private AccClass clumpToClassWithoutPrereq(List<String> input)
    {
        try
        {
            if(input.size() == 0 || input.size() == 1 || input.size() == 2)
            {
                logger.error("clumpToClassWithoutPrereq: " + input.toString());
                this.compLogger.warn("clumpToClassWithoutPrereq", "Class:`"+ input.get(0) +"` is missing rows.");
            }
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
            else if(input.get(2).length() == 11)
            {
                // Retrieve the first two charaters
                //String subCredit = input.get(2).substring(0, 2);
                // Convert first two charaters to int
                //int credit = Integer.parseInt(subCredit);
                // Set credits in class
                //TODO: Reformat credit value to double.
                output.setCredits(-5);  
            }
            else
            {
                this.compLogger.warn("clumpToClassWithoutPrereq", "Class:`"+ output.getNumber() +"`Credits misformated: " + input.get(2));
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
        catch(IndexOutOfBoundsException ex)
        {
            logger.error("clumpToClassWithoutPrereq: " + input.toString());
            this.compLogger.warn("clumpToClassWithoutPrereq", "Class:`"+ input.get(0) +"` is missing rows.");
            ex.printStackTrace();
            return new AccClass();
        }
    }

    /**
     * Iterates through a list of {@link AccClass}es and converts them to {@link AccClassDAM}es.
     * @param input list of {@link AccClass}es to be converted to {@link AccClassDAM}es.
     * @return list of {@link AccClassDAM}es.
     */
    public List<AccClassDAM> classModelListToDAMList(List<AccClass> input)
    {
        this.compLogger.info("classModelToDAM","Starting");
        // Create output list.
        List<AccClassDAM> output = new ArrayList<>();
        // Iterate through input list
        logger.info("classModelToDAM: Iterating through input list");
        for(int i = 0; i < input.size(); i++)
        {
            output.add(input.get(i).toDAM());
        }
        this.compLogger.info("classModelToDAM","Iteration Complete");
        this.compLogger.info("classModelToDAM","Returning AccClassDAM");
        return output;
    }

    /**
     * Inserts all classes in input list into the database.
     * @param input The list of {@link AccClassDAM}s to be instered into the database.
     */
    public void insertClassesToDB(List<AccClassDAM> input)
    {
        this.compLogger.info("insertClassesToDB","Starting");
        // Iterate through classes
        this.compLogger.info("insertClassesToDB","Iterating through input list");
        for(int i = 0; i < input.size(); i++)
        {
            try
            {
                accClassDAO.create(input.get(i));
            }
            catch ( DataAccessException e )
            {
                this.compLogger.error("insertClassesToDB","Data Access Exception Occured. Printing Stack Trace");
                e.printStackTrace();
            }
            catch ( Exception e )
            {
                this.compLogger.error("insertClassesToDB","Exception Occured. Printing Stack Trace");
                e.printStackTrace();
            }
        }
        this.compLogger.info("insertClassesToDB","All classes inserted");
        this.compLogger.info("insertClassesToDB","Finished");
    }

    /**
     * Imports prerequistes for classe
     * @param input
     * @throws Exception
     * @throws DataAccessException
     */
    public void importAllPrerequisitesFromClumps(List<List<String>> input) throws DataAccessException, Exception
    {
        this.compLogger.info("importPrerequisites","Starting with " + input.size() + " entries");
        // Iterate through input list
        this.compLogger.info("importPrerequisites","Iterating through input list");
        for(int i = 0; i < input.size(); i++)
        {
            this.importPrerequisites(input.get(i));
        }
        this.compLogger.info("importPrerequisites","Iterating finished.");
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
            // Check for class existing
            if(this.doesClassExist(classNumber))
            {
                if(input.get(4).contains("Prerequisite:") || input.get(4).contains("Prerequisites:"))
                {
                    List<String> classNumbers = new ArrayList<>();
                    Boolean isAndPreReq = false;
                    Boolean isOrPreReq = false;
                    String tempClassNumber = "";
                    char[] devChars = input.get(4).toCharArray();
                    // Find : at end of Prerequisite:/Prerequisites:
                    int colonInd = input.get(4).indexOf(":");
                    for(int i = colonInd; i < input.get(4).length(); i++)
                    {
                        // If the first character after Prerequisite:/Prerequisites: is ' ' and class numbers list is empty
                        if(input.get(4).charAt(i) == ' ' && tempClassNumber == "")
                        {
                            // do nothing 
                        }
                        // If comma
                        else if(input.get(4).charAt(i) == ',' && tempClassNumber != "")
                        {
                            // Check for course number with regex
                            Matcher matcher = Pattern.compile("[a-zA-Z]{3}-\\d{3}(:|[a-zA-Z]{1,2}:|)").matcher(tempClassNumber);
                            if(matcher.find()) // find course number and add to classNumber list
                            {
                                classNumbers.add(tempClassNumber);
                                tempClassNumber = "";
                            }
                        }
                        // if current char is . or ;
                        else if(input.get(4).charAt(i) == 'o' || input.get(4).charAt(i) == 'r')
                        {
                            tempClassNumber += input.get(4).charAt(i);
                            if(tempClassNumber.equals("or"))
                            {
                                isOrPreReq = true;
                                tempClassNumber = "";
                            }
                        }
                        // If characters a, n or d 
                        else if(
                            input.get(4).charAt(i) == 'a' 
                            || input.get(4).charAt(i) == 'n'
                            || input.get(4).charAt(i) == 'd'
                            )
                        {
                            tempClassNumber += input.get(4).charAt(i);
                            if(tempClassNumber.equals("and"))
                            {
                                isOrPreReq = true;
                                tempClassNumber = "";
                            }
                        }
                        else if(
                            (
                                input.get(4).charAt(i) == '.' 
                                || input.get(4).charAt(i) == ';'
                                || input.get(4).charAt(i) == ' '
                            ) 
                            && tempClassNumber != ""
                            )
                        {
                                // Check for course number with regex
                                Matcher matcher = Pattern.compile("[a-zA-Z]{3}-\\d{3}(:|[a-zA-Z]{1,2}:|)").matcher(tempClassNumber);
                                if(matcher.find()) // find course number and add to classNumber list
                                {
                                    classNumbers.add(tempClassNumber);
                                    tempClassNumber = "";
                                }
                                // If there is only one course number in the lest
                                if(classNumbers.size() == 1) isAndPreReq = true;
                                
                                // if prerequsites are not the same value
                                if(isAndPreReq != isOrPreReq)
                                {
                                    if(isAndPreReq) 
                                    {
                                        this.insertAndPrerequisets(classNumbers, classNumber);
                                        isAndPreReq = false;
                                        classNumbers = new ArrayList<>();
                                    }
                                    else if(isOrPreReq) 
                                    {
                                        this.insertOrPrerequisets(classNumbers, classNumber);
                                        isOrPreReq = false;
                                        classNumbers = new ArrayList<>();
                                    }
                                    else
                                    {
                                        logger.error("importPrerequisites: Prerequisite list and/or not defined.");
                                    }
                                }
                                else
                                {
                                    logger.error("importPrerequisites: Prerequisite list and/or is the same values.");
                                }
                            }
                        else if(input.get(4).charAt(i) == ' ' && tempClassNumber.length() > 3)
                        {
                            // Check for course number with regex
                            Matcher matcher = Pattern.compile("[a-zA-Z]{3}-\\d{3}(:|[a-zA-Z]{1,2}:|)").matcher(tempClassNumber);
                            if(matcher.find()) // find course number and add to classNumber list
                            {
                                classNumbers.add(tempClassNumber);
                                tempClassNumber = "";
                            }
                        }
                        // If characters o or r 
                        else if(input.get(4).charAt(i) != ':')
                        {
                            tempClassNumber += input.get(4).charAt(i);
                        }
                    }
                }
            }
            else
            {
                this.compLogger.warn("importPrerequisites", "Root Course:`" + classNumber + "` does not exist.");
            }
        }
    }

    /**
     * Checks to see if the class exists
     * @param classNumber
     * @return
     */
    public boolean doesClassExist(String classNumber)
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
            if(prerequisteCheck.equals("Prerequisite:") 
                || prerequisteCheck.equals("Prerequisites: "))
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
        if(!this.accClassPrerequisiteDAO.create(new AccClassPrerequisiteDAM(tempRootClass.getId()))) 
        {
            this.compLogger.warn("insertAndPrerequisets", "Prerequsite not created");
            return false;// If insert key not found.
        }
        // Get prerequisiteID
        List<AccClassPrerequisiteDAM> classList = this.accClassPrerequisiteDAO.search(AccClassPrerequisiteDAO.COL_CLASSID, "" + tempRootClass.getId());
        int prerequsitID = classList.get(classList.size()-1).getId();
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
            else
            {
                this.compLogger.warn("insertAndPrerequisets", "Root Course's:`" + rootClass + "` Course `" + prereqs.get(i) + "` does not exist.");
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
        if(!this.accClassPrerequisiteDAO.create(new AccClassPrerequisiteDAM(tempRootClass.getId()))) 
        {
            this.compLogger.warn("insertOrPrerequisets", "Prerequsite not created");
            return false;// If insert key not found.
        }
        // Get prerequisiteID
        List<AccClassPrerequisiteDAM> classList = this.accClassPrerequisiteDAO.search(AccClassPrerequisiteDAO.COL_CLASSID, "" + tempRootClass.getId());
        int prerequsitID = classList.get(classList.size()-1).getId();
    
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
            else
            {
                this.compLogger.warn("insertOrPrerequisets", "Root Course's:`" + rootClass + "` Course `" + prereqs.get(i) + "` does not exist.");
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
