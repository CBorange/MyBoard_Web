package com.ltj.myboard.domain;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@ToString
public class Comment {

    public Comment() {
        childCommentSet = new HashSet<Comment>();
    }

    private int id;

    private int post_id;

    private Integer parent_comment_id;

    private String writer_id;

    private String content;

    private int good_count;

    private int bad_count;

    private LocalDateTime created_day;

    private LocalDateTime modify_day;

    private LocalDateTime delete_day;

    // 여기부터 비즈니스 로직 관련 변수
    private HashSet<Comment> childCommentSet;

    public boolean addChildComment(Comment comment){
        return childCommentSet.add(comment);
    }

    public boolean addChildComment(List<Comment> commentList){
        return childCommentSet.addAll(commentList);
    }

    public boolean removeChildComment(Comment comment){
        return childCommentSet.remove(comment);
    }

    public boolean removeChildCommentByID(int commentID){
        Optional<Comment> commentOptional = childCommentSet.stream().filter(comment -> {
            if(comment.id == commentID)
                return true;
            return false;
        }).findAny();

        Comment foundComment = commentOptional.get();
        if(foundComment != null){
            childCommentSet.remove(foundComment);
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null)
            return false;

        if(this.getClass() != o.getClass())
            return false;

        Comment comment = (Comment)o;

        // ID가 같으면 같은 Comment.
        if(comment.getId() == this.id)
            return true;
        return false;
    }
}
