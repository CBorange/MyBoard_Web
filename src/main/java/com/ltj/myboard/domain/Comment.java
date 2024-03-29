package com.ltj.myboard.domain;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity(name = "comment")
@Getter
@Setter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="post_id", nullable = false)
    private int postId;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @Column(name = "created_day")
    private Date createdDay;

    @Column(name = "modify_day")
    private Date modifyDay;

    @Column(name = "delete_day")
    private Date deleteDay;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "writer_id", referencedColumnName = "id")
    private User writer;

    // 부모 Comment
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id", referencedColumnName = "id", nullable = true)
    private Comment parentComment = null;

    // 자식 Comment
    // mappedBy는 mapping 되는 객체(여기서는 자식 Comment)에서 참조하는 부모객체의 field명을 의미한다.
    // 이 case에서 childComments 리스트의 원소 Comment의 parentComment field는 부모 Comment를 가르키고 있으므로
    // mappedBy는 parentComment가 되어야 한다.
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parentComment", cascade = CascadeType.ALL)
    @OrderBy("created_day")
    private List<Comment> childComments;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "comment_id", insertable = false, updatable = false)
    private List<CommentActivityHistory> activityHistories;

    public long getLikesCount(){
        long count = activityHistories.stream()
                .filter(history -> history.getType() == ActivityHistoryTypes.Like.getValue()).count();
        return count;
    }

    public long getDislikesCount(){
        long count = activityHistories.stream()
                .filter(history -> history.getType() == ActivityHistoryTypes.Dislike.getValue()).count();
        return count;
    }
}
