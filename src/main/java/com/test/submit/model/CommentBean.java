package com.test.submit.model;

import java.util.List;

public class CommentBean {
    private int id;
    private int commentId;
    private String memberId;
    private String createdTime;
    private String comment;
    private List<String> commentLikeList;

    public CommentBean(int id, int commentId, String memberId, String createdTime, String comment, List<String> commentLikeList) {
        this.id = id;
        this.commentId = commentId;
        this.memberId = memberId;
        this.createdTime = createdTime;
        this.comment = comment;
        this.commentLikeList = commentLikeList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<String> getCommentLikeList() {
        return this.commentLikeList;
    }

    public void setCommentLikeList(List<String> commentLikeList) {
        this.commentLikeList = commentLikeList;
    }

    public void insertCommentLikeList(String memberId) {
        this.commentLikeList.add(memberId);
    }

    public void deleteCommentLikeList(String memberId) {
        this.commentLikeList.remove(memberId);
    }
}
