package myboard.post;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ltj.myboard.MyBoardApplication;
import com.ltj.myboard.dto.post.SubmitPostData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@SpringBootTest(classes = MyBoardApplication.class)
@AutoConfigureMockMvc
@Transactional
public class SubmitPostTest {
    @Autowired
    private MockMvc mvc;

    // DTO Json 변환을 위한 ObjectMapper
    private final ObjectMapper objectMapper = new ObjectMapper();

    // 게시글 작성 실패 케이스(유효하지 않은 DTO) 테스트
    @Test
    public void submitInsertPost_WrongDTO() throws Exception {
        // given
        SubmitPostData request = new SubmitPostData();
        request.setContent("");     // 빈 내용
        request.setTitle("1");       // 빈 제목
        request.setBoardId(126);    // 자유게시판
        request.setWriterId("admin");
        request.setState("insert");

        // when & then
        mvc.perform(MockMvcRequestBuilders
                .post("/post")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())
                .with(user("admin")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    // 게시글 작성 성공 케이스 테스트
    @Test
    public void submitInsertPost_Success() throws Exception {
        // given
        SubmitPostData request = new SubmitPostData();
        request.setContent("asfasfsaf");
        request.setTitle("temp_title");
        request.setBoardId(126);    // 자유게시판
        request.setWriterId("admin");
        request.setState("insert");

        // when & then
        mvc.perform(MockMvcRequestBuilders
                        .post("/post")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
                        .with(user("admin")))
                .andExpect(MockMvcResultMatchers.status().isSeeOther());
    }

}
