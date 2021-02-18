package com.dimkas.mars.marsrealestate.configuration;

import javax.sql.DataSource;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;

@Configuration
public class Config {

    @Value("${app.sql.user-query}")
    private String usersByUsernameQuery;
    @Value("${app.sql.authorities-query}")
    private String authoritiesByUsernameQuery;

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
        return mapper;
    }

    @Bean
    UserDetailsService userDetailsService(DataSource dataSource) {

        JdbcDaoImpl userService =  new JdbcDaoImpl();
        userService.setDataSource(dataSource);
        userService.setUsersByUsernameQuery(this.usersByUsernameQuery);
        userService.setAuthoritiesByUsernameQuery(this.authoritiesByUsernameQuery);
        return userService;
    }

}
