package com.ltj.myboard;

import com.ltj.myboard.repository.*;
import com.ltj.myboard.repository.jdbc.*;
import com.ltj.myboard.service.BoardService;
import com.ltj.myboard.service.CommentService;
import com.ltj.myboard.service.FtpService;
import com.ltj.myboard.service.PostService;
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

    @Bean
    public PostRepository postRepository(){
        return new JDBC_PostRepository(dataSource());
    }

    @Bean
    public FilteredPostRepository filteredPostRepository(){
        return new JDBC_FilteredPostRepository(dataSource());
    }

    @Bean
    public PostFileRepository postFileRepository(){
        return new JDBC_PostFileRepository((dataSource()));
    }

    @Bean
    public CommentRepository commentRepository(){
        return new JDBC_CommentRepository(dataSource());
    }
}
