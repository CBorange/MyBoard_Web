package com.ltj.myboard.repository;
import com.ltj.myboard.domain.PostFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostFileRepository extends JpaRepository<PostFile, Integer> {
    List<PostFile> findAllByPostId(int postID);
}
