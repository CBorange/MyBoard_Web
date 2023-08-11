package myboard.user;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ltj.myboard.MyBoardApplication;
import com.ltj.myboard.dto.auth.ChangeUserInfoRequest;
import com.ltj.myboard.dto.auth.ChangeUserPasswordRequest;
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
public class ChangeUserInfoTest {

    @Autowired
    private MockMvc mvc;

    // DTO Json 변환을 위한 ObjectMapper
    private final ObjectMapper objectMapper = new ObjectMapper();


    //유저정보 변경 API 실패 케이스(유효하지 않은 DTO) 테스트
    @Test
    public void changeInfo_WrongDTO() throws Exception {
        // given(유효하지 않은 DTO 작성)
        ChangeUserInfoRequest request = new ChangeUserInfoRequest();
        request.setUserID("user");
        request.setCurPassword("pw");
        request.setEmail("wrongwrong");     // 유효하지 않은 이메일 형식(@ 포함, .com, .net 등 필요)
        request.setNickname("!@$!@$@!$");   // 유효하지 않은 닉네임(특수문자 불가능, 2~12자 이내, 영문 or 숫자 or 한글 사용)

        // when & then
        mvc.perform(MockMvcRequestBuilders
                .patch("/user")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())   // CSRF 토큰
                .with(user("admin")))   // 인증이 필요한 API를 호출하기 위해 principal 만들어주는 mock util 실행
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    // 유저정보 변경 API 실패 케이스(다른 유저) 테스트
    @Test
    public void changeInfo_NotSameUser() throws Exception {
        // given(유효하지 않은 DTO 작성)
        ChangeUserInfoRequest request = new ChangeUserInfoRequest();
        request.setUserID("admin");
        request.setCurPassword("437624");
        request.setEmail("test@example.com");     // 유효한 이메일 형식(@ 포함, .com, .net 등 필요)
        request.setNickname("asd임시이름12");   // 유효한 닉네임(특수문자 불가능, 2~12자 이내, 영문 or 숫자 or 한글 사용)

        // when & then
        mvc.perform(MockMvcRequestBuilders
                        .patch("/user")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())   // CSRF 토큰
                        .with(user("ltj2020")))   // 변경하려는 유저와 다른 유저
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    // 유저정보 변경 API 실패 케이스(존재하는 이메일) 테스트
    @Test
    public void changeInfo_ExistEmail() throws  Exception{
        // given(유효하지 않은 DTO 작성)
        ChangeUserInfoRequest request = new ChangeUserInfoRequest();
        request.setUserID("admin");
        request.setCurPassword("437624");
        request.setEmail("test@example.com");     // 사용중인 다른 유저의 이메일
        request.setNickname("asd임시이름12");   // 유효한 닉네임(특수문자 불가능, 2~12자 이내, 영문 or 숫자 or 한글 사용)

        // when & then
        mvc.perform(MockMvcRequestBuilders
                        .patch("/user")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())   // CSRF 토큰
                        .with(user("admin")))   // 변경하려는 유저와 다른 유저
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    // 유저정보 변경 API 실패 케이스(존재하는 닉네임) 테스트
    @Test
    public void changeInfo_ExistNickname() throws  Exception{
        // given(유효하지 않은 DTO 작성)
        ChangeUserInfoRequest request = new ChangeUserInfoRequest();
        request.setUserID("admin");
        request.setCurPassword("437624");
        request.setEmail("test22@example.com");
        request.setNickname("79798");   // 사용중인 닉네임

        // when & then
        mvc.perform(MockMvcRequestBuilders
                        .patch("/user")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())   // CSRF 토큰
                        .with(user("admin")))   // 변경하려는 유저와 다른 유저
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    // 유저정보 변경 API(유효성검사) 성공 케이스 테스트
    @Test
    public void changeInfo_Success() throws Exception {
        // given(유효하지 않은 DTO 작성)
        ChangeUserInfoRequest request = new ChangeUserInfoRequest();
        request.setUserID("admin");
        request.setCurPassword("437624");
        request.setEmail("admin1@example.com");     // 유효한 이메일 형식(@ 포함, .com, .net 등 필요), 사용중이지 않은 이메일
        request.setNickname("asd임시이름12");   // 유효한 닉네임(특수문자 불가능, 2~12자 이내, 영문 or 숫자 or 한글 사용)

        // when & then
        mvc.perform(MockMvcRequestBuilders
                        .patch("/user")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())   // CSRF 토큰
                        .with(user("admin")))   // 변경하려는 유저와 같은 유저
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
