/**
 * 
 */
package com.gradview.data.dts;

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
import org.springframework.stereotype.Service;

import com.gradview.data.dao.RolePermissionsDAO;
import com.gradview.exception.NoRowsFoundException;

/**
 * Accesses and interacts with the {@value #TABLENAME} table.
 * 
 * @author Charles Davis
 */
@Service
public class RolePermissionsDTS
{
	private static final Logger logger = LoggerFactory.getLogger( RolePermissionsDTS.class );
	private static final String DAOOBJ = "RolePermissionsDAO";
	private static final String TABLENAME = "role-permissions";
	public static final String PERMISSIONIDCOL = "permissionID";
	public static final String ROLEIDCOL = "roleID";

	/**
	 * The access source to the database.
	 */
	@SuppressWarnings( "unused" )
	@Autowired
	private DataSource dataSource;

	/**
	 * The interface with the data source.
	 */
	@SuppressWarnings( "unused" )
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/**
	 * Retrieves all entries from the {@value #TABLENAME} table.
	 * 
	 * @return The list of all entries from the {@value #TABLENAME} table.
	 * @throws NoRowsFoundException There were no entries found in the
	 *                              {@value #TABLENAME} table.
	 * @throws DataAccessException  See message for reason for exception.
	 */
	public List< RolePermissionsDAO > getAllRows() throws NoRowsFoundException, DataAccessException
	{
		logger.info( "getAllRows: Started." );
		// Create SQL query.
		String sqlQuery = "SELECT * FROM " + TABLENAME;
		// Create results list.
		List< RolePermissionsDAO > rowsOutput = new ArrayList<>();
		// Exception catch.
		try
		{
			// Run SQL Query.
			logger.info( "getAllRows: SQL querry started running." );
			SqlRowSet srs = jdbcTemplate.queryForRowSet( sqlQuery );
			// Create rowsFound check value.
			boolean rowsFound = false;
			// Loop through all resulting rows.
			logger.info( "getAllRows: Looping through the resulting row set." );
			while ( srs.next() )
			{
				rowsFound = true;
				// Add battery to output list.
				rowsOutput.add( new RolePermissionsDAO( srs.getInt( PERMISSIONIDCOL ), srs.getInt( ROLEIDCOL ) ) );
			}
			if ( !rowsFound )
			{
				logger.warn( "getAllRows: No rows were found." );
				throw new NoRowsFoundException( "No rows found in " + TABLENAME + " table." );
			}
		}
		catch ( NoRowsFoundException e )
		{
			logger.warn( "getAllRows: NoRowsFoundException occured." );
			throw e;
		}
		catch ( InvalidResultSetAccessException e )
		{
			logger.error( "getAllRows: InvalidResultSetAccessException occured." );
			e.printStackTrace();
		}
		catch ( DataAccessException e )
		{
			logger.error( "getAllRows: DataAccessException occured." );
			throw e;
		}
		catch ( Exception ex )
		{
			logger.error( "getAllRows: Exception occured." );
			// Print a Stack Trace if an exception occurs.
			ex.printStackTrace();
		}
		logger.info( "getAllRows: Returns " + rowsOutput.size() + " resulting rows." );
		// Return batteries list.
		return rowsOutput;
	}

	/**
	 * Searches for {@value #DAOOBJ} entries in the requested column for the query
	 * in the {@value #TABLENAME} table.
	 * 
	 * @param column - The column to search for the query in.
	 * @param query  - The value to search in the column for.
	 * @return A list of {@value #DAOOBJ} entries that meet the requested criteria.
	 * @throws NoRowsFoundException There were no {@value #DAOOBJ} entries found in
	 *                              the {@value #TABLENAME} table.
	 * @throws DataAccessException  See message for reason for exception.
	 */
	public List< RolePermissionsDAO > search( String column, String query )
			throws NoRowsFoundException, DataAccessException
	{
		logger.info( "search: Started." );
		logger.info( "search: Querry:'" + query + "'. Column:'" + column + "'." );
		// Create sql command
		String sqlQ = "SELECT * FROM " + TABLENAME + " WHERE " + column + " LIKE '" + query + "'";
		// Create results list.
		List< RolePermissionsDAO > searchOutput = new ArrayList<>();

		try
		{
			// run query.
			logger.info( "search: SQL querry starts running." );
			SqlRowSet srs = this.jdbcTemplate.queryForRowSet( sqlQ );
			// Create first iteration check.
			boolean firstLoop = true;
			// Loop through all resulting rows.
			logger.info( "search: Looping through the resulting row set." );
			while ( srs.next() )
			{
				// change first iteration check to false.
				firstLoop = false;

				searchOutput.add( new RolePermissionsDAO( srs.getInt( PERMISSIONIDCOL ), srs.getInt( ROLEIDCOL ) ) );
			}
			// Check to see if this is the first iteration and if so throw exception.
			if ( firstLoop )
			{
				logger.warn( "search: No rows were found." );
				throw new NoRowsFoundException();
			}
		}
		catch ( NoRowsFoundException e )
		{
			logger.warn( "search: NoRowsFoundException occured." );
			throw e;
		}
		catch ( InvalidResultSetAccessException e )
		{
			logger.warn( "search: InvalidResultSetAccessException occured." );
			e.printStackTrace();
		}
		catch ( DataAccessException e )
		{
			logger.warn( "search: DataAccessException occured." );
			throw e;
		}
		catch ( Exception ex )
		{
			// Print a Stack Trace if an exception occurs.
			logger.error( "search: Exception occured." );
			ex.printStackTrace();
		}
		logger.info( "search: Returns " + searchOutput.size() + " resulting rows." );
		return searchOutput;
	}

