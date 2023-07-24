package com.ltj.myboard.repository;
import com.ltj.myboard.domain.PostFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostFileRepository extends JpaRepository<PostFile, Integer> {
    List<PostFile> findAllByPostId(int postID);
}
