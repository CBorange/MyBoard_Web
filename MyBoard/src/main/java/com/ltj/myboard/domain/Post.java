package com.ltj.myboard.domain;

import java.sql.Timestamp;

public class Post {
    private int ID;

    private int BoardID;

    private String WriterID;

    private String Title;

    private String Content;

    private int ViewCount;

    private int GoodCount;

    private int BadCount;

    private Timestamp CreatedDay;

    private Timestamp ModifyDay;

    private Timestamp DeleteDay;

    private int PostNumber;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getBoardID() {
        return BoardID;
    }

    public void setBoardID(int boardID) {
        BoardID = boardID;
    }

    public String getWriterID() {
        return WriterID;
    }

    public void setWriterID(String writerID) {
        WriterID = writerID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public int getViewCount() {
        return ViewCount;
    }

    public void setViewCount(int viewCount) {
        ViewCount = viewCount;
    }

    public int getGoodCount() {
        return GoodCount;
    }

    public void setGoodCount(int goodCount) {
        GoodCount = goodCount;
    }

    public int getBadCount() {
        return BadCount;
    }

    public void setBadCount(int badCount) {
        BadCount = badCount;
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

    public int getPostNumber() {
        return PostNumber;
    }

    public void setPostNumber(int postNumber) {
        PostNumber = postNumber;
    }
}
