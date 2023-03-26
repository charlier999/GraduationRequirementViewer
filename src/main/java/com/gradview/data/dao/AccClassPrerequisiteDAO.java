package com.gradview.data.dao;

import com.gradview.data.dam.AccClassPrerequisiteDAM;
import com.gradview.exception.NoRowsFoundException;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.InvalidResultSetAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

@Repository
public class AccClassPrerequisiteDAO
{
    private static final Logger logger = LoggerFactory.getLogger(AccClassPrerequisiteDAO.class);
	private static final String TABLENAME = "`acc-class-prerequisite`";
	public static final String COL_ID = "id";
    public static final String COL_CLASSID = "name";

    @Autowired
	private JdbcTemplate jdbcTemplate;

    /** 
     * Retrieves a list of all classes prerequistes from {@value #TABLENAME}
     * @return List< {@link AccClassPrerequisiteDAM} >
     * @throws NoRowsFoundException No rows were found in the table.
     * @throws DataAccessException An access exception occured.
     * @throws Exception Catch all for any exception.
     */
    public List< AccClassPrerequisiteDAM > getAll() throws NoRowsFoundException, DataAccessException, Exception
    {
        logger.info( "getAll: Started." );
		// Create SQL query.
		String sqlQuery = "SELECT * FROM " + TABLENAME;
		// Create results list.
		List< AccClassPrerequisiteDAM > output = new ArrayList<>();
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
				output.add( new AccClassPrerequisiteDAM(srs.getInt(COL_ID), srs.getInt(COL_CLASSID)));
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
     * Searches for {@link AccClassPrerequisiteDAM classes} in the requested column for the query in the {@value #TABLENAME} table.
     * @param colName The column to search for the query in.
     * @param query The value to search in the column for.
     * @return List< {@link AccClassPrerequisiteDAM} >
     * @throws NoRowsFoundException No rows were found in the table.
     * @throws DataAccessException An access exception occured.
     * @throws Exception Catch all for any exception.
     */
    public List< AccClassPrerequisiteDAM > search(String colName, String query) throws NoRowsFoundException, DataAccessException, Exception
    {
        logger.info( "search: Started." );
        logger.info( "search: Column:" + colName + ", Query: " + query);
		// Create SQL query.
		String sqlQuery = "SELECT * FROM " + TABLENAME + " WHERE " + colName + " LIKE '" + query + "'";
		// Create results list.
		List< AccClassPrerequisiteDAM > output = new ArrayList<>();
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
				output.add( new AccClassPrerequisiteDAM(srs.getInt(COL_ID), srs.getInt(COL_CLASSID)));
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
		logger.info( "search: Returns " + output.size() + " resulting rows." );
		// Return batteries list.
		return output;
    }

    
    /** 
     * Inserts the {@link AccClassPrerequisiteDAM class} into the {@value #TABLENAME} table.
     * @param accClassPrerequisiteDAM The {@link AccClassPrerequisiteDAM} to insert into the {@value #TABLENAME} table. Does NOT use the ID property.
     * @return int The primary key id of the inserted prerequisite
     * @throws DataAccessException An access exception occured.
     * @throws Exception Catch all for any exception.
     */
    public int create( AccClassPrerequisiteDAM accClassPrerequisiteDAM) throws DataAccessException, Exception
    {
        logger.info( "create: Started." );
		// Create SQL query.
		String sqlQuery = "INSERT INTO " + TABLENAME + "(" + COL_CLASSID + ") VALUES(?)";
		// Exception catch.
		try
		{
			// Run SQL Query.
			logger.info( "create: SQL querry started running." );
			SqlParameterSource parameters = new MapSqlParameterSource().addValue("classid", accClassPrerequisiteDAM.getClassID());
			KeyHolder keyHolder = new GeneratedKeyHolder();
			int rows = jdbcTemplate.update(sqlQuery, parameters, keyHolder);
            if ( rows == 1 )
			{
				logger.info( "create: Insert Success" );
				int primaryKeyID = keyHolder.getKey().intValue();
				logger.info( "create: Returns true." );
				return primaryKeyID;
			}
			else
			{
				logger.error( "create: Insert Failed" );
				logger.info( "create: Returns -1." );
				return -1;
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
        logger.info("create: Returns -1.");
        return -1;
    }

    /**
     * Delets the inputed {@link AccClassPrerequisiteDAM class} from the {@value #TABLENAME} table using the {@link AccClassPrerequisiteDAM class'} ID 
     *  to select the database entry to delete. 
     * @param input The {@link AccClassPrerequisiteDAM} to delete into the {@value #TABLENAME} table. Requires ID property.
     * @return boolean Status of deletion. True: {@link AccClassPrerequisiteDAM} was deleted from the {@value #TABLENAME} table. 
     *          False: {@link AccClassPrerequisiteDAM } was NOT deleted from the {@value #TABLENAME} table.
     * @throws DataAccessException An access exception occured.
     * @throws Exception Catch all for any exception.
     */
    public boolean delete( AccClassPrerequisiteDAM input ) throws DataAccessException
	{
		logger.info( "delete: Started." );
		// Create sql statement.
		String sqlQ = "DELETE FROM " + TABLENAME + "WHERE " + COL_ID + " = ? ";
		try
		{
			// delete
			int rows = this.jdbcTemplate.update( sqlQ, input.getId() );
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