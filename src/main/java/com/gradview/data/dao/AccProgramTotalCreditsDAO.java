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
import org.springframework.stereotype.Component;

import com.gradview.data.dam.AccProgramTotalCreditsDAM;
import com.gradview.exception.NoRowsFoundException;

@Component
public class AccProgramTotalCreditsDAO 
{
    private static final Logger logger = LoggerFactory.getLogger(AccProgramTotalCreditsDAO.class);
    private static final String TABLENAME = "`acc-program-total-credits`";
	public static final String COL_PROGRAMID = "programID";
    public static final String COL_CREDITS = "credits";
    
    @Autowired
	private JdbcTemplate jdbcTemplate;
    /** 
     * Retrieves a list of all {@link AccProgramTotalCreditsDAM programs total credits} from {@value #TABLENAME}
     * @return List< {@link AccProgramTotalCreditsDAM} >
     * @throws NoRowsFoundException No rows were found in the table.
     * @throws DataAccessException An access exception occured.
     * @throws Exception Catch all for any exception.
     */
    public List< AccProgramTotalCreditsDAM > getAll() throws NoRowsFoundException, DataAccessException, Exception
    {
        logger.debug( "getAll: Started." );
		// Create SQL query.
		String sqlQuery = "SELECT * FROM " + TABLENAME;
		// Create results list.
		List< AccProgramTotalCreditsDAM > output = new ArrayList<>();
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
				output.add( new AccProgramTotalCreditsDAM(srs.getInt(COL_PROGRAMID), srs.getInt(COL_CREDITS)));
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
     * Searches for {@link AccProgramTotalCreditsDAM programs total credits} in the requested column for the query in the {@value #TABLENAME} table.
     * @param colName The column to search for the query in.
     * @param query The value to search in the column for.
     * @return List< {@link AccProgramTotalCreditsDAM} >
     * @throws NoRowsFoundException No rows were found in the table.
     * @throws DataAccessException An access exception occured.
     * @throws Exception Catch all for any exception.
     */
    public List< AccProgramTotalCreditsDAM > search(String colName, String query) throws NoRowsFoundException, DataAccessException, Exception
    {
        logger.debug( "search: Started." );
        logger.debug( "search: Column:" + colName + ", Query: " + query);
		// Create SQL query.
		String sqlQuery = "SELECT * FROM " + TABLENAME + " WHERE " + colName + " LIKE '" + query + "'";
		// Create results list.
		List< AccProgramTotalCreditsDAM > output = new ArrayList<>();
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
				output.add( new AccProgramTotalCreditsDAM(srs.getInt(COL_PROGRAMID), srs.getInt(COL_CREDITS)));
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
			ex.printStackTrace();
		}
		logger.debug( "search: Returns " + output.size() + " resulting rows." );
		// Return batteries list.
		return output;
    }

    /** 
     * Inserts the {@link AccProgramTotalCreditsDAM program total credits} into the {@value #TABLENAME} table.
     * @param input The {@link AccProgramTotalCreditsDAM program total credits} to insert into the {@value #TABLENAME} table.
     * @return boolean Status of creation. True: {@link AccProgramTotalCreditsDAM program total credits} was added to the {@value #TABLENAME} table. 
     *          False: {@link AccProgramTotalCreditsDAM program total credits} was NOT added to the {@value #TABLENAME} table.
     * @throws DataAccessException An access exception occured.
     * @throws Exception Catch all for any exception.
     */
    public boolean create( AccProgramTotalCreditsDAM input) throws DataAccessException, Exception
    {
        logger.debug( "create: Started." );
		// Create SQL query.
		String sqlQuery = "INSERT INTO " + TABLENAME + "(" + COL_PROGRAMID + ", " + COL_CREDITS + ") VALUES(?,?)";
		// Exception catch.
		try
		{
			// Run SQL Query.
			logger.debug( "create: SQL querry started running." );
			int rows = jdbcTemplate.update(sqlQuery, input.getProgramID(), input.getCredits());
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
     * Updates the {@link AccProgramTotalCreditsDAM program total credits} into the {@value #TABLENAME} table using the {@link AccProgramTotalCreditsDAM}'s ProgramID to select the database entry to update. 
     * @param input The {@link AccProgramTotalCreditsDAM program total credits} to update into the {@value #TABLENAME} table.
     * @return boolean Status of update. True: {@link AccProgramTotalCreditsDAM program total credits} was updated in the {@value #TABLENAME} table. 
     *          False: {@link AccProgramTotalCreditsDAM program total credits} was NOT updated in the {@value #TABLENAME} table.
     * @throws DataAccessException An access exception occured.
     * @throws Exception Catch all for any exception.
     */
    public boolean update( AccProgramTotalCreditsDAM input) throws DataAccessException, Exception
    {
        logger.debug( "update: Started." );
		// Create SQL query.
		String sqlQuery = "UPDATE " + TABLENAME + " SET " + COL_CREDITS + " = ?, " + "WHERE " + COL_PROGRAMID + " = ?";
		// Exception catch.
		try
		{
			// Run SQL Query.
			logger.debug( "update: SQL querry started running." );
			int rows = jdbcTemplate.update(sqlQuery, input.getCredits(), input.getProgramID());
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
     * Delets the inputed {@link AccProgramTotalCreditsDAM program total credits} from the {@value #TABLENAME} table using both of {@link AccProgramTotalCreditsDAM}'s ProgramID
     *  to select the database entry to delete. 
     * @param input The {@link AccProgramTotalCreditsDAM} to delete into the {@value #TABLENAME} table.
     * @return boolean Status of deletion. True: {@link AccProgramTotalCreditsDAM program total credits} was deleted from the {@value #TABLENAME} table. 
     *          False: {@link AccProgramTotalCreditsDAM program total credits} was NOT deleted from the {@value #TABLENAME} table.
     * @throws DataAccessException An access exception occured.
     * @throws Exception Catch all for any exception.
     */
    public boolean delete( AccProgramTotalCreditsDAM input ) throws DataAccessException
	{
		logger.debug( "delete: Started." );
		// Create sql statement.
		String sqlQ = "DELETE FROM " + TABLENAME + "WHERE " + COL_PROGRAMID + " = ?";
		try
		{
			// delete
			int rows = this.jdbcTemplate.update( sqlQ, input.getProgramID() );
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
