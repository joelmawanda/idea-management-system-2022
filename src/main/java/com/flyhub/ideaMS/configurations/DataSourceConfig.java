package com.flyhub.ideaMS.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * this class creates a "javax.sql.DataSource" bean and exposes it to the
 * application context
 *
 * @author Benjamin E Ndugga
 */
@Configuration
public class DataSourceConfig {

    @Autowired
    private Environment env;

    @Bean
    @Profile(value = {"benjie-dev", "joel-dev", "jean-dev", "test", "mysql-dev"})
    public DataSource initialiseDataSource() {

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
        dataSource.setUrl(env.getProperty("spring.datasource.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.password"));

        return dataSource;
    }

}
