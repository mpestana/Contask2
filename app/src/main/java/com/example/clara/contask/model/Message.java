package com.example.clara.contask.model;


import android.net.Uri;

import com.example.clara.contask.FunctionsUtil;
import com.google.firebase.firestore.DocumentReference;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;


public class Message  {


    private User user;
    private String userReference;


    private String textMessage;
    private long timeMessage;
    private String uriPhoto;

    public Message() {


    }

    public Message(String userReference, String textMessage, long timeMessage, String uriPhoto) {
        this.userReference = userReference;
        this.textMessage = textMessage;
        this.timeMessage = timeMessage;
        this.uriPhoto = uriPhoto;
    }


    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUserReference() {
        return userReference;
    }

    public void setUserReference(String userReference) {
        this.userReference = userReference;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public String getDateMessage() {

        return FunctionsUtil.getDateFormat(this.timeMessage);


    }

    public long getTimeMessage() {
        return timeMessage;
    }

    public String getHourMessage() {
        return FunctionsUtil.getHourFormat(this.timeMessage);
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public void setTimeMessage(long timeMessage) {
        this.timeMessage = timeMessage;
    }

    public String getUriPhoto() {
        return uriPhoto;
    }

    public void setUriPhoto(String uriPhoto) {
        this.uriPhoto = uriPhoto;
    }
}
