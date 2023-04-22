package com.gradview.data.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.InvalidResultSetAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.gradview.data.dam.UserDAM;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class UserDAO
{
    private static final Logger logger = LoggerFactory.getLogger( UserDAO.class );
	private static final String TABLENAME = "`users`";
	public static final String COL_ID = "id";
    public static final String COL_USERNAME = "username";
    public static final String COL_PASSWORD = "password";
    public static final String COL_ENABLED = "enabled";

    @Autowired
	private JdbcTemplate jdbcTemplate;

    /**
     * Authenticates the user creditials against the {@value #TABLENAME} table.
     * @param username The username to check against the {@value #TABLENAME} table.
     * @param password The password to check against the {@value #TABLENAME} table.
     * @return The authenticated {@link UserDAM} or null if not found or exception occurs.
     */
    public UserDAM autenticate(String username, String password)
    {
        logger.debug( "autenticate: Started." );
		// Create SQL query.
		String sqlQuery = "SELECT * FROM " + TABLENAME 
            + " WHERE `" + COL_USERNAME + "`='" + username
            + "' AND `" + COL_PASSWORD + "`='" + password+"'";
		// Create results list.
		List< UserDAM > users = new ArrayList<>();
		// Exception catch.
		try
		{
			// Run SQL Query.
			logger.debug( "autenticate: SQL querry started running." );
			SqlRowSet srs = jdbcTemplate.queryForRowSet( sqlQuery );
			// Create rowsFound check value.
			boolean rowsFound = false;
			// Loop through all resulting rows.
			logger.debug( "autenticate: Looping through the resulting row set." );
			while ( srs.next() )
			{
				rowsFound = true;
				// Add rows to output list.
				users.add( new UserDAM(
                    srs.getInt(COL_ID), srs.getString(COL_USERNAME), 
                    srs.getString(COL_PASSWORD), srs.getBoolean(COL_ENABLED)));
			}
            // If no users are found
			if ( !rowsFound )
			{
				logger.debug( "autenticate: No rows were found." );
				return null;
			}
            // If more then one user is found
            if(users.size() > 1)
            {
                logger.error("autenticate: Returning null.");
                return null;
            }
            logger.debug("autenticate: Returning user.");
            return users.get(0);
		}
		catch ( InvalidResultSetAccessException e )
		{
			logger.error( "autenticate: InvalidResultSetAccessException occured.", e );
			e.printStackTrace();
            logger.error("autenticate: Returning null.");
            return null;
		}
		catch ( DataAccessException e )
		{
			logger.error( "autenticate: DataAccessException occured.", e );
            e.printStackTrace();
            logger.error("autenticate: Returning null.");
			return null;
		}
		catch ( Exception ex )
		{
			logger.error( "autenticate: Exception occured.", ex );
			ex.printStackTrace();
            logger.error("autenticate: Returning null.");
            return null;
		}
    }
}