package com.example.clara.contask.model;


import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;



public class Message  {


    private User user;
    private com.google.firebase.firestore.DocumentReference userMessage;


    private String textMessage;
    private long timeMessage;


    public Message() {


    }

    public Message(com.google.firebase.firestore.DocumentReference userMessage, String textMessage, long timeMessage) {
        this.userMessage = userMessage;
        this.textMessage = textMessage;
        this.timeMessage = timeMessage;


    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public com.google.firebase.firestore.DocumentReference getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(com.google.firebase.firestore.DocumentReference userMessage) {
        this.userMessage = userMessage;
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
