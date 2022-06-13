package com.ltj.myboard.domain;

import java.sql.Timestamp;

public class Board {
    private int ID;

    private String BoardName;

    private String BoardOwnerID;

    private int ParentBoardID;

    private String BoardIcon;

    private Timestamp CreatedDay;

    private Timestamp ModifyDay;

    private Timestamp DeleteDay;

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
}
