package com.ltj.myboard.repository.jdbc;
import com.ltj.myboard.domain.PostFile;
import com.ltj.myboard.repository.PostFileRepository;
import com.ltj.myboard.util.MyResourceLoader;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.util.List;

public class JDBC_PostFileRepository implements PostFileRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final String findPostFilesByPostID_SQL;
    private final String insertPostFile_SQL;

    public JDBC_PostFileRepository(DataSource dataSource){
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

        String daoName = "JDBC_PostFileRepository";
        findPostFilesByPostID_SQL = MyResourceLoader.loadProductionQuery(daoName, "findPostFilesByPostID.sql");
        insertPostFile_SQL = MyResourceLoader.loadProductionQuery(daoName, "insertPostFile.sql");
    }

    @Override
    public int insertPostFile(int postID, String fileID, String fileName) {
        // 쿼리실행
        MapSqlParameterSource namedParameter = new MapSqlParameterSource();
        namedParameter.addValue("postID", postID);
        namedParameter.addValue("fileID", fileID);
        namedParameter.addValue("fileName", fileName);

        KeyHolder idKeyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(insertPostFile_SQL, namedParameter, idKeyHolder, new String[]{"ID"});
        Number generatedID = idKeyHolder.getKey();
        return generatedID.intValue();
    }

    @Override
    public List<PostFile> findPostFilesByPostID(int postID) {
        // 쿼리 실행
        MapSqlParameterSource namedParameter = new MapSqlParameterSource();
        namedParameter.addValue("postID", postID);

        List<PostFile> postFiles = jdbcTemplate.query(
                findPostFilesByPostID_SQL,
                namedParameter,
                BeanPropertyRowMapper.newInstance(PostFile.class)
        );
        return postFiles;
    }
}
