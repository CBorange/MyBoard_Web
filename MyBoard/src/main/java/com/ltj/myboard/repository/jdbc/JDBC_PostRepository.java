package com.ltj.myboard.repository.jdbc;

import com.ltj.myboard.domain.Board;
import com.ltj.myboard.domain.Post;
import com.ltj.myboard.repository.PostRepository;
import com.ltj.myboard.util.MyResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public class JDBC_PostRepository implements PostRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final String findPostByID_SQL;
    private final String findAllPostByBoardID_SQL;
    private final String findPostByWriterID_SQL;

    @Autowired
    public JDBC_PostRepository(DataSource dataSource){
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

        String daoName = "JDBC_PostRepository";
        findPostByID_SQL = MyResourceLoader.loadProductionQuery(daoName, "findPostByID.sql");
        findAllPostByBoardID_SQL = MyResourceLoader.loadProductionQuery(daoName, "findAllPostByBoardID.sql");
        findPostByWriterID_SQL = MyResourceLoader.loadProductionQuery(daoName, "findPostByWriterID.sql");
    }

    @Override
    public Optional<Post> findPostByID(int postID) {
        MapSqlParameterSource namedParameter = new MapSqlParameterSource();
        namedParameter.addValue("id", postID);

        Optional<Post> ret = Optional.<Post>of((Post)jdbcTemplate.queryForObject(findPostByID_SQL, namedParameter, new BeanPropertyRowMapper(Post.class)));
        return ret;
    }

    @Override
    public List<Post> findAllPostByBoardID(int boardID) {
        // 쿼리 실행
        MapSqlParameterSource namedParameter = new MapSqlParameterSource();
        namedParameter.addValue("boardID", boardID);

        List<Post> postList = jdbcTemplate.query(
                findAllPostByBoardID_SQL,
                namedParameter,
                BeanPropertyRowMapper.newInstance(Post.class)
        );
        return postList;
    }

    @Override
    public List<Post> findPostByWriterID(String writerID) {
        // 쿼리 실행
        MapSqlParameterSource namedParameter = new MapSqlParameterSource();
        namedParameter.addValue("writerID", writerID);

        List<Post> postList = jdbcTemplate.query(
                findPostByWriterID_SQL,
                namedParameter,
                BeanPropertyRowMapper.newInstance(Post.class)
        );
        return postList;
    }
}
