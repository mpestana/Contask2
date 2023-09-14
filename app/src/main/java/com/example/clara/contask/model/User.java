package com.example.clara.contask.model;

public class User {
    private String userId;

    private String userName;

    private String userPhotoUrl;

    public User() {

    }

    public User(String userId, String userName, String userPhotoUrl) {
        this.userId = userId;
        this.userName = userName;
        this.userPhotoUrl = userPhotoUrl;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPhotoUrl() {
        return userPhotoUrl;
    }

}
