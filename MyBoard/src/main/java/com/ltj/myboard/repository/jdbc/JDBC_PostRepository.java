package com.ltj.myboard.repository.jdbc;

import com.ltj.myboard.domain.Board;
import com.ltj.myboard.domain.Post;
import com.ltj.myboard.dto.post.SubmitPostData;
import com.ltj.myboard.repository.PostRepository;
import com.ltj.myboard.util.MyResourceLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Slf4j
public class JDBC_PostRepository implements PostRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final String findPostByID_SQL;
    private final String findAllPostByBoardID_SQL;
    private final String findPostByWriterID_SQL;
    private final String insertPost_SQL;
    private final String updatePost_SQL;
    private final String deletePost_SQL;

    @Autowired
    public JDBC_PostRepository(DataSource dataSource){
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

        // 쿼리 파일 Load
        String daoName = "JDBC_PostRepository";
        findPostByID_SQL = MyResourceLoader.loadProductionQuery(daoName, "findPostByID.sql");
        findAllPostByBoardID_SQL = MyResourceLoader.loadProductionQuery(daoName, "findAllPostByBoardID.sql");
        findPostByWriterID_SQL = MyResourceLoader.loadProductionQuery(daoName, "findPostByWriterID.sql");
        insertPost_SQL = MyResourceLoader.loadProductionQuery(daoName, "insertPost.sql");
        updatePost_SQL = MyResourceLoader.loadProductionQuery(daoName, "updatePost.sql");
        deletePost_SQL = MyResourceLoader.loadProductionQuery(daoName, "deletePost.sql");
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
        MapSqlParameterSource namedParameter = new MapSqlParameterSource();
        namedParameter.addValue("writerID", writerID);

        List<Post> postList = jdbcTemplate.query(
                findPostByWriterID_SQL,
                namedParameter,
                BeanPropertyRowMapper.newInstance(Post.class)
        );
        return postList;
    }



    @Override
    public int insertPost(String title, String content, int boardID, String writerID) {
        MapSqlParameterSource namedParameter = new MapSqlParameterSource();
        namedParameter.addValue("boardID", boardID);
        namedParameter.addValue("writerID", writerID);
        namedParameter.addValue("title", title);
        namedParameter.addValue("content", content);

        KeyHolder idKeyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(insertPost_SQL, namedParameter, idKeyHolder, new String[]{"ID"});
        Number generatedID = idKeyHolder.getKey();
        return generatedID.intValue();
    }
    @Override
    public int updatePost(String title, String content, int postID, String writerID){
        MapSqlParameterSource namedParameter = new MapSqlParameterSource();
        namedParameter.addValue("title", title);
        namedParameter.addValue("content", content);
        namedParameter.addValue("writerID", writerID);
        namedParameter.addValue("postID", postID);

        int updateCount = jdbcTemplate.update(updatePost_SQL, namedParameter);
        return updateCount;
    }
    @Override
    public int deletePost(int postID){
        MapSqlParameterSource namedParameter = new MapSqlParameterSource();
        namedParameter.addValue("postID", postID);

        int deleteCount = jdbcTemplate.update(deletePost_SQL, namedParameter);
        return deleteCount;
    }
}
