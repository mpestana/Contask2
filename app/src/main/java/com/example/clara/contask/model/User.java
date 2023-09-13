package com.example.clara.contask.model;

public class User {
    private final String userId;

    private final String userName;

    private final String userPhotoUrl;


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
