package com.ltj.myboard.domain;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "board_type")
@Getter
@Setter
@NoArgsConstructor
public class BoardType implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @Convert(converter = BoardTypeDefinerConverter.class)
    private BoardTypeDefiner type;
}
