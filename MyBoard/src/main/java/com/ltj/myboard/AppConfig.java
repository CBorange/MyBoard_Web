package com.ltj.myboard;

import com.ltj.myboard.repository.BoardRepository;
import com.ltj.myboard.repository.jdbc.JDBC_BoardRepository;
import com.ltj.myboard.repository.jdbc.JDBC_PostRepository;
import com.ltj.myboard.repository.PostRepository;
import com.ltj.myboard.service.serviceinterface.BoardService;
import com.ltj.myboard.service.BoardServiceImpl;
import com.ltj.myboard.service.serviceinterface.PostService;
import com.ltj.myboard.service.PostServiceImpl;
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

    @Bean
    public BoardService boardService(){
        return new BoardServiceImpl(boardRepository());
    }

    @Bean
    public BoardRepository boardRepository(){
        return new JDBC_BoardRepository(dataSource());
    }

    @Bean
    public PostService postService(){
        return new PostServiceImpl(postRepository());
    }

    @Bean
    public PostRepository postRepository(){
        return new JDBC_PostRepository(dataSource());
    }
}
