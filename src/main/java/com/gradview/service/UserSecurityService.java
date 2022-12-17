/**
 * 
 */
package com.gradview.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.gradview.data.dao.UserAssignedRoleDAO;
import com.gradview.data.dao.UserRolesDAO;
import com.gradview.data.dao.UsersDAO;
import com.gradview.data.dts.PermissionsDTS;
import com.gradview.data.dts.RolePermissionsDTS;
import com.gradview.data.dts.UserAssignedRoleDTS;
import com.gradview.data.dts.UserDTS;
import com.gradview.data.dts.UserRolesDTS;
import com.gradview.exception.NoRowsFoundException;
import com.gradview.exception.TooManyRowsException;
import com.gradview.exception.UserNotFoundException;
import com.gradview.exception.UsernameTakenException;
import com.gradview.model.UserInfo;

/**
 * @author Charles Davis
 */
@Service
public class UserSecurityService
{
	private static final Logger logger = LoggerFactory.getLogger( UserSecurityService.class );

	@Autowired
	private UserDTS userDTS;

	@Autowired
	private UserAssignedRoleDTS userAssignedRoleDTS;

	@Autowired
	private UserRolesDTS userRolesDTS;

	@Autowired
	private PermissionsDTS permissionsDTS;

	@Autowired
	private RolePermissionsDTS rolePermissionsDTS;

	/**
	 * Checks whether a username is taken by another User.
	 * 
	 * @param username - The username to check for existing User for.
	 * @return - True: Username is taken. False: Username is NOT taken.
	 * @throws DataAccessException See message.
	 */
	private boolean isUsernameTaken( String username ) throws DataAccessException
	{
		logger.info( "isUsernameTaken: Starting." );
		try
		{
			int userID = this.getUserIDByUsername( username );
			if ( userID > 0 )
			{
				logger.info( "isUsernameTaken: Username was taken." );
				return true;
			}
			else
			{
				logger.info( "isUsernameTaken: Username is available." );
				return false;
			}
		}
		catch ( DataAccessException e )
		{
			throw e;
		}
		catch ( TooManyRowsException e )
		{
			logger.info( "isUsernameTaken: Username was taken." );
			return true;
		}
		catch ( UserNotFoundException e )
		{
			logger.info( "isUsernameTaken: Username is available." );
			return false;
		}
	}

	/**
	 * Gets the user's info from their username.
	 * 
	 * @param username - The username of the user.
	 * @return {@link UserInfo}
	 * @throws TooManyRowsException
	 * @throws UserNotFoundException
	 * @throws NoRowsFoundException
	 */
	public UserInfo getUserByUsername( String username )
			throws TooManyRowsException, UserNotFoundException, NoRowsFoundException
	{
		try
		{
			UserInfo output = new UserInfo( -1, username, "null", null, -1, null, null );
			output.setId( this.getUserIDByUsername( username ) );
			output.setUserRoles( this.getUserAssignedRoleByUserID( output.getId() ) );
		}
		catch ( DataAccessException e )
		{
			logger.error( "getUserByUsername: DataAccessException: Message:" + e.getMostSpecificCause().toString() );
			throw e;
		}
		catch ( UserNotFoundException e )
		{
			logger.warn( "getUserByUsername: No users were found." );
			throw new UserNotFoundException();
		}
		catch ( NoRowsFoundException e )
		{
			logger.warn( "getUserByUsername: No users were found." );
			throw new NoRowsFoundException();
		}
		return null;

	}

	/**
	 * Gets the user's roles by their ID number.
	 * 
	 * @param userID - The user's ID number.
	 * @return List {@link UserAssignedRoleDAO}
	 * @throws NoRowsFoundException
	 * @throws DataAccessException
	 */
	private List< UserAssignedRoleDAO > getUserAssignedRoleByUserID( int userID )
			throws NoRowsFoundException, DataAccessException
	{

		try
		{
			return userAssignedRoleDTS.search( UserAssignedRoleDTS.USERIDCOL, Integer.toString( userID ) );
		}
		catch ( DataAccessException e )
		{
			logger.error( "getUserRolesByUserID: DataAccessException: Message:" + e.getMostSpecificCause().toString() );
			throw e;
		}
		catch ( NoRowsFoundException e )
		{
			logger.warn( "getUserRolesByUserID: No users were found." );
			throw new NoRowsFoundException();
		}

	}

