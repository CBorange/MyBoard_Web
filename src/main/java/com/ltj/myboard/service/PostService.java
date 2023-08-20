package com.ltj.myboard.service;


import com.ltj.myboard.domain.*;
import com.ltj.myboard.dto.post.*;
import com.ltj.myboard.domain.ActivityHistoryTypes;
import com.ltj.myboard.repository.jpa.*;
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
import java.util.*;

@RequiredArgsConstructor
@Slf4j
@Service
public class PostService{
    @Value("${ftp.userfilepath}")
    private String userFilePath;

    private final FtpService ftpService;

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PostActivityHistoryRepository postActivityHistoryRepository;
    private final PostScrapRepository postScrapRepository;

    private final PostFileRepository postFileRepository;

    private final UserService userService;
    private final UserNotiService userNotiService;

    public Post findPostByID(int postID) {
        return postRepository.findById(postID).orElseThrow(
            () -> {
                String msg = String.format("Post Id [%d]에 해당하는 Post 데이터를 찾을 수 없습니다.", postID);
                log.info(msg);
                throw new NoSuchElementException(msg);
            }
        );
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

            // 추천 기록 조회
            List<PostActivityHistory> likesHistory = postActivityHistoryRepository.findAllByTypeAndPostId(ActivityHistoryTypes.Like.getValue(), post.getId());
            newFilteredPost.setLikeCount(likesHistory.stream().count());

            newFilteredPost.setPostData(post);

            ret.add(newFilteredPost);
            idx++;
        }

