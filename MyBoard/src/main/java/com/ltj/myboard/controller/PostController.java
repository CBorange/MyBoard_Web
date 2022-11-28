package com.ltj.myboard.controller;

import com.ltj.myboard.domain.Board;
import com.ltj.myboard.domain.Post;
import com.ltj.myboard.dto.post.OrderedComment;
import com.ltj.myboard.dto.post.SubmitPostData;
import com.ltj.myboard.service.BoardService;
import com.ltj.myboard.service.CommentService;
import com.ltj.myboard.service.PostService;
import com.ltj.myboard.util.Paginator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
                                            @RequestParam(required = false, defaultValue = "1") int pageNumber){
        addLayoutModel_FragmentContent(model, "post.html", "post");

        // Post 정보 Model에 추가
        Optional<Post> foundPost = postService.findPostByID(id);
        foundPost.ifPresentOrElse((post) -> {
            model.addAttribute("postInfo", foundPost.get());
        }, () -> {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        });

        // Board 정보 Model에 추가
        Optional<Board> foundBoard = boardService.findBoardByID(foundPost.get().getBoardID());
        foundBoard.ifPresentOrElse((board) -> {
            model.addAttribute("boardInfo", foundBoard.get());
        }, () -> {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        });

        // Comment 정보 얻어냄
        List<OrderedComment> comments = commentService.findRootCommentInPost(foundPost.get().getID());

        // 페이지 개수 구하기
        long commentCount = comments.stream().count();
        int pageCount = Paginator.getPageCount(commentCount, MAX_VISIBLE_COMMENT_COUNT_INPAGE);

        // 현재 페이지의 세션 구하기
        int curSession = Paginator.getCurSessionByCurPage(pageNumber, MAX_VISIBLE_PAGE_COUNT_INSESSION);
        int endPageNoInCurSession = curSession * MAX_VISIBLE_PAGE_COUNT_INSESSION;
        int startPageNoInCurSession = endPageNoInCurSession - (MAX_VISIBLE_PAGE_COUNT_INSESSION - 1);
        if(pageCount < endPageNoInCurSession) endPageNoInCurSession = pageCount;

        // 정렬된 댓글 리스트에서 현재 페이지에 해당하는 부분만 filtering
        List<OrderedComment> filteredCommentList = commentService.filterCommentDataInCurPage(comments, pageNumber,
                MAX_VISIBLE_COMMENT_COUNT_INPAGE);
        model.addAttribute("filteredCommentList", filteredCommentList);

        // 현재 페이지 정보 Model에 추가
        model.addAttribute("commentCount", commentCount);
        model.addAttribute("curPageNo", pageNumber);
        model.addAttribute("endPageNoInCurSession", endPageNoInCurSession);
        model.addAttribute("startPageNoInCurSession", startPageNoInCurSession);
        model.addAttribute("pageCount", pageCount);

        return LayoutViewPath;
    }

    @PostMapping("/post")
    public ResponseEntity submitPost(@RequestBody SubmitPostData submitPostData) {
        try {
            Post insertedPost = postService.submitPostProcess(submitPostData);
            String redirectURL = String.format("/post/%d", insertedPost.getID());

            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", redirectURL);
            return new ResponseEntity<String>(headers, HttpStatus.SEE_OTHER);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @DeleteMapping("/post/{id}")
    public ResponseEntity deletePost(@PathVariable("id") int id){
        try{
            int deleteCount = postService.deletePostProcess(id);
            return new ResponseEntity(deleteCount, HttpStatus.OK);
        } catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/writepostform/{id}")
    public String getWritePostPage(Model model,@PathVariable(value = "id", required = false) int id,
                                   @RequestParam(required = true) int boardID){
        addLayoutModel_FragmentContent(model, "writepostform.html", "writepostform");

        // Board 정보 Model에 추가
        Optional<Board> foundBoard = boardService.findBoardByID(boardID);
        foundBoard.ifPresentOrElse((board) -> {
            model.addAttribute("boardInfo", foundBoard.get());
        }, () -> {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        });

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
            Optional<Post> foundPost = postService.findPostByID(id);
            foundPost.ifPresentOrElse((post) -> {
                model.addAttribute("postInfo", foundPost.get());
            }, () -> {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
            });
        }

        return LayoutViewPath;
    }
}
