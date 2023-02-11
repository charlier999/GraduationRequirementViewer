package com.gradview.data.dao;


import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.InvalidResultSetAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.gradview.data.dam.AccProgramElectivesCreditsDAM;
import com.gradview.exception.NoRowsFoundException;

public class AccProgramElectivesCreditsDAO 
{
    private static final Logger logger = LoggerFactory.getLogger(AccProgramElectivesCreditsDAO.class);
    private static final String TABLENAME = "acc-program-electives-credits";
	public static final String COL_PROGRAMID = "programID";
    public static final String COL_MINIMUM = "minimum";
    public static final String COL_MAXIMUM = "maximum";
    @Autowired
	private DataSource dataSource;
    
    @Autowired
	private JdbcTemplate jdbcTemplate;
    /** 
     * Retrieves a list of all {@link AccProgramElectivesCreditsDAM programs elective credits} from {@value #TABLENAME}
     * @return List< {@link AccProgramElectivesCreditsDAM} >
     * @throws NoRowsFoundException No rows were found in the table.
     * @throws DataAccessException An access exception occured.
     * @throws Exception Catch all for any exception.
     */
    public List< AccProgramElectivesCreditsDAM > getAll() throws NoRowsFoundException, DataAccessException, Exception
    {
        logger.info( "getAll: Started." );
		// Create SQL query.
		String sqlQuery = "SELECT * FROM " + TABLENAME;
		// Create results list.
		List< AccProgramElectivesCreditsDAM > output = new ArrayList<>();
		// Exception catch.
		try
		{
			// Run SQL Query.
			logger.info( "getAll: SQL querry started running." );
			SqlRowSet srs = jdbcTemplate.queryForRowSet( sqlQuery );
			// Create rowsFound check value.
			boolean rowsFound = false;
			// Loop through all resulting rows.
			logger.info( "getAll: Looping through the resulting row set." );
			while ( srs.next() )
			{
				rowsFound = true;
				// Add rows to output list.
				output.add( new AccProgramElectivesCreditsDAM(srs.getInt(COL_PROGRAMID), srs.getInt(COL_MINIMUM), srs.getInt(COL_MAXIMUM)));
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
		logger.info( "getAll: Returns " + output.size() + " resulting rows." );
		// Return batteries list.
		return output;
    }

    /** 
     * Searches for {@link AccProgramElectivesCreditsDAM programs elective credits} in the requested column for the query in the {@value #TABLENAME} table.
     * @param colName The column to search for the query in.
     * @param query The value to search in the column for.
     * @return List< {@link AccProgramElectivesCreditsDAM} >
     * @throws NoRowsFoundException No rows were found in the table.
     * @throws DataAccessException An access exception occured.
     * @throws Exception Catch all for any exception.
     */
    public List< AccProgramElectivesCreditsDAM > search(String colName, String query) throws NoRowsFoundException, DataAccessException, Exception
    {
        logger.info( "search: Started." );
        logger.info( "search: Column:" + colName + ", Query: " + query);
		// Create SQL query.
		String sqlQuery = "SELECT * FROM " + TABLENAME + " WHERE " + colName + " LIKE '" + query + "'";
		// Create results list.
		List< AccProgramElectivesCreditsDAM > output = new ArrayList<>();
		// Exception catch.
		try
		{
			// Run SQL Query.
			logger.info( "search: SQL querry started running." );
			SqlRowSet srs = jdbcTemplate.queryForRowSet( sqlQuery );
			// Create rowsFound check value.
			boolean rowsFound = false;
			// Loop through all resulting rows.
			logger.info( "search: Looping through the resulting row set." );
			while ( srs.next() )
			{
				rowsFound = true;
				// Add rows to output list.
				output.add( new AccProgramElectivesCreditsDAM(srs.getInt(COL_PROGRAMID), srs.getInt(COL_MINIMUM), srs.getInt(COL_MAXIMUM)));
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
		logger.info( "search: Returns " + output.size() + " resulting rows." );
		// Return batteries list.
		return output;
    }

    /** 
     * Inserts the {@link AccProgramElectivesCreditsDAM program elective credits} into the {@value #TABLENAME} table.
     * @param input The {@link AccProgramElectivesCreditsDAM program elective credits} to insert into the {@value #TABLENAME} table.
     * @return boolean Status of creation. True: {@link AccProgramElectivesCreditsDAM program elective credits} was added to the {@value #TABLENAME} table. 
     *          False: {@link AccProgramElectivesCreditsDAM program elective credits} was NOT added to the {@value #TABLENAME} table.
     * @throws DataAccessException An access exception occured.
     * @throws Exception Catch all for any exception.
     */
    public boolean create( AccProgramElectivesCreditsDAM input) throws DataAccessException, Exception
    {
        logger.info( "create: Started." );
		// Create SQL query.
		String sqlQuery = "INSERT INTO " + TABLENAME + "(" + COL_PROGRAMID + ", " + COL_MINIMUM + ", " + COL_MAXIMUM + ") VALUES(?,?,?)";
		// Exception catch.
		try
		{
			// Run SQL Query.
			logger.info( "create: SQL querry started running." );
			int rows = jdbcTemplate.update(sqlQuery, input.getProgramID(), input.getMinimum(), input.getMaximum());
            if ( rows == 1 )
			{
				logger.info( "create: Insert Success" );
				logger.info( "create: Returns true." );
				return true;
			}
			else
			{
				logger.error( "create: Insert Failed" );
				logger.info( "create: Returns false." );
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
        logger.info("create: Returns false.");
        return false;
    }

    /** 
     * Updates the {@link AccProgramElectivesCreditsDAM program elective credits} into the {@value #TABLENAME} table using the {@link AccProgramElectivesCreditsDAM}'s ProgramID 
     *  to select the database entry to update. 
     * @param input The {@link AccProgramElectivesCreditsDAM program elective credits} to update into the {@value #TABLENAME} table.
     * @return boolean Status of update. True: {@link AccProgramElectivesCreditsDAM program elective credits} was updated in the {@value #TABLENAME} table. 
     *          False: {@link AccProgramElectivesCreditsDAM program elective credits} was NOT updated in the {@value #TABLENAME} table.
     * @throws DataAccessException An access exception occured.
     * @throws Exception Catch all for any exception.
     */
    public boolean update( AccProgramElectivesCreditsDAM input) throws DataAccessException, Exception
    {
        logger.info( "update: Started." );
		// Create SQL query.
		String sqlQuery = "UPDATE " + TABLENAME + " SET " + COL_MINIMUM + " = ?, " + COL_MAXIMUM + " = ?, "  + "WHERE " + COL_PROGRAMID + " = ?";
		// Exception catch.
		try
		{
			// Run SQL Query.
			logger.info( "update: SQL querry started running." );
			int rows = jdbcTemplate.update(sqlQuery, input.getMinimum(), input.getMaximum(), input.getProgramID());
            if ( rows == 1 )
			{
				logger.info( "update: Update Success" );
				logger.info( "update: Returns true." );
				return true;
			}
			else
			{
				logger.error( "update: Update Failed" );
				logger.info( "update: Returns false." );
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
        logger.info("update: Returns false.");
        return false;
    }

    /**
     * Delets the inputed {@link AccProgramElectivesCreditsDAM program elective credits} from the {@value #TABLENAME} table using both of {@link AccProgramElectivesCreditsDAM}'s ProgramID
     *  to select the database entry to delete. 
     * @param input The {@link AccProgramElectivesCreditsDAM} to delete into the {@value #TABLENAME} table.
     * @return boolean Status of deletion. True: {@link AccProgramElectivesCreditsDAM program elective credits} was deleted from the {@value #TABLENAME} table. 
     *          False: {@link AccProgramElectivesCreditsDAM program elective credits} was NOT deleted from the {@value #TABLENAME} table.
     * @throws DataAccessException An access exception occured.
     * @throws Exception Catch all for any exception.
     */
    public boolean delete( AccProgramElectivesCreditsDAM input ) throws DataAccessException
	{
		logger.info( "delete: Started." );
		// Create sql statement.
		String sqlQ = "DELETE FROM " + TABLENAME + "WHERE " + COL_PROGRAMID + " = ?";
		try
		{
			// delete
			int rows = this.jdbcTemplate.update( sqlQ, input.getProgramID() );
			if ( rows == 1 )
			{
				logger.info( "delete: Delete Successful." );
				logger.info( "delete: Returns true." );
				return true;
			}
			else
			{
				logger.error( "delete: Delete Failed." );
				logger.info( "delete: Returns false." );
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
	    logger.info( "delete: Returns false." );
		return false;
	}
}
