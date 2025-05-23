package com.africahr.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class DatabaseConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);
    
    @Value("${DATABASE_URL}")
    private String databaseUrl;
    
    @Bean
    public DataSource dataSource() throws URISyntaxException {
        logger.info("Configuring datasource from DATABASE_URL");
        
        // Handle URLs starting with postgresql:// rather than postgres://
        String updatedUrl = databaseUrl;
        if (updatedUrl.startsWith("postgresql://")) {
            updatedUrl = "postgres" + updatedUrl.substring("postgresql".length());
        }
        
        URI dbUri = new URI(updatedUrl);
        
        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        
        // Parse host and port separately to avoid -1 port issue
        String host = dbUri.getHost();
        int port = dbUri.getPort() == -1 ? 5432 : dbUri.getPort(); // Use default PostgreSQL port if not specified
        String path = dbUri.getPath();
        
        String jdbcUrl = String.format("jdbc:postgresql://%s:%d%s", host, port, path);
        
        logger.info("JDBC URL: {}", jdbcUrl);
        logger.info("Username: {}", username);
        
        return DataSourceBuilder.create()
                .url(jdbcUrl)
                .username(username)
                .password(password)
                .build();
    }
}