package com.ltj.myboard.repository;

import com.ltj.myboard.domain.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class JDBC_PostRepository implements PostRepository{

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public JDBC_PostRepository(DataSource dataSource){
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public List<Post> findPostByBoardID(int boardID) {
        return null;
    }

    @Override
    public List<Post> findPostByWriterID(String writerID) {
        return null;
    }
}
