package com.ltj.myboard.repository.jdbc;

import com.ltj.myboard.domain.Comment;
import com.ltj.myboard.repository.CommentRepository;
import com.ltj.myboard.util.MyResourceLoader;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

public class JDBC_CommentRepository implements CommentRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final String  findCommentByPostID_SQL;

    public JDBC_CommentRepository(DataSource dataSource){
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

        String daoName = "JDBC_CommentRepository";
        findCommentByPostID_SQL = MyResourceLoader.loadProductionQuery(daoName, "findCommentByPostID.sql");
    }

    @Override
    public List<Comment> findCommentByPostID(int postID){
        MapSqlParameterSource namedParameter = new MapSqlParameterSource();
        namedParameter.addValue("postID", postID);

        // 쿼리 실행
        List<Comment> comments = jdbcTemplate.query(
                findCommentByPostID_SQL,
                namedParameter,
                new CommentRowMapper()
        );

        return comments;
    }

    @Override
    public List<Comment> findAllRootComment(int postID) {
        List<Comment> comments = findCommentByPostID(postID);

        // Root 댓글만 취합
        List<Comment> rootComments = comments.stream().filter(target -> {
            if(target.getParentCommentID() == 0)
                return true;
            return false;
        }).collect(Collectors.toList());

        // 자식 Comment 객체 맵핑
        for(Comment comment : rootComments){
            List<Comment> childComments = comments.stream().filter(target -> {
                if(target.getParentCommentID() == comment.getID())
                    return true;
                return false;
            }).collect(Collectors.toList());

            comment.addChildComment(childComments);
        }

        return rootComments;
    }

    public class CommentRowMapper implements RowMapper<Comment>{
        @Override
        public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
            Comment result = new Comment();
            result.setID(rs.getInt("ID"));
            result.setPostID(rs.getInt("PostID"));
            result.setParentCommentID(rs.getInt("ParentCommentID"));
            result.setWriterID(rs.getString("WriterID"));
            result.setContent(rs.getString("Content"));
            result.setGoodCount(rs.getInt("GoodCount"));
            result.setBadCount(rs.getInt("BadCount"));
            result.setCreatedDay(rs.getTimestamp("CreatedDay").toLocalDateTime());
            result.setModifyDay(rs.getTimestamp("ModifyDay").toLocalDateTime());
            Timestamp deleteDayTS = rs.getTimestamp("DeleteDay");
            if(deleteDayTS != null)
                result.setDeleteDay(deleteDayTS.toLocalDateTime());
            return result;
        }
    }
}
