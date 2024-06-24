package com.test.submit.model;
import java.io.Serializable;

public class LoginBean implements java.io.Serializable {
    private String id;
    private String password;
    private String name;
    private int followers = 0;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getFollowers() {
        return followers;
    }
    public void increaseFollow() {
        followers += 1;
    }
    public void decreaseFollow() {
        followers -= 1;
    }
}