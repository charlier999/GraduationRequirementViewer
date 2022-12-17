/**
 * 
 */
package com.gradview.exception;

/**
 * @author Charles Davis
 *
 */
public class UsernameTakenException extends Exception
{

	/**
	 * 
	 */
	public UsernameTakenException()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public UsernameTakenException( String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace )
	{
		super( message, cause, enableSuppression, writableStackTrace );
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public UsernameTakenException( String message, Throwable cause )
	{
		super( message, cause );
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public UsernameTakenException( String message )
	{
		super( message );
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public UsernameTakenException( Throwable cause )
	{
		super( cause );
		// TODO Auto-generated constructor stub
	}

}
