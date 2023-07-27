package myboard;

import com.ltj.myboard.MyBoardApplication;
import com.ltj.myboard.domain.Post;
import com.ltj.myboard.dto.post.FilteredPost;
import com.ltj.myboard.repository.PostRepository;
import com.ltj.myboard.service.PostService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

@SpringBootTest(classes = MyBoardApplication.class)
@Transactional
public class PostServiceTest {
    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Test
    public void 제목검색조건으로_게시글조회(){
    }

    @Test
    public void 최신게시글조회(){
        int boardID = 126;
        int limit = 4;
        List<FilteredPost> result = postService.getLastestPost(boardID, limit);

        Assertions.assertTrue(result.stream().count() > 0);
    }

    @Test
    public void 게시글조회(){
        int postId = 167;
        Post foundPost =  postService.findPostByID(postId);

        long cnt = foundPost.getComments().stream().count();
    }

    @Test
    public void 다중조건_게시글조회(){
        int boardId = 126;
        String title = null;
        String content = "p";
        String nickname = null;

        Page<Post> ret = postRepository.findAllByCondition(boardId, title, content, nickname, PageRequest.of(99, 99));
    }

    @Test
    public void 내용검색조건으로_게시글조회(){

    }

    @Test
    public void 댓글검색조건으로_게시글조회(){

    }

    @Test
    public void 닉네임검색조건으로_게시글조회(){

    }

    @Test
    public void 페이지넘버_조건으로_게시글조회(){

    }

    @Test
    @Transactional
    public void 게시글추가() throws SQLException {

    }

    @Test
    public void 게시글수정(){
        // 게시글 생성
        

        // 생성된 게시글 수정
        
        // 검증
    }
}
