package com.shatteredrealmsonline.config;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

@Configuration
@PropertySource("application-prod.properties")
public class DataSourceConfig
{
    @Value("${MYSQL_HOST}")
    private String host;

    @Value("${MYSQL_PORT}")
    private String port;

    @Value("${MYSQL_DB}")
    private String db;

    @Value("${MYSQL_USER}")
    private String user;

    @Value("${MYSQL_PASS}")
    private String pass;

    @Bean
    public DataSource getDataSource()
    {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("com.mysql.jdbc.Driver");
        dataSourceBuilder.url(String.format("jdbc:mysql:%s:%s/%s", host, port, db));
        dataSourceBuilder.username(user);
        dataSourceBuilder.password(pass);
        return dataSourceBuilder.build();
    }
}
