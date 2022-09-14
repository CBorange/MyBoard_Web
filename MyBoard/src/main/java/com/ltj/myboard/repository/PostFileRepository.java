package com.ltj.myboard.repository;
import com.ltj.myboard.domain.PostFile;
import java.util.List;

public interface PostFileRepository {
    int insertPostFile(int postID, String fileID, String fileName);
    List<PostFile> findPostFilesByPostID(int postID);
}
