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

import com.gradview.data.dam.AccProgramDAM;
import com.gradview.exception.NoRowsFoundException;

public class AccProgramDAO 
{
    private static final Logger logger = LoggerFactory.getLogger(AccProgramDAO.class);
    private static final String TABLENAME = "acc-program";
	public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_DESCRIPTION = "description";
    public static final String COL_LEVEL = "level";
    public static final String COL_BAOFARTS = "ba-of-arts";
    public static final String COL_BAOFSCIENCE = "ba-of-science";
    
    @Autowired
	private DataSource dataSource;
    
    @Autowired
	private JdbcTemplate jdbcTemplate;
    /** 
     * Retrieves a list of all {@link AccProgramDAM programs} from {@value #TABLENAME}
     * @return List< {@link AccProgramDAM} >
     * @throws NoRowsFoundException No rows were found in the table.
     * @throws DataAccessException An access exception occured.
     * @throws Exception Catch all for any exception.
     */
    public List< AccProgramDAM > getAll() throws NoRowsFoundException, DataAccessException, Exception
    {
        logger.info( "getAll: Started." );
		// Create SQL query.
		String sqlQuery = "SELECT * FROM " + TABLENAME;
		// Create results list.
		List< AccProgramDAM > output = new ArrayList<>();
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
				output.add( new AccProgramDAM(srs.getInt(COL_ID), srs.getString(COL_NAME), 
                            srs.getString(COL_DESCRIPTION), srs.getString(COL_LEVEL), 
                            srs.getBoolean(COL_BAOFARTS), srs.getBoolean(COL_BAOFSCIENCE)));
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
     * Searches for {@link AccProgramDAM programs} in the requested column for the query in the {@value #TABLENAME} table.
     * @param colName The column to search for the query in.
     * @param query The value to search in the column for.
     * @return List< {@link AccProgramDAM} >
     * @throws NoRowsFoundException No rows were found in the table.
     * @throws DataAccessException An access exception occured.
     * @throws Exception Catch all for any exception.
     */
    public List< AccProgramDAM > search(String colName, String query) throws NoRowsFoundException, DataAccessException, Exception
    {
        logger.info( "search: Started." );
        logger.info( "search: Column:" + colName + ", Query: " + query);
		// Create SQL query.
		String sqlQuery = "SELECT * FROM " + TABLENAME + " WHERE " + colName + " LIKE '" + query + "'";
		// Create results list.
		List< AccProgramDAM > output = new ArrayList<>();
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
				output.add( new AccProgramDAM(srs.getInt(COL_ID), srs.getString(COL_NAME), 
                            srs.getString(COL_DESCRIPTION), srs.getString(COL_LEVEL), 
                            srs.getBoolean(COL_BAOFARTS), srs.getBoolean(COL_BAOFSCIENCE)));
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
     * Inserts the {@link AccProgramDAM program} into the {@value #TABLENAME} table.
     * @param acccompetency The {@link AccProgramDAM program} to insert into the {@value #TABLENAME} table.
     * @return boolean Status of creation. True: {@link AccProgramDAM program} was added to the {@value #TABLENAME} table. 
     *          False: {@link AccProgramDAM program} was NOT added to the {@value #TABLENAME} table.
     * @throws DataAccessException An access exception occured.
     * @throws Exception Catch all for any exception.
     */
    public boolean create( AccProgramDAM input) throws DataAccessException, Exception
    {
        logger.info( "create: Started." );
		// Create SQL query.
		String sqlQuery = "INSERT INTO " + TABLENAME + "(" + COL_NAME + ", " + COL_DESCRIPTION + ", " + COL_LEVEL + ", " 
                        + COL_BAOFARTS  + ", " + COL_BAOFSCIENCE + ") VALUES(?,?,?,?,?)";
		// Exception catch.
		try
		{
			// Run SQL Query.
			logger.info( "create: SQL querry started running." );
			int rows = jdbcTemplate.update(sqlQuery, input.getName(), input.getDescription(), input.getLevel(), input.isBaOfArts(), input.isBaOfScience());
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
     * Updates the {@link AccProgramDAM program} into the {@value #TABLENAME} table using the {@link AccProgramDAM}'s ID 
     *  to select the database entry to update. 
     * @param acccompetency The {@link AccProgramDAM program} to update into the {@value #TABLENAME} table. Requires competencyID property.
     * @return boolean Status of update. True: {@link AccProgramDAM program} was updated in the {@value #TABLENAME} table. 
     *          False: {@link AccProgramDAM program} was NOT updated in the {@value #TABLENAME} table.
     * @throws DataAccessException An access exception occured.
     * @throws Exception Catch all for any exception.
     */
    public boolean update( AccProgramDAM input) throws DataAccessException, Exception
    {
        logger.info( "update: Started." );
		// Create SQL query.
		String sqlQuery = "UPDATE " + TABLENAME + " SET " + COL_NAME + " = ?, " + COL_DESCRIPTION + " = ?, "
                            + COL_LEVEL + " = ?, " + COL_BAOFARTS + " = ?, " + COL_BAOFSCIENCE + " = ?, "  + "WHERE " + COL_ID + " = ?";
		// Exception catch.
		try
		{
			// Run SQL Query.
			logger.info( "update: SQL querry started running." );
			int rows = jdbcTemplate.update(sqlQuery, input.getName(), input.getDescription(), input.getLevel(), input.isBaOfArts(), input.isBaOfScience(), input.getId());
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
     * Delets the inputed {@link AccProgramDAM program} from the {@value #TABLENAME} table using both of {@link AccProgramDAM}'s ID
     *  to select the database entry to delete. 
     * @param input The {@link AccProgramDAM} to delete into the {@value #TABLENAME} table. Requires competencyID property.
     * @return boolean Status of deletion. True: {@link AccProgramDAM program} was deleted from the {@value #TABLENAME} table. 
     *          False: {@link AccProgramDAM program} was NOT deleted from the {@value #TABLENAME} table.
     * @throws DataAccessException An access exception occured.
     * @throws Exception Catch all for any exception.
     */
    public boolean delete( AccProgramDAM input ) throws DataAccessException
	{
		logger.info( "delete: Started." );
		// Create sql statement.
		String sqlQ = "DELETE FROM " + TABLENAME + "WHERE " + COL_ID + " = ?";
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
