package com.ltj.myboard.domain;
import com.ltj.myboard.model.ActivityHistoryTypes;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity(name = "post")
@Getter
@Setter
@ToString(exclude = "comments")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="board_id")
    private int boardId;

    @Column(name="writer_id")
    private String writerId;

    @Column(name="writer_nickname")
    private String writerNickname;

    private String title;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @Column(name="view_count")
    private int viewCount;

    // Comment Entities와 같은 조건으로 처리
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "post_id", insertable = false, updatable = false)
    private List<PostActivityHistory> activityHistories;

    @Column(name="created_day")
    private Date createdDay;

    @Column(name="modify_day")
    private Date modifyDay;

    @Column(name="delete_day")
    private Date deleteDay;

    // JPA에서 매핑된 객체에 대해 변경이 일어날 경우
    // insert -> update -> delete 순으로 처리한다.
    // 예를 들어 자식 entity 리스트에 신규 객체를 추가하고 일부를 삭제 하였다면
    // 신규 추가된 객체에 대하여 insert 쿼리르 실행하고 삭제된 자식 객체의 fk 값을 null로 처리한다.(soft delete)
    // 여기에 orphanRemoval 값이 true로 되어 있으면 null 차리된 자식객체(더 이상 Relation이 없는 고아 객체)를 삭제한다.
    // Comment Entities의 경우 Post 삭제시에 바로 삭제 되어야 하는데 Post와 연결되는 fk 컬럼(post_id)가 nullable 하지 않다.
    // 따라서 (fk 값을 null로 설정 -> 고아 객체 확인하여 삭제) 과정을 이행할 수 없다.
    // 그래서 Comment 객체에 대한 Insert, Update는 독립적으로 이루어지도록 하고 insertable, updatable을 false로 처리하여
    // 삭제시에 fk 값을 null 하지 않고 바로 삭제하게끔(CascadeType.ALL) 한다.
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "post_id", insertable = false, updatable = false)
    private List<Comment> comments;

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
