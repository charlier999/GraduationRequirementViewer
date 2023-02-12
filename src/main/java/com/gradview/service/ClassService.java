package com.gradview.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.gradview.data.dam.AccClassDAM;
import com.gradview.data.dao.AccClassDAO;
import com.gradview.exception.NoRowsFoundException;
import com.gradview.model.AccClass;

@Service
public class ClassService 
{
    private static final Logger logger = LoggerFactory.getLogger(ClassService.class);

    @Autowired
    private AccClassDAO accClassDAO;

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
        logger.info("getClassByNumber: Starting");
        List<AccClass> output = new ArrayList<>();
        
        try
        {
            logger.info("getClassByNumber: Searching for " + input);
            List<AccClassDAM> response = accClassDAO.search(AccClassDAO.COL_NUMBER, input);
            logger.info("getClassByNumber: Results found " + response.size());
            logger.info("getClassByNumber: Converting DAM to Class");
            for(int i = 0; i < response.size(); i++)
            {
                output.add(response.get(i).toAccClass());
            }
            logger.info("getClassByNumber: Conversion Complete");
            logger.info("getClassByNumber: Returning list");
            
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
}
