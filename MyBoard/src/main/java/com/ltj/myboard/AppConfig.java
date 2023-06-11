package com.ltj.myboard;

import com.ltj.myboard.repository.*;
import com.ltj.myboard.repository.jdbc.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class AppConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource(){
        return DataSourceBuilder.create().build();
    }

    // Repository, 추후 JPA 사용할 수 있으므로 Configure에서 Bean 등록
    
    @Bean
    public BoardRepository boardRepository(){
        return new JDBC_BoardRepository(dataSource());
    }

}