        return ret;
    }

    public List<FilteredPost> findPost_Best(String title, String content, String nickname,
                                                 PageRequest pageRequest, Ref<Integer> totalPageCntRet) {
        // 베스트 게시글을 조회 할 때 post_activity_history 테이블에 추천 기록이 1개 이상인 Post만 획득한다.
        // 이 때 '추천' 기록인지 판단하는 type 값은 서버 쪽 enum class와 DB의 activity_type 테이블에서 이중으로
        // 검증한다. 즉, enum class의 값과 DB의 값이 같아야만 동작한다(어느 한쪽이 달라지면 오류 발생)
        // 그래서 쿼리 실행할 때에도 enum class 기준으로 값을 넘겨줘야 한다
        Page<Post> queryRet = postRepository.findBestsByCondition(title, content, nickname,
                ActivityHistoryTypes.Like.getValue(), pageRequest);
        List<Post> retList = queryRet.getContent();
        totalPageCntRet.setValue(queryRet.getTotalPages());

        List<FilteredPost> ret = new ArrayList<>();

        int idx = 0;
        for(Post post : retList){
            FilteredPost newFilteredPost = new FilteredPost();
            newFilteredPost.setOrderedPostNo(idx + 1);
            newFilteredPost.setCommentCount(post.getComments().stream().count());

            // 추천 기록 조회
            List<PostActivityHistory> likesHistory = postActivityHistoryRepository.findAllByTypeAndPostId(ActivityHistoryTypes.Like.getValue(), post.getId());
            newFilteredPost.setLikeCount(likesHistory.stream().count());

            newFilteredPost.setPostData(post);

            ret.add(newFilteredPost);
            idx++;
        }

        return ret;
    }

    public List<FilteredPost> findPostByWriterId(String writerId, PageRequest pageRequest, Ref<Integer> totalPageCntRet){
        Page<Post> queryRet = postRepository.findAllByWriterId(writerId, pageRequest);
        List<Post> retList = queryRet.getContent();
        totalPageCntRet.setValue(queryRet.getTotalPages());

        List<FilteredPost> ret = new ArrayList<>();

        int idx =0;
        for (Post post : retList){
            FilteredPost newFilteredPost = new FilteredPost();
            newFilteredPost.setOrderedPostNo(idx + 1);
            newFilteredPost.setCommentCount(post.getComments().stream().count());

            // 추천 기록 조회
            List<PostActivityHistory> likesHistory = postActivityHistoryRepository.findAllByTypeAndPostId(ActivityHistoryTypes.Like.getValue(), post.getId());
            newFilteredPost.setLikeCount(likesHistory.stream().count());

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
    }

    private Post updatePost(String title, String content, int postID, String writerID, String writerNickname){
        Post foundPost = findPostByID(postID);

        foundPost.setTitle(title);
        foundPost.setContent(content);
        foundPost.setWriterId(writerID);
        foundPost.setWriterNickname(writerNickname);
        foundPost.setModifyDay(new Date());

        postRepository.save(foundPost);
        return foundPost;
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
            throw new IllegalArgumentException("PostService : submitPostFile Error, PostFileDelta의 State가 Insert 또는 Delete 가 아닙니다. 현재 State: " +
                    fileDelta.getState());
        }
    }
    //endregion

    @Transactional
    public PostActivityHistory applyLike(int postId, String userId) {
        long likeCountOnThis = postActivityHistoryRepository.countByTypeAndPostIdAndUserId(
                ActivityHistoryTypes.Like.getValue(), postId, userId);
        if(likeCountOnThis > 0){
            throw new IllegalStateException("이미 추천하였습니다.");
        }
        long dislikeCountOnThis = postActivityHistoryRepository.countByTypeAndPostIdAndUserId(
                ActivityHistoryTypes.Dislike.getValue(), postId, userId);
        if(dislikeCountOnThis > 0){
            throw new IllegalStateException("이미 비추천하였습니다. 추천 또는 비추천은 한번만 할 수 있습니다.");
        }

        PostActivityHistory newHistory = new PostActivityHistory();
        newHistory.setType(ActivityHistoryTypes.Like.getValue());
        newHistory.setPostId(postId);
        newHistory.setUserId(userId);
        newHistory.setCreatedDay(new Date());

        postActivityHistoryRepository.save(newHistory);

        // 알림 보내기
        Post post = findPostByID(postId);
        User user = userService.findUserByID(userId);

        userNotiService.makeNotificationForLike(userId, user.getNickname(), post.getWriterId(), post.getTitle(), postId);

        return newHistory;
    }

    public int deleteLike(int postId, String userId){
        PostActivityHistory history = postActivityHistoryRepository.findByTypeAndPostIdAndUserId(
                ActivityHistoryTypes.Like.getValue(), postId, userId)
                .orElseThrow(() -> new NoSuchElementException(postId + "/" + userId + " like history not found"));

        postActivityHistoryRepository.delete(history);
        return 1;
    }

    @Transactional
    public PostActivityHistory applyDislike(int postId, String userId){
        long dislikeCountOnThis = postActivityHistoryRepository.countByTypeAndPostIdAndUserId(
                ActivityHistoryTypes.Dislike.getValue(), postId, userId);
        if(dislikeCountOnThis > 0){
            throw new IllegalStateException("이미 비추천하였습니다.");
        }

        long likeCountOnThis = postActivityHistoryRepository.countByTypeAndPostIdAndUserId(
                ActivityHistoryTypes.Like.getValue(), postId, userId);
        if(likeCountOnThis > 0){
            throw new IllegalStateException("이미 추천하였습니다. 추천 또는 비추천은 한번만 할 수 있습니다.");
        }

        PostActivityHistory newHistory = new PostActivityHistory();
        newHistory.setType(ActivityHistoryTypes.Dislike.getValue());
        newHistory.setPostId(postId);
        newHistory.setUserId(userId);
        newHistory.setCreatedDay(new Date());

        postActivityHistoryRepository.save(newHistory);

        // 알림 보내기
        Post post = findPostByID(postId);
        User user = userService.findUserByID(userId);

        userNotiService.makeNotificationForDisLike(userId, user.getNickname(), post.getWriterId(), post.getTitle(), postId);

        return newHistory;
    }

    public int deleteDislike(int postId, String userId){
        PostActivityHistory history = postActivityHistoryRepository.findByTypeAndPostIdAndUserId(
                ActivityHistoryTypes.Dislike.getValue(), postId, userId)
                .orElseThrow(() -> new NoSuchElementException(postId + "/" + userId + " dislike history not found"));

        postActivityHistoryRepository.delete(history);
        return 1;
    }

    //region DeletePost
    @Transactional
    public void deletePostProcess(int postID){
        Post targetPost = findPostByID(postID);
        //clearCommentsOnPost(targetPost);
        deletePost(targetPost);
    }

    private void clearCommentsOnPost(Post target){
        // Comment 연관관계 삭제
        List<Comment> comments = target.getComments();
        comments.clear();
        postRepository.save(target);
    }

    private void deletePost(Post target) {
        postRepository.delete(target);
    }
    //endregion

    public List<OrderedPostScrap> findAllScrapByUser(String userId, PageRequest pageRequest, Ref<Integer> totalPageCntRet){
        Page<PostScrap> scraps = postScrapRepository.findAllByUserId(userId, pageRequest);
        List<PostScrap> retList = scraps.getContent();
        totalPageCntRet.setValue(scraps.getTotalPages());

        List<OrderedPostScrap> ret = new ArrayList<>();

        int idx = 0;
        for(PostScrap scrap : retList){
            OrderedPostScrap newOrderedScrap = new OrderedPostScrap();
            newOrderedScrap.setOrderedScrapNo(idx + 1);
            newOrderedScrap.setScrapData(scrap);

            ret.add(newOrderedScrap);
            idx++;
        }

        return ret;
    }

    public PostScrap makePostScrap(int postId, String userId, String remark){
        // 이미 스크랩 했는지 확인
        long scrapCount = postScrapRepository.countByPostIdAndUserId(postId, userId);
        if(scrapCount > 0){
            throw new IllegalStateException("이미 스크랩되었습니다.");
        }

        PostScrap newScrap = new PostScrap();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("cannot found post data " + postId));
        newScrap.setPost(post);
        newScrap.setUserId(userId);
        newScrap.setRemark(remark);
        newScrap.setCreatedDay(new Date());

        postScrapRepository.save(newScrap);
        return newScrap;
    }

    public void deletePostScrap(int scrapId){
        postScrapRepository.deleteById(scrapId);
    }

    public void modifyPostScrap(int scrapId, String newRemark){
        PostScrap foundScrap = postScrapRepository.findById(scrapId)
                .orElseThrow(() -> new NoSuchElementException("cannot found srcrap " + scrapId));
        foundScrap.setRemark(newRemark);
        foundScrap.setModifyDay(new Date());

        postScrapRepository.save(foundScrap);
    }
}
