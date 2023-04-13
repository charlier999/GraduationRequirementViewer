package com.gradview.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

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
import com.gradview.model.AccClassPrerequisite;
import com.gradview.ui.ufo.UFOClassSearch;

@Service
public class ClassService 
{
    private static final Logger logger = LoggerFactory.getLogger(ClassService.class);

    @Autowired
    private AccClassDAO accClassDAO;
    @Autowired
    private AccClassPrerequisiteDAO accClassPrerequisiteDAO;
    @Autowired
    private AccClassPrerequisiteAndDAO accClassPrerequisiteAndDAO;
    @Autowired
    private AccClassPrerequisiteOrDAO accClassPrerequisiteOrDAO;

    /**
     * Retrieves all classes by thier number
     * @param input The string representing the class number
     * @return List< {@link AccClass} >
     * @throws NoRowsFoundException No rows were found in the table.
     * @throws DataAccessException An access exception occured.
     * @throws Exception Catch all for any exception.
     */
    public List<AccClass> getClassByNumber(String input) throws NoRowsFoundException, DataAccessException, Exception
    {
        logger.debug("getClassByNumber: Starting");
        List<AccClass> output = new ArrayList<>();
        
        try
        {
            logger.debug("getClassByNumber: Searching for " + input);
            List<AccClassDAM> response = accClassDAO.search(AccClassDAO.COL_NUMBER, input);
            logger.debug("getClassByNumber: Results found " + response.size());
            logger.debug("getClassByNumber: Converting DAM to Class");
            for(int classCount = 0; classCount < response.size(); classCount++)
            {
                output.add(response.get(classCount).toAccClass());
            } 
            // Get Prerequisites
            for(int classCount = 0; classCount < output.size(); classCount++)
            {
                // Set prerequisites by searching for prerequisites using class id
                output.get(classCount).setPrerequisites(this.getClassPrerequisitesByClassID(output.get(classCount).getId()));
            } 


            logger.debug("getClassByNumber: Conversion Complete");
            logger.debug("getClassByNumber: Returning list");
            
        }
        catch ( DataAccessException e )
        {
            logger.error("getClassByNumber: Data Access Exception occured: " + e);
            throw e;
        }
        catch ( NoRowsFoundException e )
        {
            logger.warn("getClassByNumber: No Rows Found Exception occured: " + e);
            throw e;
        }
        catch ( Exception e )
        {
            logger.error("getClassByNumber: Exception occured: " + e);
            throw e;
        }
        return output;
    }

    /**
     * Retrieves all basic classes by thier number
     * @param input The string representing the class number
     * @return List< {@link AccClass} >
     * @throws NoRowsFoundException No rows were found in the table.
     * @throws DataAccessException An access exception occured.
     * @throws Exception Catch all for any exception.
     */
    public List<AccClass> getBasicClassByNumber(String input) throws NoRowsFoundException, DataAccessException, Exception
    {
        logger.debug("getBasicClassByNumber: Starting");
        List<AccClass> output = new ArrayList<>();
        
        try
        {
            logger.debug("getBasicClassByNumber: Searching for " + input);
            List<AccClassDAM> response = accClassDAO.search(AccClassDAO.COL_NUMBER, input);
            logger.debug("getBasicClassByNumber: Results found " + response.size());
            logger.debug("getBasicClassByNumber: Converting DAM to Class");
            for(int classCount = 0; classCount < response.size(); classCount++)
            {
                output.add(response.get(classCount).toAccClass());
            } 
            logger.debug("getBasicClassByNumber: Conversion Complete");
            logger.debug("getBasicClassByNumber: Returning list");
            
        }
        catch ( DataAccessException e )
        {
            logger.error("getBasicClassByNumber: Data Access Exception occured: " + e);
            throw e;
        }
        catch ( NoRowsFoundException e )
        {
            logger.warn("getBasicClassByNumber: No Rows Found Exception occured: " + e);
            throw e;
        }
        catch ( Exception e )
        {
            logger.error("getBasicClassByNumber: Exception occured: " + e);
            throw e;
        }
        return output;
    }
    
    /**
     * Retrieves a list of basic class info from a list of classIDs
     * @param classIDs The list of classIDs to retrieve basic class info from.
     * @return List< {@link AccClass} >
     */
    public List<AccClass> getBasicClassesByClassIDs(List<Integer> classIDs)
    {
        List<AccClass> output = new ArrayList<>();
        for(int i = 0; i < classIDs.size(); i++)
        {
            try
            {
                logger.debug("getBasicClassesByClassIDs: Searching for " + classIDs.get(i));
                List<AccClassDAM> response = accClassDAO.search(AccClassDAO.COL_ID, Integer.toString(classIDs.get(i)));
                logger.debug("getBasicClassesByClassIDs: Results found " + response.size());
                logger.debug("getBasicClassesByClassIDs: Converting DAM to Class");
                for(int classCount = 0; classCount < response.size(); classCount++)
                {
                    output.add(response.get(classCount).toAccClass());
                } 
                logger.debug("getBasicClassesByClassIDs: Conversion Complete");
                logger.debug("getBasicClassesByClassIDs: Returning list");
                
            }
            catch ( DataAccessException e )
            {
                logger.error("getBasicClassesByClassIDs: Data Access Exception occured: " + e);
                throw e;
            }
            catch ( NoRowsFoundException e )
            {
                logger.warn("getBasicClassesByClassIDs: No Rows Found Exception occured: " + e);
            }
            catch ( Exception e )
            {
                logger.error("getBasicClassesByClassIDs: Exception occured: " + e);
            }
        }

        return output;
    }

