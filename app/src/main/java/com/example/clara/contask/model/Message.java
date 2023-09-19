package com.example.clara.contask.model;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;


public class Message  {


    private User user;
    private com.google.firebase.firestore.DocumentReference userReference;


    private String textMessage;
    private long timeMessage;


    public Message() {


    }

    public Message(com.google.firebase.firestore.DocumentReference userReference, String textMessage, long timeMessage) {
        this.userReference = userReference;
        this.textMessage = textMessage;
        this.timeMessage = timeMessage;


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
}
