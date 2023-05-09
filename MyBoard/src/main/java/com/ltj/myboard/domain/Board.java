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
public class Board {

    public Board(){
        childBoardSet = new HashSet<Board>();
    }

    private int id;

    private String boardName;

    private String boardOwnerId;

    private int parentBoardId;

    private String boardIcon;

    private LocalDateTime createdDay;

    private LocalDateTime modifyDay;

    private LocalDateTime deleteDay;

    // 여기부터 비즈니스 로직 관련 변수
    private HashSet<Board> childBoardSet;

    public int getChildBoardCount(){
        return (int)childBoardSet.stream().count();
    }

    public boolean addChildBoard(Board newBoard){
        return childBoardSet.add(newBoard);
    }

    public boolean addChildBoard(List<Board> newBoards){
        return childBoardSet.addAll(newBoards);
    }

    public boolean removeChildBoard(Board removeBoard){
        return childBoardSet.remove(removeBoard);
    }

    public boolean removeChildBoardByID(int removeBoardID){
        Optional<Board> boardOptional = childBoardSet.stream().filter(board -> {
            if(board.id == removeBoardID)
                return true;
            return false;
        }).findAny();

        Board foundBoard =  boardOptional.get();
        if(foundBoard != null){
            childBoardSet.remove(foundBoard);
            return true;
        }
        return false;
    }
    @Override
    public int hashCode(){
        // ID가 유일한 PK다. ID가 같으면 같은 Board다.
        return this.id;
    }

    @Override
    public boolean equals(Object object){
        if(object == null)
            return false;

        if(this.getClass() != object.getClass())
            return false;

        Board board = (Board)object;

        // ID가 같으면 같은 Board다 ID가 유일한 PK다.
        if(board.getId() == this.id)
            return true;
        return false;
    }
}
