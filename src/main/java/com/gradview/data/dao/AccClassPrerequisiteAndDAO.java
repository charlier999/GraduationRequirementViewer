package com.gradview.data.dao;

import com.gradview.data.dam.AccClassPrerequisiteAndDAM;
import com.gradview.exception.NoRowsFoundException;

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

@Repository
public class AccClassPrerequisiteAndDAO
{
    private static final Logger logger = LoggerFactory.getLogger(AccClassPrerequisiteAndDAO.class);
	private static final String TABLENAME = "`acc-class-prerequisite-and`";
	public static final String COL_PREREQUISITEID = "prerequisiteID";
    public static final String COL_REQUIRED_CLASSID = "requiredClassID";

    @Autowired
	private JdbcTemplate jdbcTemplate;

    /** 
     * Retrieves a list of all classes prerequiste ands from {@value #TABLENAME}
     * @return List< {@link AccClassPrerequisiteAndDAM} >
     * @throws NoRowsFoundException No rows were found in the table.
     * @throws DataAccessException An access exception occured.
     * @throws Exception Catch all for any exception.
     */
    public List< AccClassPrerequisiteAndDAM > getAll() throws NoRowsFoundException, DataAccessException, Exception
    {
        logger.debug( "getAll: Started." );
		// Create SQL query.
		String sqlQuery = "SELECT * FROM " + TABLENAME;
		// Create results list.
		List< AccClassPrerequisiteAndDAM > output = new ArrayList<>();
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
				output.add( new AccClassPrerequisiteAndDAM(srs.getInt(COL_PREREQUISITEID), srs.getInt(COL_REQUIRED_CLASSID)));
			}
			if ( !rowsFound )
			{
				logger.debug( "getAll: No rows were found." );
				throw new NoRowsFoundException( "No rows found in " + TABLENAME + " table." );
			}
		}
		catch ( NoRowsFoundException e )
		{
			logger.debug( "getAll: NoRowsFoundException occured." );
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
     * Searches for {@link AccClassPrerequisiteAndDAM classes} in the requested column for the query in the {@value #TABLENAME} table.
     * @param colName The column to search for the query in.
     * @param query The value to search in the column for.
     * @return List< {@link AccClassPrerequisiteAndDAM} >
     * @throws NoRowsFoundException No rows were found in the table.
     * @throws DataAccessException An access exception occured.
     * @throws Exception Catch all for any exception.
     */
    public List< AccClassPrerequisiteAndDAM > search(String colName, String query) throws NoRowsFoundException, DataAccessException, Exception
    {
        logger.debug( "search: Started." );
        logger.debug( "search: Column:" + colName + ", Query: " + query);
		// Create SQL query.
		String sqlQuery = "SELECT * FROM " + TABLENAME + " WHERE " + colName + " LIKE '" + query + "'";
		// Create results list.
		List< AccClassPrerequisiteAndDAM > output = new ArrayList<>();
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
				output.add( new AccClassPrerequisiteAndDAM(srs.getInt(COL_PREREQUISITEID), srs.getInt(COL_REQUIRED_CLASSID)));
			}
			if ( !rowsFound )
			{
				logger.debug( "search: No rows were found." );
				throw new NoRowsFoundException( "No rows found in " + TABLENAME + " table." );
			}
		}
		catch ( NoRowsFoundException e )
		{
			logger.debug( "search: NoRowsFoundException occured." );
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
     * Inserts the {@link AccClassPrerequisiteAndDAM class} into the {@value #TABLENAME} table.
     * @param accClassPrerequisiteAndDAM The {@link AccClassPrerequisiteAndDAM} to insert into the {@value #TABLENAME} table. Does NOT use the ID property.
     * @return boolean Status of creation. True: {@link AccClassPrerequisiteAndDAM Class} was added to the {@value #TABLENAME} table. 
     *          False: {@link AccClassPrerequisiteAndDAM Class} was NOT added to the {@value #TABLENAME} table.
     * @throws DataAccessException An access exception occured.
     * @throws Exception Catch all for any exception.
     */
    public boolean create( AccClassPrerequisiteAndDAM accClassPrerequisiteAndDAM) throws DataAccessException, Exception
    {
        logger.debug( "create: Started." );
		// Create SQL query.
		String sqlQuery = "INSERT INTO " + TABLENAME 
			+ "("+ COL_PREREQUISITEID + "," + COL_REQUIRED_CLASSID + ")"
			+ " VALUES(?,?)";
		// Exception catch.
		try
		{
			// Run SQL Query.
			logger.debug( "create: SQL querry started running." );
			int rows = jdbcTemplate.update(sqlQuery, 
				accClassPrerequisiteAndDAM.getPrerequisiteID(), 
				accClassPrerequisiteAndDAM.getRequiredClassID());
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
     * Delets the inputed {@link AccClassPrerequisiteAndDAM class} from the {@value #TABLENAME} table using the {@link AccClassPrerequisiteAndDAM class'} ID 
     *  to select the database entry to delete. 
     * @param input The {@link AccClassPrerequisiteAndDAM} to delete into the {@value #TABLENAME} table. Requires ID property.
     * @return boolean Status of deletion. True: {@link AccClassPrerequisiteAndDAM} was deleted from the {@value #TABLENAME} table. 
     *          False: {@link AccClassPrerequisiteAndDAM } was NOT deleted from the {@value #TABLENAME} table.
     * @throws DataAccessException An access exception occured.
     * @throws Exception Catch all for any exception.
     */
    public boolean delete( AccClassPrerequisiteAndDAM input ) throws DataAccessException
	{
		logger.debug( "delete: Started." );
		// Create sql statement.
		String sqlQ = "DELETE FROM " + TABLENAME 
			+ "WHERE " + COL_PREREQUISITEID + " = ? "
			+ "AND " + COL_REQUIRED_CLASSID + " = ? ";
		try
		{
			// delete
			int rows = this.jdbcTemplate.update( sqlQ, input.getPrerequisiteID(), input.getRequiredClassID() );
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
			logger.error( "delete: DataAccessException occured." );
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