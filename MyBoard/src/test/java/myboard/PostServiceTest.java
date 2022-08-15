package myboard;

import com.ltj.myboard.MyBoardApplication;
import com.ltj.myboard.domain.Post;
import com.ltj.myboard.dto.board.FilteredPost;
import com.ltj.myboard.service.PostService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@SpringBootTest(classes = MyBoardApplication.class)
public class PostServiceTest {
    @Autowired
    private PostService postService;

    @Test
    public void 제목검색조건으로_게시글조회(){
        List<FilteredPost> findResult = postService.findPost_UserParam(115,"Title", "21","ModifyDay","ASC");
        Assertions.assertTrue(findResult.stream().count() > 0);
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
        Post insertedPost = postService.insertPost("테스트 추가",
                "<p>테스트 내용</p>", 126, "admin");
        Assertions.assertNotNull(insertedPost);
    }
}
