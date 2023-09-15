package com.example.clara.contask.model;


import com.google.firebase.firestore.DocumentReference;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;


public class MessageSend {



    private DocumentReference userMessage;


    private String textMessage;
    private long timeMessage;


    public MessageSend() {


    }

    public MessageSend(DocumentReference userMessage, String textMessage, long timeMessage) {
        this.userMessage = userMessage;
        this.textMessage = textMessage;
        this.timeMessage = timeMessage;

    }

    public DocumentReference getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(DocumentReference userMessage) {
        this.userMessage = userMessage;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public long getTimeMessage() {
        return timeMessage;
    }

    public void setTimeMessage(long timeMessage) {
        this.timeMessage = timeMessage;
    }
}
