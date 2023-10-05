package com.example.clara.contask.model;


import android.net.Uri;

import com.google.firebase.firestore.DocumentReference;


public class MessageSend {



    private DocumentReference userReference;
    private String textMessage;
    private long timeMessage;

    private String uriPhoto;
    public MessageSend() {


    }

    public MessageSend(DocumentReference userReference, String textMessage, long timeMessage, String uriPhoto) {
        this.userReference = userReference;
        this.textMessage = textMessage;
        this.timeMessage = timeMessage;
        this.uriPhoto = uriPhoto;
    }

    public DocumentReference getUserReference() {
        return userReference;
    }

    public void setUserReference(DocumentReference userReference) {
        this.userReference = userReference;
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

    public String getUriPhoto() {
        return uriPhoto;
    }

    public void setUriPhoto(String uriPhoto) {
        this.uriPhoto = uriPhoto;
    }
}
