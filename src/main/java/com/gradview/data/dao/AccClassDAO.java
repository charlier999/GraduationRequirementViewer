package com.gradview.data.dao;


import java.util.ArrayList;
import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.InvalidResultSetAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.gradview.data.dam.AccClassDAM;
import com.gradview.exception.NoRowsFoundException;

/**
 * Accesses and interacts with {@value #TABLENAME} table.
 */
@Repository
public class AccClassDAO 
{
    private static final Logger logger = LoggerFactory.getLogger(AccClassDAO.class);
	private static final String TABLENAME = "`acc-class`";
	public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_DESCRIPTION = "description";
    public static final String COL_NUMBER = "number";
    public static final String COL_CREDITS = "credits";

    @Autowired
	private JdbcTemplate jdbcTemplate;


    
    /** 
     * Retrieves a list of all classes from {@value #TABLENAME}
     * @return List< {@link AccClassDAM} >
     * @throws NoRowsFoundException No rows were found in the table.
     * @throws DataAccessException An access exception occured.
     * @throws Exception Catch all for any exception.
     */
    public List< AccClassDAM > getAll() throws NoRowsFoundException, DataAccessException, Exception
    {
        logger.debug( "getAll: Started." );
		// Create SQL query.
		String sqlQuery = "SELECT * FROM " + TABLENAME;
		// Create results list.
		List< AccClassDAM > output = new ArrayList<>();
		// Exception catch.
		try
		{
			// Run SQL Query.
			logger.debug( "getAll: SQL querry started running." );
			SqlRowSet srs = jdbcTemplate.queryForRowSet( sqlQuery );
			// Create rowsFound check value.
			boolean rowsFound = false;
			// Loop through all resulting rows.
			logger.debug( "getAll: Looping through the resulting row set." );
			while ( srs.next() )
			{
				rowsFound = true;
				// Add rows to output list.
				output.add( new AccClassDAM(srs.getInt(COL_ID), srs.getString(COL_NAME), 
                                            srs.getString(COL_DESCRIPTION), srs.getString(COL_NUMBER), 
                                            srs.getInt(COL_CREDITS)));
			}
			if ( !rowsFound )
			{
				logger.warn( "getAll: No rows were found." );
				throw new NoRowsFoundException( "No rows found in " + TABLENAME + " table." );
			}
		}
		catch ( NoRowsFoundException e )
		{
			logger.warn( "getAll: NoRowsFoundException occured." );
			throw e;
		}
		catch ( InvalidResultSetAccessException e )
		{
			logger.error( "getAll: InvalidResultSetAccessException occured." );
			e.printStackTrace();
		}
		catch ( DataAccessException e )
		{
			logger.error( "getAll: DataAccessException occured." );
			throw e;
		}
		catch ( Exception ex )
		{
			logger.error( "getAll: Exception occured." );
			// Print a Stack Trace if an exception occurs.
			ex.printStackTrace();
		}
		logger.debug( "getAll: Returns " + output.size() + " resulting rows." );
		// Return batteries list.
		return output;
    }

    
    /** 
     * Searches for {@link AccClassDAM classes} in the requested column for the query in the {@value #TABLENAME} table.
     * @param colName The column to search for the query in.
     * @param query The value to search in the column for.
     * @return List< {@link AccClassDAM} >
     * @throws NoRowsFoundException No rows were found in the table.
     * @throws DataAccessException An access exception occured.
     * @throws Exception Catch all for any exception.
     */
    public List< AccClassDAM > search(String colName, String query) throws NoRowsFoundException, DataAccessException, Exception
    {
        logger.debug( "search: Started." );
        logger.debug( "search: Column:" + colName + ", Query: " + query);
		// Create SQL query.
		String sqlQuery = "SELECT * FROM " + TABLENAME + " WHERE " + colName + " LIKE '" + query + "'";
		// Create results list.
		List< AccClassDAM > output = new ArrayList<>();
		// Exception catch.
		try
		{
			// Run SQL Query.
			logger.debug( "search: SQL querry started running." );
			SqlRowSet srs = jdbcTemplate.queryForRowSet( sqlQuery );
			// Create rowsFound check value.
			boolean rowsFound = false;
			// Loop through all resulting rows.
			logger.debug( "search: Looping through the resulting row set." );
			while ( srs.next() )
			{
				rowsFound = true;
				// Add rows to output list.
				output.add( new AccClassDAM(srs.getInt(COL_ID), srs.getString(COL_NAME), 
                                            srs.getString(COL_DESCRIPTION), srs.getString(COL_NUMBER), 
                                            srs.getInt(COL_CREDITS)));
			}
			if ( !rowsFound )
			{
				logger.warn( "search: No rows were found." );
				throw new NoRowsFoundException( "No rows found in " + TABLENAME + " table." );
			}
		}
		catch ( NoRowsFoundException e )
		{
			logger.warn( "search: NoRowsFoundException occured." );
			throw e;
		}
		catch ( InvalidResultSetAccessException e )
		{
			logger.error( "search: InvalidResultSetAccessException occured." );
			e.printStackTrace();
		}
		catch ( DataAccessException e )
		{
			logger.error( "search: DataAccessException occured." );
			throw e;
		}
		catch ( Exception ex )
		{
			logger.error( "search: Exception occured." );
			// Print a Stack Trace if an exception occurs.
			throw ex;
		}
		logger.debug( "search: Returns " + output.size() + " resulting rows." );
		// Return batteries list.
		return output;
    }

    
    /** 
     * Inserts the {@link AccClassDAM class} into the {@value #TABLENAME} table.
     * @param accClass The {@link AccClassDAM} to insert into the {@value #TABLENAME} table. Does NOT use the ID property.
     * @return boolean Status of creation. True: {@link AccClassDAM Class} was added to the {@value #TABLENAME} table. 
     *          False: {@link AccClassDAM Class} was NOT added to the {@value #TABLENAME} table.
     * @throws DataAccessException An access exception occured.
     * @throws Exception Catch all for any exception.
     */
    public boolean create( AccClassDAM accClass) throws DataAccessException, Exception
    {
        logger.debug( "create: Started." );
		// Create SQL query.
		String sqlQuery = "INSERT INTO " + TABLENAME + "(" + COL_NAME + ", " + COL_DESCRIPTION + ", " 
                                + COL_NUMBER + ", " + COL_CREDITS + ") VALUES(?,?,?,?)";
		// Exception catch.
		try
		{
			// Run SQL Query.
			logger.debug( "create: SQL querry started running." );
			int rows = jdbcTemplate.update(sqlQuery, accClass.getName(), accClass.getDescription(), 
                                            accClass.getNumber(), accClass.getCredits());
            if ( rows == 1 )
			{
				logger.debug( "create: Insert Success" );
				logger.debug( "create: Returns true." );
				return true;
			}
			else
			{
				logger.error( "create: Insert Failed" );
				logger.debug( "create: Returns false." );
				return false;
			}
		}
		catch ( DataAccessException e )
		{
			logger.error( "create: DataAccessException occured." );
			throw e;
		}
		catch ( Exception ex )
		{
			logger.error( "create: Exception occured." );
			// Print a Stack Trace if an exception occurs.
			ex.printStackTrace();
		}
        logger.error("create: Failed to Insert.");
        logger.debug("create: Returns false.");
        return false;
    }

    
    /** 
     * Updates the {@link AccClassDAM class} into the {@value #TABLENAME} table using the {@link AccClassDAM class'} ID 
     *  to select the database entry to update. 
     * @param accClass The {@link AccClassDAM} to update into the {@value #TABLENAME} table. Requires ID property.
     * @return boolean Status of update. True: {@link AccClassDAM Class} was updated in the {@value #TABLENAME} table. 
     *          False: {@link AccClassDAM Class} was NOT updated in the {@value #TABLENAME} table.
     * @throws DataAccessException An access exception occured.
     * @throws Exception Catch all for any exception.
     */
    public boolean update( AccClassDAM accClass) throws DataAccessException, Exception
    {
        logger.debug( "update: Started." );
		// Create SQL query.
		String sqlQuery = "UPDATE " + TABLENAME + " SET " + COL_NAME + " = ?, " + COL_DESCRIPTION + " = ?, "
                            + COL_NUMBER + " = ?, " + COL_CREDITS + " = ?, "  + "WHERE " + COL_ID + " = ?";
		// Exception catch.
		try
		{
			// Run SQL Query.
			logger.debug( "update: SQL querry started running." );
			int rows = jdbcTemplate.update(sqlQuery, accClass.getName(), accClass.getDescription(), 
                                            accClass.getNumber(), accClass.getCredits(), accClass.getId());
            if ( rows == 1 )
			{
				logger.debug( "update: Update Success" );
				logger.debug( "update: Returns true." );
				return true;
			}
			else
			{
				logger.error( "update: Update Failed" );
				logger.debug( "update: Returns false." );
				return false;
			}
		}
		catch ( DataAccessException e )
		{
			logger.error( "update: DataAccessException occured." );
			throw e;
		}
		catch ( Exception ex )
		{
			logger.error( "update: Exception occured." );
			// Print a Stack Trace if an exception occurs.
			ex.printStackTrace();
		}
        logger.error("update: Failed to Update.");
        logger.debug("update: Returns false.");
        return false;
    }

