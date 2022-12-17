package com.gradview.exception;

/**
 * No Rows were found 
 * @author Charles Davis
 *
 */
public class NoRowsFoundException extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public NoRowsFoundException()
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
	public NoRowsFoundException( String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace )
	{
		super( message, cause, enableSuppression, writableStackTrace );
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public NoRowsFoundException( String message, Throwable cause )
	{
		super( message, cause );
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public NoRowsFoundException( String message )
	{
		super( message );
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public NoRowsFoundException( Throwable cause )
	{
		super( cause );
		// TODO Auto-generated constructor stub
	}

	
}
