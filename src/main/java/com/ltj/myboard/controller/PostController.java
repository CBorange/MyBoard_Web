package com.ltj.myboard.controller;

import com.ltj.myboard.domain.*;
import com.ltj.myboard.dto.post.OrderedComment;
import com.ltj.myboard.dto.post.SubmitPostData;
import com.ltj.myboard.service.BoardService;
import com.ltj.myboard.service.CommentService;
import com.ltj.myboard.service.PostService;
import com.ltj.myboard.util.Paginator;
import com.ltj.myboard.util.Ref;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PostController extends LayoutControllerBase {
    private final BoardService boardService;
    private final PostService postService;
    private final CommentService commentService;

    private final int MAX_VISIBLE_PAGE_COUNT_INSESSION = 9;
    private final int MAX_VISIBLE_COMMENT_COUNT_INPAGE = 30;

    @GetMapping("/post/{id}")
    public String getPostPage(Model model, @PathVariable("id") int id,
                                            @RequestParam(required = false, defaultValue = "1") int pageNumber,
                                            @RequestParam(required = false) Integer focusCommentId){
        addLayoutModel_FragmentContent(model, "post.html", "post");

        // Post 정보 Model에 추가
        Post foundPost = postService.findPostByID(id);
        model.addAttribute("postInfo", foundPost);

        // 추천/비추천 개수
        model.addAttribute("likesCount", foundPost.getLikesCount());
        model.addAttribute("dislikesCount", foundPost.getDislikesCount());

        // Board 정보 Model에 추가
        Board foundBoard = boardService.findBoardByID(foundPost.getBoardId());
        model.addAttribute("boardInfo", foundBoard);

        // 포커싱 하려는 댓글(주로 알림 체크에 해당) 있으면 해당 Comment가 존재하는 Page 탐색
        // Root Comment 에서 해당하는 댓글 있는지 탐색, 있으면 페이지 넘버 변경
        if(focusCommentId != null){
            List<Comment> allRootComments = commentService.getAllRootCommentsInPost(id);

            // 모든 Root 댓글 탐색
            Iterator<Comment> rootIterator = allRootComments.iterator();
            int searchPage = 1;
            while(rootIterator.hasNext()){
                boolean searchExit = false;

                for(int i=0; i < MAX_VISIBLE_COMMENT_COUNT_INPAGE; ++i){
                    if(rootIterator.hasNext()){
                        // Root Comment 탐색
                        Comment searchTarget = rootIterator.next();
                        if(searchTarget.getId() == focusCommentId.intValue()){
                            pageNumber = searchPage;
                            searchExit = true;
                            break;
                        }

                        // Root->Sub Comment 탐색
                        List<Comment> subComments = searchTarget.getChildComments();
                        if(subComments != null && subComments.stream().count() > 0){
                            for(Comment searchSubTarget : subComments){
                                if(searchSubTarget.getId() == focusCommentId.intValue()){
                                    pageNumber = searchPage;
                                    searchExit = true;
                                    break;
                                }
                            }
                            if(searchExit) break;
                        }
                    } else{
                        searchExit = true;
                        break;
                    }
                }

                if(searchExit) break;
                searchPage++;
            }
            model.addAttribute("focustCommentId", focusCommentId.intValue());
        }

        // 타겟 페이지 Comment 리스트 얻어냄
        Ref<Integer> totalPageRef = new Ref<>();
        List<OrderedComment> comments = commentService.findRootCommentInPost(foundPost.getId(),
                PageRequest.of(pageNumber - 1,
                                MAX_VISIBLE_COMMENT_COUNT_INPAGE,
                                Sort.by(Sort.Direction.ASC, "createdDay")),
                                totalPageRef);

        long commentCount = commentService.getCommentCountByPost(id);

        // 페이지 개수 구하기
        int pageCount = totalPageRef.getValue();
        if(pageCount == 0)
            pageCount = 1;
        if(pageNumber > pageCount)
            throw new IllegalArgumentException(pageNumber + " page is out of bound");

        // 현재 페이지의 세션 구하기
        int curSession = Paginator.getCurSessionByCurPage(pageNumber, MAX_VISIBLE_PAGE_COUNT_INSESSION);
        int endPageNoInCurSession = curSession * MAX_VISIBLE_PAGE_COUNT_INSESSION;
        int startPageNoInCurSession = endPageNoInCurSession - (MAX_VISIBLE_PAGE_COUNT_INSESSION - 1);
        if(pageCount < endPageNoInCurSession) endPageNoInCurSession = pageCount;

        model.addAttribute("orderedCommentList", comments);

        // 현재 페이지 정보 Model에 추가
        model.addAttribute("commentCount", commentCount);
        model.addAttribute("curPageNo", pageNumber);
        model.addAttribute("endPageNoInCurSession", endPageNoInCurSession);
        model.addAttribute("startPageNoInCurSession", startPageNoInCurSession);
        model.addAttribute("pageCount", pageCount);

        return LayoutViewPath;
    }

    @PostMapping("/post")
    public ResponseEntity submitPost(@Valid @RequestBody SubmitPostData submitPostData) throws IOException {
        Post insertedPost = postService.submitPostProcess(submitPostData);
        String redirectURL = String.format("/post/%d", insertedPost.getId());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", redirectURL);
        return new ResponseEntity<String>(headers, HttpStatus.SEE_OTHER);
    }

    @DeleteMapping("/post/{id}")
    public ResponseEntity deletePost(@PathVariable("id") int id){
        postService.deletePostProcess(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/writepostform/{id}")
    public String getWritePostPage(Model model,@PathVariable(value = "id", required = false) int id,
                                   @RequestParam(required = true) int boardID){
        addLayoutModel_FragmentContent(model, "writepostform.html", "writepostform");

        // Board 정보 Model에 추가
        Board foundBoard = boardService.findBoardByID(boardID);
        model.addAttribute("boardInfo", foundBoard);

        // id로 수정인지 신규 글쓰기인지 판단, id가 0보다 작은 값이면 신규 글쓰기
        // id가 0보다 크다면 기존 게시글 수정으로 처리
        if(id < 0){
            model.addAttribute("editMode", "write");

            // 신규 작성의 경우 빈 post 데이터 전송
            // 빈 데이터라 해도 무조건 전송해야함 tyhmeleaf rendering 순서상 js보다 먼저 실행되기 때문에(html 호출되는 시점)
            // postInfo 데이터가 없으면 오류발생
            model.addAttribute("postInfo", new Post());
        } else{
            // 게시글 수정의 경우 게시글 제목 및 내용 model로 전달
            model.addAttribute("editMode", "modify");

            // post 정보 get
            Post foundPost = postService.findPostByID(id);
            model.addAttribute("postInfo", foundPost);
        }

        return LayoutViewPath;
    }

    @PostMapping("/post/{id}/like")
    public ResponseEntity applyLikePost(@PathVariable("id") int id, @RequestParam("userId") String userId){
        PostActivityHistory history = postService.applyLike(id, userId);
        return new ResponseEntity(history, HttpStatus.CREATED);
    }

    @DeleteMapping("/post/{id}/like")
    public ResponseEntity deleteLikePost(@PathVariable("id") int id, @RequestParam("userId") String userId){
        int ret = postService.deleteLike(id, userId);
        return new ResponseEntity(ret, HttpStatus.NO_CONTENT);
    }

    @PostMapping("/post/{id}/dislike")
    public ResponseEntity applyDislikePost(@PathVariable("id") int id, @RequestParam("userId") String userId){
        PostActivityHistory history = postService.applyDislike(id, userId);
        return new ResponseEntity(history, HttpStatus.CREATED);
    }

    @DeleteMapping("/post/{id}/dislike")
    public ResponseEntity deleteDislikePost(@PathVariable("id") int id, @RequestParam("userId") String userId){
        int ret = postService.deleteDislike(id, userId);
        return new ResponseEntity(ret, HttpStatus.NO_CONTENT);
    }

    @PostMapping("/post/{id}/scrap")
    public ResponseEntity makePostScrap(@PathVariable("id") int id,
                                        @RequestParam("userId") String userId,
                                        @RequestParam("remark") String remark){
        PostScrap ret = postService.makePostScrap(id, userId, remark);
        return new ResponseEntity(ret, HttpStatus.CREATED);
    }

    @DeleteMapping("/post/scrap/{id}")
    public ResponseEntity removePostScrap(@PathVariable("id") int id){
        postService.deletePostScrap(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/post/scrap/{id}")
    public ResponseEntity modifyPostScrap(@PathVariable("id") int id,
                                          @RequestParam("newRemark") String newRemark){
        postService.modifyPostScrap(id, newRemark);
        return new ResponseEntity(HttpStatus.OK);
    }
}
