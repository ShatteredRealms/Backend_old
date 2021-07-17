package com.shatteredrealmsonline;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.DatabaseDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.support.DatabaseStartupValidator;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.stream.Stream;

@SpringBootApplication
public class ShatteredRealmsApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShatteredRealmsApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ShatteredRealmsApplication.class, args);
    }

    // See: https://deinum.biz/2020-06-30-Wait-for-database-startup/
    @Bean
    public DatabaseStartupValidator databaseStartupValidator(DataSource dataSource) {
        var dsv = new DatabaseStartupValidator();
        dsv.setDataSource(dataSource);
        dsv.setValidationQuery(DatabaseDriver.MYSQL.getValidationQuery());
        return dsv;
    }

    @Bean
    public static BeanFactoryPostProcessor dependsOnPostProcessor() {
        return bf -> {
            addDepends(bf, EntityManagerFactory.class);
        };
    }

    private static void addDepends(ConfigurableListableBeanFactory bf, Class<?> c) {
        String[] jpa = bf.getBeanNamesForType(c);
        Stream.of(jpa)
                .map(bf::getBeanDefinition)
                .forEach(it -> it.setDependsOn("databaseStartupValidator"));
    }
}
