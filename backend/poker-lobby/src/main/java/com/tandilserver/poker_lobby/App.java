package com.tandilserver.poker_lobby;

import javax.servlet.Filter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tandilserver.poker_lobby.filters.CorsFilter;
import com.tandilserver.poker_lobby.filters.JWT.JWTFilter;
import com.tandilserver.poker_lobby.filters.JWT.JWTValidFilter;

@SpringBootApplication
@EntityScan("com.tandilserver.poker_lobby.dataBase.domain")
@Configuration("com.tandilserver.poker_lobby.dataBase.JDBConfig")
public class App extends SpringBootServletInitializer
{
    public static void main( String[] args )
    {
    	SpringApplication.run(App.class, args);
    }
    
    @Bean
	public FilterRegistrationBean<Filter> registerCorsFilter() {
		FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<Filter>();
		registration.setFilter(corsFilter());
		registration.addUrlPatterns("/*");
		// registration.addInitParameter("paramName", "paramValue");
		registration.setName("corsFilter");
		registration.setOrder(1);
		return registration;
	}
    
    public Filter corsFilter() {
		return new CorsFilter();
	}
    
    @Bean
	public FilterRegistrationBean<Filter> registerJwtFilter() {
		FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<Filter>();
		registration.setFilter(jwtFilter());
		addPatternsJWT(registration);
		registration.setName("jwtFilter");
		registration.setOrder(2);
		return registration;
	}
    
    public Filter jwtFilter() {
    	return new JWTFilter();
    }
    
    @Bean
	public FilterRegistrationBean<Filter> registerJwtValidFilter() {
		FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<Filter>();
		registration.setFilter(jwtValidFilter());
		addPatternsJWT(registration);
		registration.setName("jwtFilter");
		registration.setOrder(3);
		return registration;
	}
    
    public Filter jwtValidFilter() {
    	return new JWTValidFilter();
    }
    
    public static void addPatternsJWT(FilterRegistrationBean<Filter> registration) {
    	registration.addUrlPatterns(
    		"/accountRest/*",
    		"/roomsRest/*"
    	);
    }
    
}
