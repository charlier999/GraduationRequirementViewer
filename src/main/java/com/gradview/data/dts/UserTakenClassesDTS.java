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

import com.gradview.data.dao.UserTakenClassesDAO;
import com.gradview.exception.NoRowsFoundException;
import com.gradview.exception.TooManyRowsException;

/**
 * Accesses and interacts with the {@value #TABLENAME} table.
 * 
 * @author Charles Davis
 */
@Service
public class UserTakenClassesDTS
{
	private static final Logger logger = LoggerFactory.getLogger( UserTakenClassesDTS.class );
	private static final String TABLENAME = "user-taken-classes";
	public static final String USERIDCOL = "userID";
	public static final String CLASSIDCOL = "classID";

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
	public List< UserTakenClassesDAO > getAllUserTakenClasses() throws NoRowsFoundException, DataAccessException
	{
		logger.info( "getAllUserTakenClasses: Started." );
		// Create SQL query.
		String sqlQuery = "SELECT * FROM " + TABLENAME;
		// Create results list.
		List< UserTakenClassesDAO > userTakenClassesOutput = new ArrayList<>();
		// Exception catch.
		try
		{
			// Run SQL Query.
			logger.info( "getAllUserTakenClasses: SQL querry started running." );
			SqlRowSet srs = jdbcTemplate.queryForRowSet( sqlQuery );
			// Create rowsFound check value.
			boolean rowsFound = false;
			// Loop through all resulting rows.
			logger.info( "getAllUserTakenClasses: Looping through the resulting row set." );
			while ( srs.next() )
			{
				rowsFound = true;
				// Add battery to output list.
				userTakenClassesOutput
						.add( new UserTakenClassesDAO( srs.getInt( USERIDCOL ), srs.getInt( USERIDCOL ) ) );
			}
			if ( !rowsFound )
			{
				logger.warn( "getAllUserTakenClasses: No rows were found." );
				throw new NoRowsFoundException( "No rows found in " + TABLENAME + " table." );
			}
		}
		catch ( NoRowsFoundException e )
		{
			logger.warn( "getAllUserTakenClasses: NoRowsFoundException occured." );
			throw e;
		}
		catch ( InvalidResultSetAccessException e )
		{
			logger.error( "getAllUserTakenClasses: InvalidResultSetAccessException occured." );
			e.printStackTrace();
		}
		catch ( DataAccessException e )
		{
			logger.error( "getAllUserTakenClasses: DataAccessException occured." );
			throw e;
		}
		catch ( Exception ex )
		{
			logger.error( "getAllUserTakenClasses: Exception occured." );
			// Print a Stack Trace if an exception occurs.
			ex.printStackTrace();
		}
		logger.info( "getAllUserTakenClasses: Returns " + userTakenClassesOutput.size() + " resulting rows." );
		// Return batteries list.
		return userTakenClassesOutput;
	}

	/**
	 * Searches for {@link UserTakenClassesDAO entries} in the requested column for
	 * the query in the {@value #TABLENAME} table.
	 * 
	 * @param column - The column to search for the query in.
	 * @param query  - The value to search in the column for.
	 * @return A list of {@link UserTakenClassesDAO entries} that meet the requested
	 *         criteria.
	 * @throws NoRowsFoundException There were no {@link UserTakenClassesDAO
	 *                              entries} found in the {@value #TABLENAME} table.
	 * @throws DataAccessException  See message for reason for exception.
	 */
	public List< UserTakenClassesDAO > search( String column, String query )
			throws NoRowsFoundException, DataAccessException
	{
		logger.info( "search: Started." );
		logger.info( "search: Querry:'" + query + "'. Column:'" + column + "'." );
		// Create sql command
		String sqlQ = "SELECT * FROM " + TABLENAME + " WHERE " + column + " LIKE '" + query + "'";
		// Create results list.
		List< UserTakenClassesDAO > userTakenClassesOutput = new ArrayList<>();

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

				userTakenClassesOutput
						.add( new UserTakenClassesDAO( srs.getInt( USERIDCOL ), srs.getInt( USERIDCOL ) ) );
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
		logger.info( "search: Returns " + userTakenClassesOutput.size() + " resulting rows." );
		return userTakenClassesOutput;
	}

