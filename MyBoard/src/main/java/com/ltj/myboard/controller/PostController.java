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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.sql.SQLException;
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

    @GetMapping("/writepostform")
    public String getWritePostPage(Model model, @RequestParam(required = true) int boardID){
        addLayoutModel_FragmentContent(model, "writepostform.html", "writepostform");

        // Board 정보 Model에 추가
        Optional<Board> foundBoard = boardService.findBoardByID(boardID);
        foundBoard.ifPresentOrElse((board) -> {
            model.addAttribute("boardInfo", foundBoard.get());
        }, () -> {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        });

        return LayoutViewPath;
    }

    @PutMapping("/post")
    public ResponseEntity submitPost(@RequestBody SubmitPostData submitPostData){
        try {
            Post insertedPost = postService.insertPost(submitPostData.getTitle(), submitPostData.getContent(),
                    submitPostData.getBoardID(), submitPostData.getWriterID());
            String redirectURL = String.format("/post/%d", insertedPost.getID());

            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", redirectURL);
            return new ResponseEntity<String>(headers, HttpStatus.SEE_OTHER);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
