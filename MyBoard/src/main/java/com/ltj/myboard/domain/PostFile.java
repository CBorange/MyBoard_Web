package com.ltj.myboard.domain;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "postfile")
@Getter
@Setter
@ToString
public class PostFile {
    @Id
    private int id;

    @Column(name="post_id")
    private int postId;

    @Column(name="file_id")
    private String fileId;

    @Column(name="file_name")
    private String fileName;
}
