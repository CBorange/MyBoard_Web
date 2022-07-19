package com.ltj.myboard.repository.jdbc;

import com.ltj.myboard.domain.Post;
import com.ltj.myboard.dto.FilteredPost;
import com.ltj.myboard.repository.FilteredPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class JDBC_FilteredPostRepository implements FilteredPostRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public JDBC_FilteredPostRepository(DataSource dataSource){
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public List<FilteredPost> findPost_UseSearch_Title(int boardID, String condition_title, String sortTargetColumn, String orderByMethod) {
        // 쿼리 실행
        String sql = "SELECT @ROWNUM:=@ROWNUM+1 as OrderedPostNo, p.*\n" +
                     "FROM post p, (SELECT @ROWNUM:=0) R\n" +
                     "WHERE Title LIKE '%:condition%'\n" +
                     "ORDER BY :sortColumn :orderByMethod;";

        MapSqlParameterSource namedParameter = new MapSqlParameterSource();
        namedParameter.addValue("condition", condition_title);
        namedParameter.addValue("sortColumn", sortTargetColumn);
        namedParameter.addValue("orderByMethod", orderByMethod);

        

        return null;
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
}
