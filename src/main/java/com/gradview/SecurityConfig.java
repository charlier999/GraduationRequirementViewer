package com.gradview;

import java.util.Collection;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import com.gradview.data.dam.UserDAM;
import com.gradview.data.dao.UserDAO;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    
    /** 
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        logger.info("configure(HttpSecurity): Starting.");
        http
        .authorizeHttpRequests()
            //.antMatchers("/test").hasRole("ADMIN")
            .anyRequest().permitAll()
            .and()
        .formLogin()
            .loginPage("/login")
            .failureUrl("/login?failure")
            // .successForwardUrl("/home")
            .defaultSuccessUrl("/home", true)
            .permitAll()
            .and()
        .logout()
            .logoutUrl("/logout")
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
            .invalidateHttpSession(true)
            .logoutSuccessUrl("/home")
            .permitAll()
            .and()
        .csrf()
            .requireCsrfProtectionMatcher(new AntPathRequestMatcher("!/logout", HttpMethod.GET.toString()))
            .and();
        logger.info("configure(HttpSecurity): Ending.");
    }
}

/**
 * The custom {@link AuthenticationProvider} for authenticateing users
 */
@Component
class AdminAuthenticationProvider implements AuthenticationProvider 
{
    private static final Logger logger = LoggerFactory.getLogger(AdminAuthenticationProvider.class);

    @Autowired
    private UserDAO userDao;

    @Override
    public Authentication authenticate( Authentication authentication ) throws AuthenticationException
    {
        // Pull credtials from input
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        logger.info("authenticate: User `"+username+"` is attempting to authenticate.");
        
        // Authenticate credentials
        UserDAM user = this.userDao.autenticate(username, password);
        
        // If authenticated
        if(user != null)
        {
            logger.info("authenticate: User `"+username+"` is authenticated.");
            Collection<? extends GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ADMIN"));
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, authorities);
            return authToken;
        }
        else
        {
            logger.info("authenticate: User `"+username+"` is NOT authenticated.");
            throw new BadCredentialsException("Invalid username or password");
        }
}

    @Override
    public boolean supports( Class< ? > authentication )
    {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
