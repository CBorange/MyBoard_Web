package com.ltj.myboard.domain;

import java.time.LocalDateTime;

public class Comment {
    private int ID;

    private int PostID;

    private int ParentCommentID;

    private String WriterID;

    private String Content;

    private int GoodCount;

    private int BadCount;

    private LocalDateTime CreatedDay;

    private LocalDateTime MoodifyDay;

    private LocalDateTime DeleteDay;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getPostID() {
        return PostID;
    }

    public void setPostID(int postID) {
        PostID = postID;
    }

    public int getParentCommentID() {
        return ParentCommentID;
    }

    public void setParentCommentID(int parentCommentID) {
        ParentCommentID = parentCommentID;
    }

    public String getWriterID() {
        return WriterID;
    }

    public void setWriterID(String writerID) {
        WriterID = writerID;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
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

    public LocalDateTime getMoodifyDay() {
        return MoodifyDay;
    }

    public void setMoodifyDay(LocalDateTime moodifyDay) {
        MoodifyDay = moodifyDay;
    }

    public LocalDateTime getDeleteDay() {
        return DeleteDay;
    }

    public void setDeleteDay(LocalDateTime deleteDay) {
        DeleteDay = deleteDay;
    }
}
