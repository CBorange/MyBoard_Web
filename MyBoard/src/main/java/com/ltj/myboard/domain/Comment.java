package com.ltj.myboard.domain;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Entity(name = "comment")
@Getter
@Setter
@ToString
public class Comment {

    public Comment() {
        childCommentSet = new HashSet<Comment>();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="post_id")
    private int postId;

    @Column(name = "parent_comment_id")
    private Integer parentCommentId;

    @Column(name = "writer_id")
    private String writerId;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @Column(name = "good_count")
    private int goodCount;

    @Column(name = "bad_count")
    private int badCount;

    @Column(name = "created_day")
    private Date createdDay;

    @Column(name = "modify_day")
    private Date modifyDay;

    @Column(name = "delete_day")
    private Date deleteDay;

    // 여기부터 비즈니스 로직 관련 변수
    @Transient
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
