package com.ltj.myboard.domain;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Board {

    public Board(){
        childBoardSet = new HashSet<Board>();
    }

    private int ID;

    private String BoardName;

    private String BoardOwnerID;

    private int ParentBoardID;

    private String BoardIcon;

    private LocalDateTime CreatedDay;

    private LocalDateTime ModifyDay;

    private LocalDateTime DeleteDay;

    // 여기부터 비즈니스 로직 관련 변수
    private HashSet<Board> childBoardSet;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getBoardName() {
        return BoardName;
    }

    public void setBoardName(String boardName) {
        BoardName = boardName;
    }

    public String getBoardOwnerID() {
        return BoardOwnerID;
    }

    public void setBoardOwnerID(String boardOwnerID) {
        BoardOwnerID = boardOwnerID;
    }

    public int getParentBoardID() {
        return ParentBoardID;
    }

    public void setParentBoardID(int parentBoardID) {
        ParentBoardID = parentBoardID;
    }

    public String getBoardIcon() {
        return BoardIcon;
    }

    public void setBoardIcon(String boardIcon) {
        BoardIcon = boardIcon;
    }

    public LocalDateTime getCreatedDay() {
        return CreatedDay;
    }

    public void setCreatedDay(LocalDateTime createdDay) {
        CreatedDay = createdDay;
    }

    public LocalDateTime getModifyDay() {
        return ModifyDay;
    }

    public void setModifyDay(LocalDateTime modifyDay) {
        ModifyDay = modifyDay;
    }

    public LocalDateTime getDeleteDay() {
        return DeleteDay;
    }

    public void setDeleteDay(LocalDateTime deleteDay) {
        DeleteDay = deleteDay;
    }

    public HashSet<Board> getChildBoardSet() {
        return childBoardSet;
    }

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
            if(board.ID == removeBoardID)
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
    public String toString() {
        return "Board{" +
                "ID=" + ID +
                ", BoardName='" + BoardName + '\'' +
                ", BoardOwnerID='" + BoardOwnerID + '\'' +
                ", ParentBoardID=" + ParentBoardID +
                ", BoardIcon='" + BoardIcon + '\'' +
                ", CreatedDay=" + CreatedDay +
                ", ModifyDay=" + ModifyDay +
                ", DeleteDay=" + DeleteDay +
                ", childBoardSet=" + childBoardSet +
                '}';
    }

    @Override
    public int hashCode(){
        // ID가 유일한 PK다. ID가 같으면 같은 Board다.
        return this.ID;
    }

    @Override
    public boolean equals(Object object){
        if(object == null)
            return false;

        if(this.getClass() != object.getClass())
            return false;

        Board board = (Board)object;

        // ID가 같으면 같은 Board다 ID가 유일한 PK다.
        if(board.getID() == this.ID)
            return true;
        return false;
    }
}
