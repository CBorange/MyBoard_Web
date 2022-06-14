package com.ltj.myboard.domain;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;

public class Board {
    private int ID;

    private String BoardName;

    private String BoardOwnerID;

    private int ParentBoardID;

    private String BoardIcon;

    private Timestamp CreatedDay;

    private Timestamp ModifyDay;

    private Timestamp DeleteDay;

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

    public Timestamp getCreatedDay() {
        return CreatedDay;
    }

    public void setCreatedDay(Timestamp createdDay) {
        CreatedDay = createdDay;
    }

    public Timestamp getModifyDay() {
        return ModifyDay;
    }

    public void setModifyDay(Timestamp modifyDay) {
        ModifyDay = modifyDay;
    }

    public Timestamp getDeleteDay() {
        return DeleteDay;
    }

    public void setDeleteDay(Timestamp deleteDay) {
        DeleteDay = deleteDay;
    }

    public HashSet<Board> getChildBoardSet() {
        return childBoardSet;
    }

    public boolean addChildBoard(Board newBoard){
        return childBoardSet.add(newBoard);
    }

    public boolean removeChildBoard(Board removeBoard){
        return childBoardSet.remove(removeBoard);
    }

    public boolean removeChildBoardByID(int removeBoardID){
        return false;
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
