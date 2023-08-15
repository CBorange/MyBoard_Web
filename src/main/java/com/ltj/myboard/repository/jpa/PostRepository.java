package com.ltj.myboard.repository.jpa;

import com.ltj.myboard.domain.Post;
import com.ltj.myboard.model.ActivityHistoryTypes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Integer> {
    Optional<Post> findById(int postID);
    List<Post> findAllByBoardId(int boardID, Pageable pageable);
    List<Post> findAllByBoardId(int boardID);

    Page<Post> findAllByWriterId(String writerId, Pageable pageable);

    @Query("SELECT p " +
            "FROM post p " +
            "WHERE p.boardId = :boardId AND " +
            "      (:title IS NOT NULL AND p.title LIKE CONCAT('%', COALESCE(:title, ''), '%') OR " +
            "      :content IS NOT NULL AND p.content LIKE CONCAT('%', COALESCE(:content, ''), '%') OR " +
            "      :nickname IS NOT NULL AND p.writerNickname LIKE CONCAT('%', COALESCE(:nickname, ''), '%')) " +
            "ORDER BY createdDay DESC")
    Page<Post> findAllByCondition(@Param("boardId") int boardId,
                                  @Param("title") String title,
                                  @Param("content") String content,
                                  @Param("nickname") String nickname,
                                  Pageable pageable);

    /**
     * 베스트 게시글 조회, post_activity_history의 like type 개수 조회 서브쿼리 부분 '2'라고 하드코딩 한 부분 서브 쿼리 하나 더 만들어서 조회하는 쪽으로 수정 필요*/
    @Query("SELECT p " +
            "FROM post p " +
            "WHERE (0 < (SELECT count(id) " +
            "           FROM post_activity_history pa " +
            "           WHERE p.id = pa.postId AND pa.type = :like_type)) AND " +
            "      (:title IS NOT NULL AND p.title LIKE CONCAT('%', COALESCE(:title, ''), '%') OR " +
            "      :content IS NOT NULL AND p.content LIKE CONCAT('%', COALESCE(:content, ''), '%') OR " +
            "      :nickname IS NOT NULL AND p.writerNickname LIKE CONCAT('%', COALESCE(:nickname, ''), '%')) " +
            "ORDER BY createdDay DESC")
    Page<Post> findBestsByCondition(@Param("title") String title,
                                    @Param("content") String content,
                                    @Param("nickname") String nickname,
                                    @Param("like_type") int likeType,
                                    Pageable pageable);
}
