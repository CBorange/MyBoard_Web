package com.ltj.myboard.service;


import com.ltj.myboard.domain.*;
import com.ltj.myboard.dto.post.ApplyLikeData;
import com.ltj.myboard.dto.post.FilteredPost;
import com.ltj.myboard.dto.post.PostFileDelta;
import com.ltj.myboard.dto.post.SubmitPostData;
import com.ltj.myboard.repository.PostDislikesHistoryRepository;
import com.ltj.myboard.repository.PostFileRepository;
import com.ltj.myboard.repository.PostLikesHistoryRepository;
import com.ltj.myboard.repository.PostRepository;
import com.ltj.myboard.util.Ref;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class PostService{
    @Value("${ftp.userfilepath}")
    private String userFilePath;

    private final FtpService ftpService;

    private final PostRepository postRepository;
    private final PostLikesHistoryRepository postLikesHistoryRepository;
    private final PostDislikesHistoryRepository postDislikesHistoryRepository;

    private final PostFileRepository postFileRepository;

    private final UserService userService;

    public Optional<Post> findPostByID(int postID) {
        return postRepository.findById(postID);
    }

    public long getLikesCount(int postId){
        return postLikesHistoryRepository.countByPostId(postId);
    }

    public long getDislikesCount(int postId){
        return postDislikesHistoryRepository.countByPostId(postId);
    }

    public List<FilteredPost> getLastestPost(int boardID, int resultLimit){
        List<Post> lastestPosts = postRepository.findAllByBoardId(boardID,
                PageRequest.of(0, resultLimit, Sort.by(Sort.Direction.DESC, "modifyDay")));

        List<FilteredPost> ret = new ArrayList<>();

        int idx = 0;
        for(Post post : lastestPosts){
            FilteredPost newFilteredPost = new FilteredPost();
            newFilteredPost.setOrderedPostNo(idx + 1);
            long commentCnt = post.getComments().stream().count();
            newFilteredPost.setCommentCount(commentCnt);
            newFilteredPost.setPostData(post);

            ret.add(newFilteredPost);
            idx++;
        }
        return ret;
    }

    public List<FilteredPost> findPost_UserParam(int boardID, String title, String content, String nickname,
                                                 PageRequest pageRequest, Ref<Integer> totalPageCntRet) {
        Page<Post> queryRet = postRepository.findAllByCondition(boardID, title, content, nickname, pageRequest);
        List<Post> retList = queryRet.getContent();
        totalPageCntRet.setValue(queryRet.getTotalPages());

        List<FilteredPost> ret = new ArrayList<>();

        int idx = 0;
        for(Post post : retList){
            FilteredPost newFilteredPost = new FilteredPost();
            newFilteredPost.setOrderedPostNo(idx + 1);
            newFilteredPost.setCommentCount(post.getComments().stream().count());
            newFilteredPost.setLikeCount(post.getLikesHistories().stream().count());
            newFilteredPost.setPostData(post);

            ret.add(newFilteredPost);
            idx++;
        }

        return ret;
    }

    public List<FilteredPost> findPost_Best(String title, String content, String nickname,
                                                 PageRequest pageRequest, Ref<Integer> totalPageCntRet) {
        Page<Post> queryRet = postRepository.findBestsByCondition(title, content, nickname, pageRequest);
        List<Post> retList = queryRet.getContent();
        totalPageCntRet.setValue(queryRet.getTotalPages());

        List<FilteredPost> ret = new ArrayList<>();

        int idx = 0;
        for(Post post : retList){
            FilteredPost newFilteredPost = new FilteredPost();
            newFilteredPost.setOrderedPostNo(idx + 1);
            newFilteredPost.setCommentCount(post.getComments().stream().count());
            newFilteredPost.setLikeCount(post.getLikesHistories().stream().count());
            newFilteredPost.setPostData(post);

            ret.add(newFilteredPost);
            idx++;
        }

        return ret;
    }

    //region SubmitPost(Insert, Update)
    @Transactional
    public Post submitPostProcess(SubmitPostData submitPostData) throws IOException {
        // SubmitPostData 의 State에 따라 게시글을 생성할지, 수정할지 결정한다.
        // * 게시글 삭제는 별도의 API로 접근한다 이 함수로 들어오지 않는다.

        // 1. 현재 submit 데이터가 게시글 '생성' 인지, '수정'인지 검사한다.
        // state 값으로 생성인지 수정인지 검사
        Post submittedPost = null;
        if(submitPostData.getState().equals("insert")){
            // 1-1. 게시글 생성, 신규 Post ID 채번
            submittedPost = insertPost(submitPostData.getTitle(),
                                        submitPostData.getContent(),
                                        submitPostData.getBoardId(),
                                        submitPostData.getWriterId(),
                                        submitPostData.getWriterNickname());
        } else {
            // 1-2. 게시글 내용 수정
            submittedPost = updatePost(submitPostData.getTitle(),
                                        submitPostData.getContent(),
                                        submitPostData.getPostId(),
                                        submitPostData.getWriterId(),
                                        submitPostData.getWriterNickname());
        }

        // 2. 게시글에 등록된 파일 state에 따라 적절한 처리 실행
        // * 게시글 '생성'의 경우 insert State만 들어올 것이고, '수정'의 경우 복합적으로 State가 들어올 것임
        PostFileDelta[] targetFiles = submitPostData.getImageSource();
        for(PostFileDelta delta : targetFiles){
            submitPostFile(submittedPost.getId(), delta);
        }
        
        return submittedPost;
    }

    private Post insertPost(String title, String content, int boardID, String writerID, String writerNickname) {
        try{
            Post newPost = new Post();
            newPost.setTitle(title);
            newPost.setContent(content);
            newPost.setBoardId(boardID);
            newPost.setWriterId(writerID);
            newPost.setWriterNickname(writerNickname);
            newPost.setCreatedDay(new Date());
            newPost.setModifyDay(new Date());

            postRepository.save(newPost);
            return newPost;
        }catch (Exception e){
            log.error("insertPost Error, " + e.getMessage());
            throw new IllegalStateException("insertPost Error" + e.getMessage());
        }
    }

    private Post updatePost(String title, String content, int postID, String writerID, String writerNickname){
        try{
            Post foundPost = postRepository.findById(postID).orElseThrow(() -> {
                log.error("updatePost Error occured " + postID + "doesn't exist");
                throw new IllegalStateException("updatePost Error occured " + postID + "doesn't exist");
            });
            foundPost.setTitle(title);
            foundPost.setContent(content);
            foundPost.setWriterId(writerID);
            foundPost.setWriterNickname(writerNickname);
            foundPost.setModifyDay(new Date());

            postRepository.save(foundPost);
            return foundPost;
        } catch (Exception e){
            log.error("updatePost Error, " + e.getMessage());
            throw new IllegalStateException("updatePost Error" + e.getMessage());
        }
    }

    private PostFile submitPostFile(int postID, PostFileDelta fileDelta) throws IOException {
        // PostFileDelta의 State에 따라서 적절한 처리를 실행한다.
        // PostFileDelta의 State는 항상 Insert 또는 Delete 둘 중 하나이다.
        // * 사용자가 기등록한 게시글의 이미지를 수정할 수 있는 경로는 이미지를 제거 후 재등록 하는 경우 밖에 없으므로
        // 이 와 같은 경우 Delete, Insert로 총 2번 이 함수가 호출될 것이다.
        if(PostFileDelta.checkIsInsertData(fileDelta)) {
            PostFile newPostFile = new PostFile();
            newPostFile.setPostId(postID);
            newPostFile.setFileId(fileDelta.getFileID());
            newPostFile.setFileName(fileDelta.getFileName());

            postFileRepository.save(newPostFile);

            return newPostFile;
        } else if (PostFileDelta.checkIsDeleteData(fileDelta)){
            List<PostFile> targetFiles = postFileRepository.findAllByPostId(postID);
            for(PostFile file : targetFiles){
                ftpService.deleteFile(userFilePath, fileDelta.getFileName());
                postFileRepository.delete(file);
            }
            return null;
        } else{
            throw new IllegalStateException("PostService : submitPostFile Error, PostFileDelta의 State가 Insert 또는 Delete 가 아닙니다. 현재 State: " +
                    fileDelta.getState());
        }
    }
    //endregion

    @Transactional
    public PostLikesHistory applyLikePost(int postId, String userId) {
        long likeCountOnThis = postLikesHistoryRepository.countByPostIdAndUserId(postId, userId);
        if(likeCountOnThis > 0){
            throw new IllegalStateException("이미 추천하였습니다.");
        }
        long dislikeCountOnThis = postDislikesHistoryRepository.countByPostIdAndUserId(postId, userId);
        if(dislikeCountOnThis > 0){
            throw new IllegalStateException("이미 비추천하였습니다. 추천 또는 비추천은 한번만 할 수 있습니다.");
        }

        PostLikesHistory newHistory = new PostLikesHistory();
        newHistory.setPostId(postId);
        newHistory.setUserId(userId);
        newHistory.setCreatedDay(new Date());

        postLikesHistoryRepository.save(newHistory);

        // 알림 보내기
        Post post = postRepository.findById(postId).orElseThrow(() -> {
            return new IllegalStateException(String.format("cannot found post for make notification when add post like history [post id : %d]", postId));
        });
        User user = userService.findUserByID(userId).orElseThrow(() -> {
            return new IllegalStateException(String.format("cannot found sender user for make notification when add post like history [user id : %d]", userId));
        });

        userService.makeNotificationForLike(userId, user.getNickname(), post.getWriterId(), post.getTitle(), postId);

        return newHistory;
    }

    public int deleteLikePost(int postId, String userId){
        PostLikesHistory history = postLikesHistoryRepository.findByPostIdAndUserId(postId, userId)
                .orElseThrow(() -> new IllegalStateException(postId + "/" + userId + " like history not found"));

        postLikesHistoryRepository.delete(history);
        return 1;
    }

    @Transactional
    public PostDislikesHistory applyDislikePost(int postId, String userId){
        long dislikeCountOnThis = postDislikesHistoryRepository.countByPostIdAndUserId(postId, userId);
        if(dislikeCountOnThis > 0){
            throw new IllegalStateException("이미 비추천하였습니다.");
        }
        long likeCountOnThis = postLikesHistoryRepository.countByPostIdAndUserId(postId, userId);
        if(likeCountOnThis > 0){
            throw new IllegalStateException("이미 추천하였습니다. 추천 또는 비추천은 한번만 할 수 있습니다.");
        }

        PostDislikesHistory newHistory = new PostDislikesHistory();
        newHistory.setPostId(postId);
        newHistory.setUserId(userId);
        newHistory.setCreatedDay(new Date());

        postDislikesHistoryRepository.save(newHistory);

        // 알림 보내기
        Post post = postRepository.findById(postId).orElseThrow(() -> {
            return new IllegalStateException(String.format("cannot found post for make notification when add post like history [post id : %d]", postId));
        });
        User user = userService.findUserByID(userId).orElseThrow(() -> {
            return new IllegalStateException(String.format("cannot found sender user for make notification when add post like history [user id : %d]", userId));
        });

        userService.makeNotificationForDisLike(userId, user.getNickname(), post.getWriterId(), post.getTitle(), postId);

        return newHistory;
    }

    public int deleteDislikePost(int postId, String userId){
        PostDislikesHistory history = postDislikesHistoryRepository.findByPostIdAndUserId(postId, userId)
                .orElseThrow(() -> new IllegalStateException(postId + "/" + userId + " like history not found"));

        postDislikesHistoryRepository.delete(history);
        return 1;
    }

    //region DeletePost
    @Transactional
    public int deletePostProcess(int postID){
        int deleteCount = deletePost(postID);
        return deleteCount;
    }

    private int deletePost(int postID) {
        try{
            postRepository.deleteById(postID);
            return 1;
        }catch (Exception e){
            log.error("deletePost Error, " + e.getMessage());
            throw new IllegalStateException("deletePost Error" + e.getMessage());
        }
    }
    //endregion
}