    /**
     * Delets the inputed {@link AccClassDAM class} from the {@value #TABLENAME} table using the {@link AccClassDAM class'} ID 
     *  to select the database entry to delete. 
     * @param input The {@link AccClassDAM} to delete into the {@value #TABLENAME} table. Requires ID property.
     * @return boolean Status of deletion. True: {@link AccClassDAM} was deleted from the {@value #TABLENAME} table. 
     *          False: {@link AccClassDAM } was NOT deleted from the {@value #TABLENAME} table.
     * @throws DataAccessException An access exception occured.
     * @throws Exception Catch all for any exception.
     */
    public boolean delete( AccClassDAM input ) throws DataAccessException
	{
		logger.debug( "delete: Started." );
		// Create sql statement.
		String sqlQ = "DELETE FROM " + TABLENAME + "WHERE " + COL_ID + " = ? ";
		try
		{
			// delete
			int rows = this.jdbcTemplate.update( sqlQ, input.getId() );
			if ( rows == 1 )
			{
				logger.debug( "delete: Delete Successful." );
				logger.debug( "delete: Returns true." );
				return true;
			}
			else
			{
				logger.error( "delete: Delete Failed." );
				logger.debug( "delete: Returns false." );
				return false;
			}
		}
		catch ( DataAccessException e )
		{
			logger.warn( "delete: DataAccessException occured." );
			throw e;
		}
		catch ( Exception ex )
		{
			logger.error( "delete: Exception occured." );
			// Print a Stack Trace if an exception occurs.
			ex.printStackTrace();
		}
		logger.error( "delete: Delete Failed." );
	    logger.debug( "delete: Returns false." );
		return false;
	}





}
