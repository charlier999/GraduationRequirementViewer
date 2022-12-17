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

import com.gradview.data.dao.UsersDAO;
import com.gradview.exception.NoRowsFoundException;

/**
 * Accesses and interacts with the {@value #TABLENAME} table.
 * 
 * @author Charles Davis
 */
@Service
public class UserDTS
{
	private static final Logger logger = LoggerFactory.getLogger( UserDTS.class );
	private static final String TABLENAME = "users";
	public static final String IDCOL = "id";
	public static final String USERNAMECOL = "username";
	public static final String PASSWORDCOL = "password";

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
	 * Retrieves all {@link UsersDAO users} from the {@value #TABLENAME} table.
	 * 
	 * @return The list of all {@link UsersDAO users} from the {@value #TABLENAME}
	 *         table.
	 * @throws NoRowsFoundException There were no {@link UsersDAO users} found in
	 *                              the {@value #TABLENAME} table.
	 * @throws DataAccessException  See message for reason for exception.
	 */
	public List< UsersDAO > getAllUsers() throws NoRowsFoundException, DataAccessException
	{
		logger.info( "getAllUsers: Started." );
		// Create SQL query.
		String sqlQuery = "SELECT * FROM " + TABLENAME;
		// Create results list.
		List< UsersDAO > usersOutput = new ArrayList<>();
		// Exception catch.
		try
		{
			// Run SQL Query.
			logger.info( "getAllUsers: SQL querry started running." );
			SqlRowSet srs = jdbcTemplate.queryForRowSet( sqlQuery );
			// Create rowsFound check value.
			boolean rowsFound = false;
			// Loop through all resulting rows.
			logger.info( "getAllUsers: Looping through the resulting row set." );
			while ( srs.next() )
			{
				rowsFound = true;
				// Add battery to output list.
				usersOutput.add( new UsersDAO( srs.getInt( IDCOL ), srs.getString( USERNAMECOL ),
						srs.getString( PASSWORDCOL ) ) );
			}
			if ( !rowsFound )
			{
				logger.warn( "getAllUsers: No rows were found." );
				throw new NoRowsFoundException( "No users found in " + TABLENAME + " table." );
			}
		}
		catch ( NoRowsFoundException e )
		{
			logger.warn( "getAllUsers: NoRowsFoundException occured." );
			throw e;
		}
		catch ( InvalidResultSetAccessException e )
		{
			logger.error( "getAllUsers: InvalidResultSetAccessException occured." );
			e.printStackTrace();
		}
		catch ( DataAccessException e )
		{
			logger.error( "getAllUsers: DataAccessException occured." );
			throw e;
		}
		catch ( Exception ex )
		{
			logger.error( "getAllUsers: Exception occured." );
			// Print a Stack Trace if an exception occurs.
			ex.printStackTrace();
		}
		logger.info( "getAllUsers: Returns " + usersOutput.size() + " resulting rows." );
		// Return batteries list.
		return usersOutput;
	}

	/**
	 * Searches for {@link UsersDAO users} in the requested column for the query in
	 * the {@value #TABLENAME} table.
	 * 
	 * @param column - The column to search for the query in.
	 * @param query  - The value to search in the column for.
	 * @return A list of {@link UsersDAO users} that meet the requested criteria.
	 * @throws NoRowsFoundException There were no {@link UsersDAO users} found in
	 *                              the {@value #TABLENAME} table.
	 * @throws DataAccessException  See message for reason for exception.
	 */
	public List< UsersDAO > search( String column, String query ) throws NoRowsFoundException, DataAccessException
	{
		logger.info( "search: Started." );
		logger.info( "search: Querry:'" + query + "'. Column:'" + column + "'." );
		// Create sql command
		String sqlQ = "SELECT * FROM " + TABLENAME + " WHERE " + column + " LIKE '" + query + "'";
		// Create results list of items.
		List< UsersDAO > users = new ArrayList<>();

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

				// int enabled = 0;
				// if ( srs.getBoolean( ENABLECOL ) )
				// enabled = 1;
				// else enabled = 0;
				// add user to results list.
				users.add( new UsersDAO( srs.getInt( IDCOL ), srs.getString( USERNAMECOL ),
						srs.getString( PASSWORDCOL ) ) );
			}
			// Check to see if this is the first iteration and if so throw exception.
			if ( firstLoop )
			{
				logger.warn( "search: No users were found." );
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
		logger.info( "search: Returns " + users.size() + " resulting rows." );
		return users;
	}

	/**
	 * Adds the inputed {@link UsersDAO user} to the {@value #TABLENAME} table.
	 * 
	 * @param user - The {@link User user} to add to the {@value #TABLENAME} table.
	 * @return Status of creation. True: {@link UsersDAO user} was added to the
	 *         {@value #TABLENAME} table. False: {@link UsersDAO User} was NOT added
	 *         to the database.
	 * @throws DataAccessException An issue occurred, {@link UsersDAO user} was not
	 *                             created.
	 */
	public boolean create( UsersDAO user ) throws DataAccessException
	{
		logger.info( "create: Started." );
		// Create sql statement.
		String sqlQ = "INSERT INTO " + TABLENAME + "(" + USERNAMECOL + ", " + PASSWORDCOL + ") VALUES(?, ?)";
		try
		{
			// insert User into DB
			int rows = this.jdbcTemplate.update( sqlQ, user.getUsername(), user.getPassword() );
			if ( rows == 1 )
			{
				logger.info( "create: Inserted User into the DB succesfly." );
				logger.info( "create: Returns true." );
				return true;
			}
			else
			{
				logger.error( "create: Faild to insert user into DB." );
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
		logger.error( "create: Faild to insert user into DB." );
		logger.info( "create: Returns false." );
		return false;
	}

	/**
	 * Updates the {@link UsersDAO user} in the database using the {@link UsersDAO
	 * user's} id to select database entry to update.
	 * 
	 * @param user - The {@link UsersDAO user} to update the database with.
	 * @return Status of update. True: {@link UsersDAO} was updated. False:
	 *         {@link UsersDAO} was NOT updated.
	 * @throws DataAccessException An issue occurred, {@link UsersDAO} was not
	 *                             updated.
	 */
	public boolean update( UsersDAO user ) throws DataAccessException
	{
		logger.info( "update: Started." );
		// Create sql statement
		String sqlQ = "UPDATE " + TABLENAME + " SET " + USERNAMECOL + " = ?, " + PASSWORDCOL + " = ?, WHERE " + IDCOL
				+ " = ?";

		try
		{
			// update row with User in DB
			logger.info( "update: SQL querry starts running." );
			int rows = this.jdbcTemplate.update( sqlQ, user.getUsername(), user.getPassword(), user.getId() );
			if ( rows == 1 )
			{
				logger.info( "update: Updated user into the DB succesfly." );
				logger.info( "update: Returns true." );
				return true;
			}
			else
			{
				logger.warn( "update: Faild to update user into DB." );
				logger.info( "update: Returns false." );
				return false;
			}
		}
		catch ( DataAccessException e )
		{
			logger.warn( "update: DataAccessException occured." );
			throw e;
		}
		catch ( Exception ex )
		{
			// Print a Stack Trace if an exception occurs.
			logger.error( "update: Exception occured." );
			ex.printStackTrace();
		}
		logger.warn( "update: Faild to update user into DB." );
		logger.info( "update: Returns false." );
		return false;
	}
}