	/**
	 * Adds the inputed {@value #DAOOBJ} to the {@value #TABLENAME} table.
	 * 
	 * @param entry - The {@value #DAOOBJ} to add to the {@value #TABLENAME} table.
	 * @return Status of creation. True: {@value #DAOOBJ} was added to the
	 *         {@value #TABLENAME} table. False: {@value #DAOOBJ} was NOT added to
	 *         the database.
	 * @throws DataAccessException An issue occurred, {@value #DAOOBJ} was not added
	 *                             to the {@value #TABLENAME}.
	 */
	public boolean create( RolePermissionsDAO entry ) throws DataAccessException
	{
		logger.info( "create: Started." );
		// Create sql statement.
		String sqlQ = "INSERT INTO " + TABLENAME + "(" + PERMISSIONIDCOL + ", " + ROLEIDCOL + ") VALUES(?, ?)";

		try
		{
			// create
			int rows = this.jdbcTemplate.update( sqlQ, entry.getPermissionID(), entry.getRoleID() );
			if ( rows == 1 )
			{
				logger.info( "create: Inserted " + DAOOBJ + " into the DB succesfly." );
				logger.info( "create: Returns true." );
				return true;
			}
			else
			{
				logger.error( "create: Faild to insert " + DAOOBJ + " into DB." );
				logger.info( "create: Returns false." );
				return false;
			}
		}
		catch ( DataAccessException e )
		{
			logger.warn( "create: DataAccessException occured." );
			throw e;
		}
		catch ( Exception ex )
		{
			logger.error( "create: Exception occured." );
			// Print a Stack Trace if an exception occurs.
			ex.printStackTrace();
		}
		logger.error( "create: Faild to insert " + DAOOBJ + " into DB." );
		logger.info( "create: Returns false." );
		return false;
	}

	/**
	 * removes the inputed {@value #DAOOBJ} from the {@value #TABLENAME} table.
	 * 
	 * @param entry - The {@value #DAOOBJ} to remove from the {@value #TABLENAME}
	 *              table.
	 * @return Status of deletion. True: {@value #DAOOBJ} was deleted to the
	 *         {@value #TABLENAME} table. False: {@value #DAOOBJ} was NOT deleted to
	 *         the database.
	 * @throws DataAccessException An issue occurred, {@value #DAOOBJ} was not
	 *                             deleted to the {@value #TABLENAME}.
	 */
	public boolean delete( RolePermissionsDAO entry ) throws DataAccessException
	{
		logger.info( "delete: Started." );
		// Create sql statement.
		String sqlQ = "DELETE FROM " + TABLENAME + "WHERE " + PERMISSIONIDCOL + " = ? AND '" + ROLEIDCOL + "' = ?";
		try
		{
			// delete
			int rows = this.jdbcTemplate.update( sqlQ, entry.getPermissionID(), entry.getRoleID() );
			if ( rows == 1 )
			{
				logger.info( "delete: Deleted " + DAOOBJ + " from the DB succesfly." );
				logger.info( "delete: Returns true." );
				return true;
			}
			else
			{
				logger.error( "delete: Faild to delete " + DAOOBJ + " from DB." );
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
		logger.error( "delete: Faild to delete " + DAOOBJ + " from DB." );
		logger.info( "delete: Returns false." );
		return false;
	}

}
