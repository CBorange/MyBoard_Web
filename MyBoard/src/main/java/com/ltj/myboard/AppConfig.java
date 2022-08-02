package com.ltj.myboard;

import com.ltj.myboard.repository.BoardRepository;
import com.ltj.myboard.repository.CommentRepository;
import com.ltj.myboard.repository.FilteredPostRepository;
import com.ltj.myboard.repository.jdbc.JDBC_BoardRepository;
import com.ltj.myboard.repository.jdbc.JDBC_CommentRepository;
import com.ltj.myboard.repository.jdbc.JDBC_FilteredPostRepository;
import com.ltj.myboard.repository.jdbc.JDBC_PostRepository;
import com.ltj.myboard.repository.PostRepository;
import com.ltj.myboard.service.BoardService;
import com.ltj.myboard.service.CommentService;
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

    @Bean
    public BoardService boardService(){
        return new BoardService(boardRepository());
    }

    @Bean
    public PostService postService(){
        return new PostService(postRepository(), filteredPostRepository());
    }

    @Bean
    public CommentService commentService(){
        return new CommentService(commentRepository());
    }

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
    public CommentRepository commentRepository(){
        return new JDBC_CommentRepository(dataSource());
    }
}
