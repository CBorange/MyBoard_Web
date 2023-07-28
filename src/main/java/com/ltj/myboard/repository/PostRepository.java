package com.ltj.myboard.repository;

import com.ltj.myboard.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
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

    @Query("SELECT p " +
            "FROM post p " +
            "WHERE (0 < (SELECT count(id) " +
            "           FROM post_likes_history pl " +
            "           WHERE p.id = pl.postId)) AND " +
            "      (:title IS NOT NULL AND p.title LIKE CONCAT('%', COALESCE(:title, ''), '%') OR " +
            "      :content IS NOT NULL AND p.content LIKE CONCAT('%', COALESCE(:content, ''), '%') OR " +
            "      :nickname IS NOT NULL AND p.writerNickname LIKE CONCAT('%', COALESCE(:nickname, ''), '%')) " +
            "ORDER BY createdDay DESC")
    Page<Post> findBestsByCondition(@Param("title") String title,
                                    @Param("content") String content,
                                    @Param("nickname") String nickname,
                                    Pageable pageable);
}
