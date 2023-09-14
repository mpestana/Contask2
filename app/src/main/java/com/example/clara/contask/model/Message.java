package com.example.clara.contask.model;

import java.time.LocalDate;

public class Message {
    private String userId;

    private String textMessage;

    private String dateMessage;

    private  String timeMessage;



    public Message() {

    }

    public Message(String userId, String textMessage, String dateMessage, String timeMessage) {
        this.userId = userId;
        this.textMessage = textMessage;
        this.dateMessage = dateMessage;
        this.timeMessage = timeMessage;
    }
    public String getUserId() {
        return userId;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public String getDateMessage() {
        return dateMessage;
    }

    public String getTimeMessage() {
        return timeMessage;
    }


}
