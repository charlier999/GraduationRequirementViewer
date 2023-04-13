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

import com.gradview.data.dam.AccGeneralEducationClassesDAM;
import com.gradview.exception.NoRowsFoundException;

@Component
public class AccGeneralEducationClassesDAO 
{
    private static final Logger logger = LoggerFactory.getLogger(AccGeneralEducationClassesDAO.class);
    private static final String TABLENAME = "acc-general-education-classes";
	public static final String COL_CLASSID = "classID";
    public static final String COL_COMPETENCYID = "competencyID";
    public static final String COL_BAOFARTS = "ba-of-arts";
    public static final String COL_BAOFSCIENCE = "ba-of-science";
    
    @Autowired
	private JdbcTemplate jdbcTemplate;
    /** 
     * Retrieves a list of all {@link AccGeneralEducationClassesDAM general education classes} from {@value #TABLENAME}
     * @return List< {@link AccGeneralEducationClassesDAM} >
     * @throws NoRowsFoundException No rows were found in the table.
     * @throws DataAccessException An access exception occured.
     * @throws Exception Catch all for any exception.
     */
    public List< AccGeneralEducationClassesDAM > getAll() throws NoRowsFoundException, DataAccessException, Exception
    {
        logger.debug( "getAll: Started." );
		// Create SQL query.
		String sqlQuery = "SELECT * FROM " + TABLENAME;
		// Create results list.
		List< AccGeneralEducationClassesDAM > output = new ArrayList<>();
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
				output.add( new AccGeneralEducationClassesDAM(srs.getInt(COL_CLASSID), srs.getInt(COL_COMPETENCYID), 
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
		logger.debug( "getAll: Returns " + output.size() + " resulting rows." );
		// Return batteries list.
		return output;
    }

    /** 
     * Searches for {@link AccGeneralEducationClassesDAM general education classes} in the requested column for the query in the {@value #TABLENAME} table.
     * @param colName The column to search for the query in.
     * @param query The value to search in the column for.
     * @return List< {@link AccGeneralEducationClassesDAM} >
     * @throws NoRowsFoundException No rows were found in the table.
     * @throws DataAccessException An access exception occured.
     * @throws Exception Catch all for any exception.
     */
    public List< AccGeneralEducationClassesDAM > search(String colName, String query) throws NoRowsFoundException, DataAccessException, Exception
    {
        logger.debug( "search: Started." );
        logger.debug( "search: Column:" + colName + ", Query: " + query);
		// Create SQL query.
		String sqlQuery = "SELECT * FROM " + TABLENAME + " WHERE " + colName + " LIKE '" + query + "'";
		// Create results list.
		List< AccGeneralEducationClassesDAM > output = new ArrayList<>();
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
				output.add( new AccGeneralEducationClassesDAM(srs.getInt(COL_CLASSID), srs.getInt(COL_COMPETENCYID), 
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
		logger.debug( "search: Returns " + output.size() + " resulting rows." );
		// Return batteries list.
		return output;
    }

    /** 
     * Inserts the {@link AccGeneralEducationClassesDAM general education class} into the {@value #TABLENAME} table.
     * @param accClass The {@link AccGeneralEducationClassesDAM general education class} to insert into the {@value #TABLENAME} table.
     * @return boolean Status of creation. True: {@link AccGeneralEducationClassesDAM general education class} was added to the {@value #TABLENAME} table. 
     *          False: {@link AccGeneralEducationClassesDAM general education class} was NOT added to the {@value #TABLENAME} table.
     * @throws DataAccessException An access exception occured.
     * @throws Exception Catch all for any exception.
     */
    public boolean create( AccGeneralEducationClassesDAM input) throws DataAccessException, Exception
    {
        logger.debug( "create: Started." );
		// Create SQL query.
		String sqlQuery = "INSERT INTO " + TABLENAME + "(" + COL_CLASSID + ", " + COL_COMPETENCYID + ", " + COL_BAOFARTS + ", " + COL_BAOFSCIENCE + ") VALUES(?,?,?,?)";
		// Exception catch.
		try
		{
			// Run SQL Query.
			logger.debug( "create: SQL querry started running." );
			int rows = jdbcTemplate.update(sqlQuery, input.getClassID(), input.getCompetencyID(), input.isBaOfArts(), input.isBaOfScience());
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
     * Updates the {@link AccGeneralEducationClassesDAM general education class} into the {@value #TABLENAME} table using the {@link AccGeneralEducationClassesDAM}' classID 
     *  to select the database entry to update. 
     * @param accClass The {@link AccGeneralEducationClassesDAM general education class} to update into the {@value #TABLENAME} table. Requires classID property.
     * @return boolean Status of update. True: {@link AccGeneralEducationClassesDAM general education class} was updated in the {@value #TABLENAME} table. 
     *          False: {@link AccGeneralEducationClassesDAM general education class} was NOT updated in the {@value #TABLENAME} table.
     * @throws DataAccessException An access exception occured.
     * @throws Exception Catch all for any exception.
     */
    public boolean update( AccGeneralEducationClassesDAM input) throws DataAccessException, Exception
    {
        logger.debug( "update: Started." );
		// Create SQL query.
		String sqlQuery = "UPDATE " + TABLENAME + " SET " + COL_CLASSID + " = ?, " + COL_COMPETENCYID + " = ?, "
                            + COL_BAOFARTS + " = ?, " + COL_BAOFSCIENCE + " = ?, "  + "WHERE " + COL_CLASSID + " = ?";
		// Exception catch.
		try
		{
			// Run SQL Query.
			logger.debug( "update: SQL querry started running." );
			int rows = jdbcTemplate.update(sqlQuery, input.getClassID(), input.getCompetencyID(), input.isBaOfArts(), input.isBaOfScience(), input.getClassID());
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
     * Delets the inputed {@link AccGeneralEducationClassesDAM general education class} from the {@value #TABLENAME} table using both of {@link AccGeneralEducationClassesDAM}'s class ID
     *  to select the database entry to delete. 
     * @param input The {@link AccGeneralEducationClassesDAM} to delete into the {@value #TABLENAME} table. Requires classID property.
     * @return boolean Status of deletion. True: {@link AccGeneralEducationClassesDAM general education class} was deleted from the {@value #TABLENAME} table. 
     *          False: {@link AccGeneralEducationClassesDAM general education class} was NOT deleted from the {@value #TABLENAME} table.
     * @throws DataAccessException An access exception occured.
     * @throws Exception Catch all for any exception.
     */
    public boolean delete( AccGeneralEducationClassesDAM input ) throws DataAccessException
	{
		logger.debug( "delete: Started." );
		// Create sql statement.
		String sqlQ = "DELETE FROM " + TABLENAME + "WHERE " + COL_CLASSID + " = ?";
		try
		{
			// delete
			int rows = this.jdbcTemplate.update( sqlQ, input.getClassID() );
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
