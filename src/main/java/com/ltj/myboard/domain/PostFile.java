package com.ltj.myboard.domain;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity(name = "post_file")
@Getter
@Setter
@ToString
public class PostFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="post_id", nullable = false)
    private int postId;

    @Column(name="file_id")
    private String fileId;

    @Column(name="file_name")
    private String fileName;
}
