package myboard;

import com.ltj.myboard.domain.Board;
import com.ltj.myboard.service.BoardService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.List;

@SpringBootTest
public class BoardServiceTest {

    @Autowired
    private BoardService boardService;

    @Test
    public void 루트_게시판_읽어오기(){
        // when
        List<Board> rootBoards = boardService.getAllRootBoards();

        // then
        for (Board rootBoard : rootBoards){
            Assertions.assertEquals(0, rootBoard.getParent_board_id());
        }
    }

    @Test
    public void 전체_게시판_읽어오기(){
        // when
        List<Board> boards = boardService.getAllBoards();
        for (Board item : boards){
            if(item.getParent_board_id() == 0){
                HashSet<Board> boardSet = item.getChildBoardSet();
                for(Board childBoard : boardSet){
                    System.out.println(childBoard.toString());
                }
            }
        }
    }

    @Test
    public void ID로_게시판찾기(){

    }
}
