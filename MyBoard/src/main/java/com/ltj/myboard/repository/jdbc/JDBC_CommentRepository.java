package com.ltj.myboard.repository.jdbc;

import com.ltj.myboard.domain.Comment;
import com.ltj.myboard.dto.post.OrderedComment;
import com.ltj.myboard.repository.CommentRepository;
import com.ltj.myboard.util.MyResourceLoader;
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
        findCommentByPostID_SQL = MyResourceLoader.loadProductionQuery(daoName, "findOrderedCommentByPostID.sql");
    }

    @Override
    public List<OrderedComment> findOrderedCommentByPostID(int postID){
        MapSqlParameterSource namedParameter = new MapSqlParameterSource();
        namedParameter.addValue("postID", postID);

        // 쿼리 실행
        List<OrderedComment> comments = jdbcTemplate.query(
                findCommentByPostID_SQL,
                namedParameter,
                new OrderedCommentRowMapper()
        );

        return comments;
    }

    @Override
    public List<OrderedComment> findOrderedRootComment(int postID) {
        List<OrderedComment> comments = findOrderedCommentByPostID(postID);

        // Root 댓글만 취합
        List<OrderedComment> rootComments = comments.stream().filter(target -> {
            if(target.getCommentData().getParentCommentID() == 0)
                return true;
            return false;
        }).collect(Collectors.toList());

        // 자식 Comment 객체 맵핑
        for(OrderedComment comment : rootComments){
            List<OrderedComment> childComments = comments.stream().filter(target -> {
                if(target.getCommentData().getParentCommentID() == comment.getCommentData().getID())
                    return true;
                return false;
            }).collect(Collectors.toList());

            for (OrderedComment childComment : childComments){
                comment.getCommentData().addChildComment(childComment.getCommentData());
            }
        }

        return rootComments;
    }

    public class OrderedCommentRowMapper implements RowMapper<OrderedComment>{
        @Override
        public OrderedComment mapRow(ResultSet rs, int rowNum) throws SQLException {
            OrderedComment result = new OrderedComment();
            result.setOrderedCommentNo(rs.getInt("OrderedCommentNo"));

            Comment commentData = new Comment();
            commentData.setID(rs.getInt("ID"));
            commentData.setPostID(rs.getInt("PostID"));
            commentData.setParentCommentID(rs.getInt("ParentCommentID"));
            commentData.setWriterID(rs.getString("WriterID"));
            commentData.setContent(rs.getString("Content"));
            commentData.setGoodCount(rs.getInt("GoodCount"));
            commentData.setBadCount(rs.getInt("BadCount"));
            commentData.setCreatedDay(rs.getTimestamp("CreatedDay").toLocalDateTime());
            commentData.setModifyDay(rs.getTimestamp("ModifyDay").toLocalDateTime());
            Timestamp deleteDayTS = rs.getTimestamp("DeleteDay");
            if(deleteDayTS != null)
                commentData.setDeleteDay(deleteDayTS.toLocalDateTime());

            result.setCommentData(commentData);
            return result;
        }
    }
}
