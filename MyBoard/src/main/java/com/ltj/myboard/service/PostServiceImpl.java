package com.ltj.myboard.service;

import com.ltj.myboard.domain.Post;
import com.ltj.myboard.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository){
        this.postRepository = postRepository;
    }

    @Override
    public Optional<Post> findPostByID(int postID) {
        return postRepository.findPostByID(postID);
    }

    @Override
    public List<Post> findPostByBoardID(int boardID) {
        return postRepository.findPostByBoardID(boardID);
    }

    @Override
    public List<Post> findPostByWriterID(String writerID) {
        return postRepository.findPostByWriterID(writerID);
    }
}
