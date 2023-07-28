package com.zapp.marketapp.config;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@Setter
@ConfigurationProperties(prefix = "db")
public class DbConnection {

    private String url;
    private String username;
    private String password;
    private String driver;

    @Bean
    public DataSource dataSource() {
        DataSourceBuilder<?> config = DataSourceBuilder.create();
        config.url(url);
        config.username(username);
        config.password(password);
        config.driverClassName(driver);
        return config.build();
    }
}
