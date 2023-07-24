package com.ltj.myboard.dto.post;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PostFileDelta {
    private String fileID;
    private String fileName;
    private String state;

    public static boolean checkIsInsertData(PostFileDelta delta){
        String state = delta.getState().toLowerCase();
        if(state.equals("insert"))
            return true;
        return false;
    }

    public static boolean checkIsDeleteData(PostFileDelta delta){
        String state = delta.getState().toLowerCase();
        if(state.equals("delete"))
            return true;
        return false;
    }
}
