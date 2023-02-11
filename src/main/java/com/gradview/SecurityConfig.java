/**
 * 
 */
package com.gradview;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


/**
 * The Security configuration of the spring boot instance
 * 
 * @author Charles Davis
 */
@SuppressWarnings( "deprecation" )
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
	private static final Logger logger = LoggerFactory.getLogger( SecurityConfig.class );

	@Override
	protected void configure( HttpSecurity http ) throws Exception
	{
		logger.info( "HttpSecurity configuration is started." );
		http.authorizeRequests().anyRequest().permitAll();
		logger.info( "HttpSecurity configuration has finished." );
	}
}
