package com.ltj.myboard.domain;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "post_scrap")
@Getter
@Setter
@ToString
public class PostScrap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="user_id")
    private String userId;

    @Column(name="created_day")
    private Date createdDay;

    @Column(name="modify_day")
    private Date modifyDay;

    private String remark;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", referencedColumnName = "id", nullable = false)
    private Post post = null;
}
