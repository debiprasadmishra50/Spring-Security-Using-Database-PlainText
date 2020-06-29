package demo.spring.config;

import java.beans.PropertyVetoException;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.mchange.v2.c3p0.ComboPooledDataSource;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "demo.spring")
@PropertySource("classpath:persistance-mysql.properties")
public class ApplicationConfig {
	
	// set variable to hold the data read from properties file
	@Autowired
	private Environment env;
	
	// setup a Logger for diagnostics
	private Logger logger = Logger.getLogger(getClass().getName());
	
	// define a bean for ViewResolver
	@Bean
	public ViewResolver viewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		
		viewResolver.setPrefix("/WEB-INF/view/");
		viewResolver.setSuffix(".jsp");
		
		return viewResolver;
	}
	
	// define a Bean for our security data-source
	@Bean
	public DataSource securityDataSource() {
		
		// create a connection pool
		ComboPooledDataSource securityDataSource = new ComboPooledDataSource();
		
		// set the JDBC Driver class
		try {
			securityDataSource.setDriverClass(env.getProperty("jdbc.driver"));
		} catch (PropertyVetoException e) {
			throw new RuntimeException(e);
		}
		
		// log the connection properties
		logger.info(">>> jdbc.url="+env.getProperty("jdbc.url"));
		logger.info(">>> jdbc.user="+env.getProperty("jdbc.user"));
		
		// set the Database connection properties
		securityDataSource.setJdbcUrl(env.getProperty("jdbc.url"));
		securityDataSource.setUser(env.getProperty("jdbc.user"));
		securityDataSource.setPassword(env.getProperty("jdbc.password"));
		
		// set the connection pool properties
		securityDataSource.setInitialPoolSize(getIntProperty("connection.pool.initialPoolSize"));
		securityDataSource.setMinPoolSize(getIntProperty("connection.pool.minPoolSize"));
		securityDataSource.setMaxPoolSize(getIntProperty("connection.pool.maxPoolSize"));
		securityDataSource.setMaxIdleTime(getIntProperty("connection.pool.maxIdleTime"));

		return securityDataSource;
	}
	
	// need a helper method
	// reading a environment property and convert it to integer
	private int getIntProperty(String propName) {
		
		String propValue = env.getProperty(propName);
		
		// now convert it to int
		int intPropVal = Integer.parseInt(propValue);
		
		return intPropVal;
	}
}













