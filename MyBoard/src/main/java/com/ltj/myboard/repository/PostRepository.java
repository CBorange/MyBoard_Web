package com.ltj.myboard.repository;

import com.ltj.myboard.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Integer> {
    Optional<Post> findById(int postID);
    List<Post> findAllByBoardId(int boardID);
    List<Post> findAllByWriterId(String writerID);
}
