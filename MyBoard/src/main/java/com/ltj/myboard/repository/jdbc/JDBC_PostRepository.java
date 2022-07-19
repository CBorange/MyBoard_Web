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
import java.util.Optional;

public class JDBC_PostRepository implements PostRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public JDBC_PostRepository(DataSource dataSource){
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public Optional<Post> findPostByID(int postID) {
        String sql = "SELECT * FROM post WHERE ID = :id";

        MapSqlParameterSource namedParameter = new MapSqlParameterSource();
        namedParameter.addValue("id", postID);

        Optional<Post> ret = Optional.<Post>of((Post)jdbcTemplate.queryForObject(sql, namedParameter, new BeanPropertyRowMapper(Post.class)));
        return ret;
    }

    @Override
    public List<Post> findAllPostByBoardID(int boardID) {
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
