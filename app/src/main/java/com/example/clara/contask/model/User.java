package com.example.clara.contask.model;

public class User {
    private String userId;

    private String userName;

    private String userPhotoUrl;

    private String userToken;



    public User() {

    }

    public User(String userId, String userName, String userPhotoUrl) {
        this.userId = userId;
        this.userName = userName;
        this.userPhotoUrl = userPhotoUrl;

    }

    public User(String userId, String userName, String userPhotoUrl, String token) {
        this.userId = userId;
        this.userName = userName;
        this.userPhotoUrl = userPhotoUrl;
        this.userToken=token;

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
    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }




}
