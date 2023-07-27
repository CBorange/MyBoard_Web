package com.ltj.myboard.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.core.annotation.Order;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Entity(name = "board")
@Getter
@Setter
@ToString
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @Column(name="owner_id")
    private String ownerId;

    private String icon;

    @Column(name = "created_day")
    private Date createdDay;

    @Column(name = "modify_day")
    private Date modifyDay;

    @Column(name = "delete_day")
    private Date deleteDay;

    @Column(name = "sort_order")
    private int sortOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_board_id", referencedColumnName = "id", nullable = true)
    private Board parentBoard = null;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parentBoard", cascade = CascadeType.REMOVE)
    @OrderBy("sort_order")
    private List<Board> childBoards;
}
