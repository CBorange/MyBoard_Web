package com.ltj.myboard.repository;

import com.ltj.myboard.domain.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    Optional<Post> findPostByID(int postID);
    List<Post> findPostByBoardID(int boardID);
    List<Post> findPostByWriterID(String writerID);
}
