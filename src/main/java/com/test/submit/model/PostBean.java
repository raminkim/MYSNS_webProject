package com.test.submit.model;

public class PostBean {
    private int id;
    private String memberId;
    private String title;
    private String content;
    private String fileName;
    private String createdTime;
    

    public PostBean(int id, String memberId, String title, String content, String fileName, String createdTime) {
        this.id = id;
        this.memberId = memberId;
        this.title = title;
        this.content = content;
        this.fileName = fileName;
        this.createdTime = createdTime;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }
}