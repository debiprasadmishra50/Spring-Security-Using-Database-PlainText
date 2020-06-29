package demo.spring.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

	// Add a reference to our security data source
	@Autowired
	private DataSource securityDataSource;
		
	@Override 
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		// use JDBC authentication ... no hard-coded user values
		auth.jdbcAuthentication().dataSource(securityDataSource);
	}

	@Override // configure security of Web Paths in application, Login, Logout etc...
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()	// Restrict access based on HttpServletRequest
		.antMatchers("/").hasRole("EMPLOYEE")
		.antMatchers("/leaders/**").hasRole("MANAGER")
		.antMatchers("/systems/**").hasRole("ADMIN")
		.and()
			.formLogin() // Customize the login process
				.loginPage("/myLoginPage") 	// show our custom form at the request mapping
				.loginProcessingUrl("/authenticateTheUser") 	// Login form should POST data to this URL for processing
				.permitAll() // Allow everyone to see the login page
			.and()
			.logout().permitAll() // logout
			.and()
				.exceptionHandling()
				.accessDeniedPage("/access-denied");
	}	

}

















