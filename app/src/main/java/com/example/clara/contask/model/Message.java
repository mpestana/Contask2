package com.example.clara.contask.model;


import android.net.Uri;

import com.google.firebase.firestore.DocumentReference;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;


public class Message  {


    private User user;
    private com.google.firebase.firestore.DocumentReference userReference;


    private String textMessage;
    private long timeMessage;
    private String uriPhoto;

    public Message() {


    }

    public Message(DocumentReference userReference, String textMessage, long timeMessage, String uriPhoto) {
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

    public com.google.firebase.firestore.DocumentReference getUserReference() {
        return userReference;
    }

    public void setUserReference(com.google.firebase.firestore.DocumentReference userReference) {
        this.userReference = userReference;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public String getDateMessage() {
        Timestamp timestamp = new Timestamp(this.timeMessage);
        String date = new SimpleDateFormat("dd/MM/yyyy").format(timestamp.getTime());
        return date.toString();


    }

    public long getTimeMessage() {
        return timeMessage;
    }

    public String getHourMessage() {

        Timestamp timestamp = new Timestamp(this.timeMessage);
        String date = new SimpleDateFormat("HH:mm").format(timestamp.getTime());

        return date.toString();
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
