package com.ltj.myboard.repository;

import com.ltj.myboard.domain.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    Optional<Post> findPostByID(int postID);
    List<Post> findAllPostByBoardID(int boardID);

    List<Post> findPost_UseSearch_Title(int boardID, String condition_title);
    List<Post> findPost_UseSearch_Content(int boardID, String condition_content);
    List<Post> findPost_UseSearch_Comment(int boardID, String condition_comment);
    List<Post> findPost_UseSearch_Nickname(int boardID, String condition_nickname);

    List<Post> findPostByWriterID(String writerID);
}
