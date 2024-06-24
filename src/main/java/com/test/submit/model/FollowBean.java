package com.test.submit.model;

public class FollowBean {
    private String followerId; // 팔로우하는 사람.
    private String followeeId; // 팔로우 당하는 사람.
    private String followedTime;

    public FollowBean(String followerId, String followeeId, String followedTime) {
        this.followerId = followerId;
        this.followeeId = followeeId;
        this.followedTime = followedTime;
    }
    
    public String getFollowerId() {
        return followerId;
    }
    public void setFollowerId(String followerId) {
        this.followerId = followerId;
    }

    public String getFolloweeId() {
        return followeeId;
    }
    public void setFolloweeId(String followeeId) {
        this.followeeId = followeeId;
    }

    public String getFollowedTime() {
        return followedTime;
    }
    public void setFollowedTime(String followedTime) {
        this.followedTime = followedTime;
    }
}
