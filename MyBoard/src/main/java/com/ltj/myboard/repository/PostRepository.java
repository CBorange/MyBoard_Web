package com.ltj.myboard.repository;

import com.ltj.myboard.domain.Post;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface PostRepository {
    Optional<Post> findPostByID(int postID);
    List<Post> findAllPostByBoardID(int boardID);
    List<Post> findPostByWriterID(String writerID);
    int insertPost(String title, String content, int boardID, String writerID);
}
