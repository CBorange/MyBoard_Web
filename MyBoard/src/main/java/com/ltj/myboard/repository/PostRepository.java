package com.ltj.myboard.repository;

import com.ltj.myboard.domain.Post;

import java.util.List;

public interface PostRepository {
    List<Post> findPostByBoardID(int boardID);
    List<Post> findPostByWriterID(String writerID);
}
