package com.ltj.myboard.dto.board;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BoardSearchParam {
    private int pageNumber;
    private String searchMethod;
    private String searchCondition;
    private String sortOrderTarget;
    private String sortMethod;
}
