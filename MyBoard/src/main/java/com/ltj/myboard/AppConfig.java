package com.ltj.myboard;

import com.ltj.myboard.repository.BoardRepository;
import com.ltj.myboard.repository.JDBC_BoardRepository;
import com.ltj.myboard.repository.JDBC_PostRepository;
import com.ltj.myboard.repository.PostRepository;
import com.ltj.myboard.service.BoardService;
import com.ltj.myboard.service.BoardServiceImpl;
import com.ltj.myboard.service.PostService;
import com.ltj.myboard.service.PostServiceImpl;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

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
