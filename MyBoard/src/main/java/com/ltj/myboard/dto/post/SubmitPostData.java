package com.ltj.myboard.dto.post;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SubmitPostData {
    private String title;
    private String content;
    private PostFileDelta[] imageSource;

    // submit 하는 post의 상태, insert(신규 생성) or update(수정)
    private String state;

    //submit 하는 post의 ID, 신규 게시글 생성의 경우 ID가 들어오지 않음, '수정'일 경우에만 들어옴
    private int postId;

    private int boardId;
    private String writerId;
    private String writerNickname;

    public static boolean checkIsInsertData(SubmitPostData submitPostData){
        String state = submitPostData.getState().toLowerCase();
        if(state.equals("insert"))
            return true;
        return false;
    }

    public static boolean checkIsUpdateData(SubmitPostData submitPostData){
        String state = submitPostData.getState().toLowerCase();
        if(state.equals("update"))
            return true;
        return false;
    }
}
