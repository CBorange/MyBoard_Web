package myboard;

import com.ltj.myboard.MyBoardApplication;
import com.ltj.myboard.domain.Comment;
import com.ltj.myboard.dto.post.OrderedComment;
import com.ltj.myboard.service.CommentService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

@SpringBootTest(classes = MyBoardApplication.class)
public class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Test
    public void 댓글조회(){
        // given
        int postId = 11;

        // when
        List<OrderedComment> rets = commentService.findRootCommentInPost(postId);

        // then
        Assertions.assertThat(rets.stream().count()).isGreaterThan(0);
    }

    @Test
    @Transactional
    public void 댓글작성() throws SQLException {
        Comment insertedComment = commentService.insertComment(12, null, "admin", "테스트 댓글");
        Assertions.assertThat(insertedComment).isNotNull();
    }
}
