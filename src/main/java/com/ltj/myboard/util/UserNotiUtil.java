package com.ltj.myboard.util;

public class UserNotiUtil {
    private static String getCuttedContent(String content){
        if(content.length() > 50){
            content = content.substring(0, 50);
            content += "...";
        }
        return content;
    }

    public static String makeContentForComment(String senderNickname, String commentContent){
        commentContent = getCuttedContent(commentContent);
        return String.format("%s님이 회원님의 게시글에 \"%s\" 라고 댓글을 남겼습니다.", senderNickname, commentContent);
    }

    public static String makeContentForSubComment(String senderNickname, String commentContent){
        commentContent = getCuttedContent(commentContent);
        return String.format("%s님이 회원님의 댓글에 \"%s\" 라고 대댓글을 남겼습니다.", senderNickname, commentContent);
    }

    public static String makeContentForLikePost(String senderNickname, String postTitle){
        return String.format("%s님이 회원님의 \"%s\" 게시글을 추천하였습니다.", senderNickname, postTitle);
    }

    public static String makeContentForDislikePost(String senderNickname, String postTitle){
        return String.format("%s님이 회원님의 \"%s\" 게시글을 비추천하였습니다.", senderNickname, postTitle);
    }
}
