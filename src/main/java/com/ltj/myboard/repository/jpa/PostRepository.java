package com.ltj.myboard.repository.jpa;

import com.ltj.myboard.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
            "      p.writer.id = ALL (SELECT id " +
            "                        FROM user " +
            "                        WHERE nickname LIKE CONCAT('%', COALESCE(:nickname,''), '%'))) " +
            "ORDER BY createdDay DESC")
    Page<Post> findAllByCondition(@Param("boardId") int boardId,
                                  @Param("title") String title,
                                  @Param("content") String content,
                                  @Param("nickname") String nickname,
                                  Pageable pageable);

    @Query("SELECT p " +
            "FROM post p " +
            "WHERE (0 < (SELECT count(id) " +
            "           FROM post_activity_history pa " +
            "           WHERE p.id = pa.postId AND pa.type = :like_type)) AND " +
            "      (:title IS NOT NULL AND p.title LIKE CONCAT('%', COALESCE(:title, ''), '%') OR " +
            "      :content IS NOT NULL AND p.content LIKE CONCAT('%', COALESCE(:content, ''), '%') OR " +
            "      p.writer.id = ALL (SELECT id " +
            "                        FROM user " +
            "                        WHERE nickname LIKE CONCAT('%', COALESCE(:nickname,''), '%'))) AND " +
            "      (0 < (SELECT count(type) " +
            "             FROM board b " +
            "             WHERE p.boardId = b.id AND b.boardType.type = com.ltj.myboard.domain.BoardTypeDefiner.Common)) " +
            "ORDER BY createdDay DESC")
    Page<Post> findBestsByCondition(@Param("title") String title,
                                    @Param("content") String content,
                                    @Param("nickname") String nickname,
                                    @Param("like_type") int likeType,
                                    Pageable pageable);
}