	/**
	 * Retrieves the requested entry by userID and classID.
	 * 
	 * @param userID  - The userID to search for.
	 * @param classID - The classID to search for.
	 * @return The requested {@link UserTakenClassesDAO}
	 * @throws NoRowsFoundException
	 */
	public UserTakenClassesDAO getUserTakenClass( int userID, int classID ) throws NoRowsFoundException
	{
		logger.info( "getUserTakenClass: Started." );
		logger.info( "getUserTakenClass: UserID:'" + userID + "'. ClassID:'" + classID + "'." );
		// Create sql command
		String sqlQ = "SELECT * FROM " + TABLENAME + " WHERE " + USERIDCOL + " = ? AND '" + CLASSIDCOL + "' = ?";
		// Create results list.
		List< UserTakenClassesDAO > userTakenClassesOutput = new ArrayList<>();

		try
		{
			// run query.
			logger.info( "getUserTakenClass: SQL querry starts running." );
			SqlRowSet srs = this.jdbcTemplate.queryForRowSet( sqlQ );
			// Create first iteration check.
			int iLoop = 0;
			// Loop through all resulting rows.
			logger.info( "getUserTakenClass: Looping through the resulting row set." );
			while ( srs.next() )
			{
				// iterate the iterator.
				iLoop++ ;

				userTakenClassesOutput
						.add( new UserTakenClassesDAO( srs.getInt( USERIDCOL ), srs.getInt( USERIDCOL ) ) );
			}
			// Check to see if this is the first iteration and if so throw exception.
			if ( iLoop == 0 )
			{
				logger.warn( "getUserTakenClass: No rows were found." );
				throw new NoRowsFoundException();
			}
			else if ( iLoop == 2 )
			{
				logger.warn( "getUserTakenClass: ToManyRowsException occured." );
				throw new TooManyRowsException();
			}
		}
		catch ( NoRowsFoundException e )
		{
			logger.warn( "getUserTakenClass: NoRowsFoundException occured." );
			throw e;
		}
		catch ( InvalidResultSetAccessException e )
		{
			logger.warn( "getUserTakenClass: InvalidResultSetAccessException occured." );
			e.printStackTrace();
		}
		catch ( DataAccessException e )
		{
			logger.warn( "getUserTakenClass: DataAccessException occured." );
			throw e;
		}
		catch ( Exception ex )
		{
			// Print a Stack Trace if an exception occurs.
			logger.error( "getUserTakenClass: Exception occured." );
			ex.printStackTrace();
		}
		logger.info( "getUserTakenClass: Returns UserID:'" + userTakenClassesOutput.get( 0 ).getUserID()
				+ "'. ClassID:'" + userTakenClassesOutput.get( 0 ).getClassID() + "'." );
		return userTakenClassesOutput.get( 0 );
	}

	/**
	 * Adds the inputed {@link UserTakenClassesDAO} to the {@value #TABLENAME}
	 * table.
	 * 
	 * @param entry - The {@link UserTakenClassesDAO} to add to the
	 *              {@value #TABLENAME} table.
	 * @return Status of creation. True: {@link UserTakenClassesDAO} was added to
	 *         the {@value #TABLENAME} table. False: {@link UserTakenClassesDAO }
	 *         was NOT added to the database.
	 * @throws DataAccessException An issue occurred, {@link UserTakenClassesDAO }
	 *                             was not added to the {@value #TABLENAME}.
	 */
	public boolean create( UserTakenClassesDAO entry ) throws DataAccessException
	{
		logger.info( "create: Started." );
		// Create sql statement.
		String sqlQ = "INSERT INTO " + TABLENAME + "(" + USERIDCOL + ", " + CLASSIDCOL + ") VALUES(?, ?)";

		try
		{
			// create
			int rows = this.jdbcTemplate.update( sqlQ, entry.getUserID(), entry.getClass() );
			if ( rows == 1 )
			{
				logger.info( "create: Inserted UserTakenClassesDAO into the DB succesfly." );
				logger.info( "create: Returns true." );
				return true;
			}
			else
			{
				logger.error( "create: Faild to insert UserTakenClassesDAO into DB." );
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
		logger.error( "create: Faild to insert UserTakenClassesDAO into DB." );
		logger.info( "create: Returns false." );
		return false;
	}

	/**
	 * removes the inputed {@link UserTakenClassesDAO} from the {@value #TABLENAME}
	 * table.
	 * 
	 * @param entry - The {@link UserTakenClassesDAO} to remove from the
	 *              {@value #TABLENAME} table.
	 * @return Status of deletion. True: {@link UserTakenClassesDAO} was deleted to
	 *         the {@value #TABLENAME} table. False: {@link UserTakenClassesDAO }
	 *         was NOT deleted to the database.
	 * @throws DataAccessException An issue occurred, {@link UserTakenClassesDAO }
	 *                             was not deleted to the {@value #TABLENAME}.
	 */
	public boolean delete( UserTakenClassesDAO entry ) throws DataAccessException
	{
		logger.info( "delete: Started." );
		// Create sql statement.
		String sqlQ = "DELETE FROM " + TABLENAME + "WHERE " + USERIDCOL + " = ? AND '" + CLASSIDCOL + "' = ?";
		try
		{
			// delete
			int rows = this.jdbcTemplate.update( sqlQ, entry.getUserID(), entry.getClass() );
			if ( rows == 1 )
			{
				logger.info( "delete: Deleted UserTakenClassesDAO from the DB succesfly." );
				logger.info( "delete: Returns true." );
				return true;
			}
			else
			{
				logger.error( "delete: Faild to delete UserTakenClassesDAO from DB." );
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
		logger.error( "delete: Faild to delete UserTakenClassesDAO from DB." );
		logger.info( "delete: Returns false." );
		return false;
	}
}