    /**
     * Searches the table for query
     * @param search The search object containing the query
     * @return List< {@link AccClass} >
     * @throws NoRowsFoundException No rows were found in the table.
     * @throws DataAccessException An access exception occured.
     * @throws Exception Catch all for any exception.
     */
    public List<AccClass> search(UFOClassSearch search) throws DataAccessException, NoRowsFoundException, Exception
    {
        logger.debug("search: Starting");
        List<AccClass> output = new ArrayList<>();
        try
        {
            List<AccClassDAM> response = new ArrayList<>();
            if(search.getTableHeader().equals(AccClassDAO.COL_NUMBER))
            {
                response = accClassDAO.search(search.getTableHeader(), search.getQuerry());
            }
            else if(search.getTableHeader().equals(AccClassDAO.COL_NAME))
            {
                response = accClassDAO.search(search.getTableHeader(), "%" + search.getQuerry() + "%");
            }
            else if(search.getTableHeader().equals(AccClassDAO.COL_DESCRIPTION))
            {
                response = accClassDAO.search(search.getTableHeader(), "%" + search.getQuerry() + "%");
            }
            else
            {
                throw new Exception();
            }
            
            logger.debug("search: Results found " + response.size());
            logger.debug("search: Converting DAM to Class");
            for(int i = 0; i < response.size(); i++)
            {
                output.add(response.get(i).toAccClass());
            }
            logger.debug("search: Conversion Complete");
        }
        catch ( DataAccessException e )
        {
            logger.error("search: Data Access Exception occured: " + e);
            throw e;
        }
        catch ( NoRowsFoundException e )
        {
            logger.warn("search: No Rows Found Exception occured: " + e);
            throw e;
        }
        catch ( Exception e )
        {
            logger.error("search: Exception occured: " + e);
            throw e;
        }
        logger.debug("search: Returning list");
        return output;
    }


    private List<AccClassPrerequisite> getClassPrerequisitesByClassID(int classID) throws DataAccessException, Exception
    {
        List<AccClassPrerequisite> output = new ArrayList<>();
        try
        {
            // Get prerequsites
            List<AccClassPrerequisiteDAM> prerequisiteDAMs =  
                this.accClassPrerequisiteDAO.search(AccClassPrerequisiteDAO.COL_CLASSID, Integer.toString(classID));

            // Convert DAM to nonDAM prerequisite
            for(int preReqCount = 0; preReqCount < prerequisiteDAMs.size(); preReqCount++)
            {
                output.add(prerequisiteDAMs.get(preReqCount).toAccClassPrerequisite());
            }
            // Find Ands and Ors for prereques
            for(int preReqCount = 0; preReqCount < output.size(); preReqCount++)
            {
                try
                {
                    // Find And PreReqs
                    List<AccClassPrerequisiteAndDAM> prerequisiteAndDAMs = 
                        this.accClassPrerequisiteAndDAO.search(AccClassPrerequisiteAndDAO.COL_PREREQUISITEID, 
                            Integer.toString(output.get(preReqCount).id));
                    if(prerequisiteAndDAMs.size() > 0) output.get(preReqCount).andOr = true;
                    // Loop through found And PreReqs
                    for(int preReqAndCount = 0; preReqAndCount < prerequisiteAndDAMs.size(); preReqAndCount++)
                    {
                        // Add and prereq classes
                        output.get(preReqCount).classIDs.add(prerequisiteAndDAMs.get(preReqAndCount).getRequiredClassID());
                    }
                }
                catch ( NoRowsFoundException e )
                {
                    logger.debug("getClassPrerequisitesByClassID: Class has no AND prerequisites.");
                    try
                    {
                        // Find And PreReqs
                        List<AccClassPrerequisiteOrDAM> prerequisiteOrDAMs = 
                            this.accClassPrerequisiteOrDAO.search(AccClassPrerequisiteOrDAO.COL_PREREQUISITEID, 
                                Integer.toString(output.get(preReqCount).id));
                        if(prerequisiteOrDAMs.size() > 0) output.get(preReqCount).andOr = false;
                        // Loop through found And PreReqs
                        for(int preReqOrCount = 0; preReqOrCount < prerequisiteOrDAMs.size(); preReqOrCount++)
                        {
                            // Add and prereq classes
                            output.get(preReqCount).classIDs.add(prerequisiteOrDAMs.get(preReqOrCount).getRequiredClassID());
                        }
                    }
                    catch ( NoRowsFoundException ex )
                    {
                        logger.warn("getClassPrerequisitesByClassID: Class has no AND or OR prerequisites.");
                    }
                }
            } // End of And Ors for loop
        }
        catch ( NoRowsFoundException e )
        {
            logger.debug("getClassPrerequisitesByClassID: Class has no prerequisites.");
        }
        return output;
    }
}
