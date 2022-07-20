package com.ltj.myboard.repository;
import com.ltj.myboard.dto.board.FilteredPost;

import java.util.List;

public interface FilteredPostRepository {
    List<FilteredPost> findPost_UseSearch_Title(int boardID, String condition_title, String sortTargetColumn, String orderByMethod) ;
    List<FilteredPost> findPost_UseSearch_Content(int boardID, String condition_content, String sortTargetColumn, String orderByMethod);
    List<FilteredPost> findPost_UseSearch_Comment(int boardID, String condition_comment, String sortTargetColumn, String orderByMethod);
    List<FilteredPost> findPost_UseSearch_Nickname(int boardID, String condition_nickname, String sortTargetColumn, String orderByMethod);
}
