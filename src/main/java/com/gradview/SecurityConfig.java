/**
 * 
 */
package com.gradview;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import com.gradview.data.dts.UserDTS;

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

	@Autowired
	private DataSource dataSource;

	@Bean
	public PasswordEncoder passwordEncoder()
	{
		// return new BCryptPasswordEncoder();
		return new PasswordEnconderTest();
	}

	@Override
	protected void configure( HttpSecurity http ) throws Exception
	{
		logger.info( "HttpSecurity configuration is started." );
		http.csrf().disable().httpBasic().and().authorizeRequests().antMatchers( "/login", "/registration/**", "/test" )
				.permitAll().anyRequest().authenticated().and().formLogin().loginPage( "/login" )
				.usernameParameter( "username" ).passwordParameter( "password" ).permitAll()
				.defaultSuccessUrl( "/home", true ).and().logout().logoutUrl( "/logout" ).invalidateHttpSession( true )
				.clearAuthentication( true ).permitAll().logoutSuccessUrl( "/login" );
		logger.info( "HttpSecurity configuration has finished." );
	}

	// @Autowired
	public void configure( AuthenticationManagerBuilder auth ) throws Exception
	{
		logger.info( "AuthenticationManagerBuilder configuration is started." );
		auth.jdbcAuthentication().dataSource( this.dataSource )// .passwordEncoder( this.passwordEncoder() )
				.usersByUsernameQuery( "SELECT username, password, enabled FROM users WHERE username = ?" )
				.authoritiesByUsernameQuery( "SELECT users.username, ur.name " + "FROM users "
						+ "INNER JOIN `user-assigned-role` AS uar " + "ON users.id = uar.userID "
						+ "INNER JOIN `user-roles` AS ur " + "ON uar.roleID = ur.id " + "WHERE users.username = ?" ); // This
																														// works
																														// now
		logger.info( "AuthenticationManagerBuilder configuration has finished." );
	}

}

class PasswordEnconderTest implements PasswordEncoder
{
	@Override
	public String encode( CharSequence charSequence )
	{
		return charSequence.toString();
	}

	@Override
	public boolean matches( CharSequence charSequence, String s )
	{
		return charSequence.toString().equals( s );
	}
}
