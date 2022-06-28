package com.ltj.myboard.repository.jdbc;

import com.ltj.myboard.domain.Board;
import com.ltj.myboard.domain.Post;
import com.ltj.myboard.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class JDBC_PostRepository implements PostRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public JDBC_PostRepository(DataSource dataSource){
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public List<Post> findPostByBoardID(int boardID) {
        // 쿼리 실행
        String sql = "SELECT * FROM post WHERE BoardID = :boardID;";

        MapSqlParameterSource namedParameter = new MapSqlParameterSource();
        namedParameter.addValue("boardID", boardID);

        List<Post> postList = jdbcTemplate.query(
                sql,
                namedParameter,
                BeanPropertyRowMapper.newInstance(Post.class)
        );
        return postList;
    }

    @Override
    public List<Post> findPostByWriterID(String writerID) {
        // 쿼리 실행
        String sql = "SELECT * FROM post WHERE WriterID = :writerID;";

        MapSqlParameterSource namedParameter = new MapSqlParameterSource();
        namedParameter.addValue("writerID", writerID);

        List<Post> postList = jdbcTemplate.query(
                sql,
                namedParameter,
                BeanPropertyRowMapper.newInstance(Post.class)
        );
        return postList;
    }
}
