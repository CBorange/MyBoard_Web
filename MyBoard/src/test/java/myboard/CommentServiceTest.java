package myboard;

import com.ltj.myboard.MyBoardApplication;
import com.ltj.myboard.domain.Comment;
import com.ltj.myboard.service.CommentService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(classes = MyBoardApplication.class)
public class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Test
    public void 댓글조회(){
        // given
        int postId = 1;

        // when
        List<Comment> rets = commentService.findCommentInPost(postId);

        // then
        Assertions.assertThat(rets.stream().count() > 0);
    }
}
