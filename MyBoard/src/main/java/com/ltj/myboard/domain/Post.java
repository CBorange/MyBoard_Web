package com.ltj.myboard.domain;
import java.time.LocalDateTime;

public class Post {
    private int ID;

    private int BoardID;

    private String WriterID;

    private String Title;

    private String Content;

    private int ViewCount;

    private int GoodCount;

    private int BadCount;

    private LocalDateTime CreatedDay;

    private LocalDateTime ModifyDay;

    private LocalDateTime DeleteDay;

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
}
