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
@Transactional
public class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Test
    public void 댓글조회(){
        // given
        int commentId = 408;

        // when
        Comment found = commentService.findCommentById(commentId);
        Comment parentComment = found.getParentComment();
        // then
        Assertions.assertThat(found != null);
    }

    @Test
    public void 루트_댓글조회(){

    }

    @Test
    public void 게시글_댓글조회(){
    }

    @Test
    @Transactional
    public void 댓글작성() throws SQLException {

    }
}
