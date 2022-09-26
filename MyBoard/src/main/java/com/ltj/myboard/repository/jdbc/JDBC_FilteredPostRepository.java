package com.ltj.myboard.repository.jdbc;

import com.ltj.myboard.domain.Post;
import com.ltj.myboard.dto.board.FilteredPost;
import com.ltj.myboard.repository.FilteredPostRepository;
import com.ltj.myboard.util.MyResourceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class JDBC_FilteredPostRepository implements FilteredPostRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final String findPost_UseSearch_Title_SQL;

    @Autowired
    public JDBC_FilteredPostRepository(DataSource dataSource){
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

        String daoName = "JDBC_FilteredPostRepository";
        findPost_UseSearch_Title_SQL = MyResourceLoader.loadProductionQuery(daoName, "findPost_UseSearch_Title.sql");
    }

    @Override
    public List<FilteredPost> findPost_UseSearch_Title(int boardID, String condition_title, String sortTargetColumn, String orderByMethod) {
        // 쿼리 실행

        MapSqlParameterSource namedParameter = new MapSqlParameterSource();
        namedParameter.addValue("boardID", boardID);
        namedParameter.addValue("condition", "%" + condition_title + "%");

        List<FilteredPost> filteredPostList = jdbcTemplate.query(
          findPost_UseSearch_Title_SQL,
          namedParameter,
          new FilteredObjectRowMapper()
        );

        return filteredPostList;
    }



    @Override
    public List<FilteredPost> findPost_UseSearch_Content(int boardID, String condition_content, String sortTargetColumn, String orderByMethod) {
        return null;
    }

    @Override
    public List<FilteredPost> findPost_UseSearch_Comment(int boardID, String condition_comment, String sortTargetColumn, String orderByMethod) {
        return null;
    }

    @Override
    public List<FilteredPost> findPost_UseSearch_Nickname(int boardID, String condition_nickname, String sortTargetColumn, String orderByMethod) {
        return null;
    }

    public class FilteredObjectRowMapper implements RowMapper<FilteredPost>{

        @Override
        public FilteredPost mapRow(ResultSet rs, int rowNum) throws SQLException {
            FilteredPost resultPost = new FilteredPost();
            resultPost.setOrderedPostNo(rs.getInt("OrderedPostNo"));
            resultPost.setCommentCount(rs.getInt("CommentCount"));

            Post postData = (new BeanPropertyRowMapper<>(Post.class)).mapRow(rs,rowNum);
            resultPost.setPostData(postData);

            return resultPost;
        }
    }
}