	/**
	 * Gets the user's ID number by their username.
	 * 
	 * @param username - The username to search for.
	 * @return The userID of the username.
	 * @throws TooManyRowsException
	 * @throws UserNotFoundException
	 */
	private int getUserIDByUsername( String username ) throws TooManyRowsException, UserNotFoundException
	{
		logger.info( "getUserIDByUsername: Starting." );
		try
		{
			logger.info( "getUserIDByUsername: searching for users by username." );
			List< UsersDAO > users = this.userDTS.search( UserDTS.USERNAMECOL, username );
			logger.info( "getUserIDByUsername: search compleate." );
			if ( users.size() > 1 )
			{
				logger.error( "getUserIDByUsername: More then one user was retreived." );
				throw new TooManyRowsException();
			}
			return users.get( 0 ).getId();
		}
		catch ( DataAccessException e )
		{
			logger.error( "getUserIDByUsername: DataAccessException: Message:" + e.getMostSpecificCause().toString() );
			throw e;
		}
		catch ( NoRowsFoundException e )
		{
			logger.warn( "getUserIDByUsername: No users were found." );
			throw new UserNotFoundException();
		}
	}

	/**
	 * Gets the current user by their session. REQUIRES httpResponse from the method
	 * calling it.
	 * 
	 * @param httpResponse ({@link HttpServletResponse}) - REQUIRED FOR REDIRECT.
	 * @return {@link User}
	 * @throws DataAccessException
	 * @throws IOException
	 */
	public UserInfo getCurrentSessionUser( HttpServletResponse httpResponse ) throws DataAccessException, IOException
	{
		logger.info( "getCurrentSessionUser: Starting." );
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		try
		{
			// Get the user from session authentication.
			logger.info( ": Retreiveing logged in user's details." );
			String name = auth.getName();
			// If name equals the name given by spring for a user not logged in.
			if ( name.equals( "anonymousUser" ) ) httpResponse.sendRedirect( "/login" );
			return this.getUserByUsername( name );
		}
		catch ( TooManyRowsException e )
		{
			logger.info( "getCurrentSessionUser: More then expected rows." );
			logger.info( "getCurrentSessionUser: Redirecting to '/login'." );
			httpResponse.sendRedirect( "/login" );

		}
		catch ( UserNotFoundException e )
		{
			logger.info( "getCurrentSessionUser: User was not found." );
			logger.info( "getCurrentSessionUser: Redirecting to '/login'." );
			httpResponse.sendRedirect( "/login" );
		}
		catch ( DataAccessException e )
		{
			logger.warn( "getCurrentSessionUser: DataAccessException ocured." );
			logger.info( "getCurrentSessionUser: Redirecting to '/login'." );
			httpResponse.sendRedirect( "/login" );
		}
		catch ( NoRowsFoundException e )
		{
			logger.warn( "getUserIDByUsername: No users were found." );
			logger.info( "getCurrentSessionUser: Redirecting to '/login'." );
			httpResponse.sendRedirect( "/login" );
		}
		return null;
	}

	/**
	 * Inserts a {@link User} in the user table and assigns them the 'User' role.
	 * 
	 * @param user ({@link User}) - The user to insert into the user table.
	 * @return (boolean) - True: {@link User} was inserted with its {@link Role}.
	 *         False: {@link User} was NOT inserted with its {@link Role}.
	 * @throws UsernameTakenException The username of the {@link User} was already
	 *                                taken by another {@link User}.
	 * @throws EmailTakenException    The email of the {@link User} was already
	 *                                taken by another {@link User}.
	 * @throws RoleNotFoundException  The 'User' {@link Role} was not found.
	 * @throws DataAccessException    See message.
	 */
	public boolean addUser( UsersDAO user ) throws UsernameTakenException, DataAccessException
	{
		logger.info( "addUser: Has started." );
		boolean success = true;
		int userRoleID = -1;
		try
		{
			if ( this.isUsernameTaken( user.getUsername() ) )
			{
				logger.info( "addUser: Username was taken." );
				throw new UsernameTakenException();
			}
			// Add User to DB.
			logger.info( "addUser: Creating new user." );
			success = userDTS.create( user );
			// If user wasn't added to the database.
			if ( !success )
			{
				logger.error( "addUser: User failed to be created." );
				return false;
			}
			// Get the userID from newly created user.
			try
			{
				logger.info( "addUser: Searches for newly created user." );
				user.setId( this.getUserIDByUsername( user.getUsername() ) );
			}
			catch ( TooManyRowsException e )
			{
				logger.error( "addUser: Newly created user was duplicated" );
				// Use first user in list of users.
				try
				{
					user = userDTS.search( UserDTS.USERNAMECOL, user.getUsername() ).get( 0 );
				}
				catch ( NoRowsFoundException e1 )
				{
					// Do nothing because there is already more then one user found already.
				}
			}
			catch ( UserNotFoundException e )
			{
				logger.error( "addUser: Newly created user was not found." );
				return false;
			}
			// Assign user role to user.
			userAssignedRoleDTS.create( new UserAssignedRoleDAO(user.getId(), 1) );
			
			return true;

		}
		catch ( DataAccessException e )
		{
			logger.error( "addUser: DataAccessException: Message:" + e.getMostSpecificCause().toString() );
			throw e;
		}
	}
}
