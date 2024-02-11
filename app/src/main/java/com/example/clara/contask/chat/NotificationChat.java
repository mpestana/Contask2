package com.example.clara.contask.chat;

import java.util.List;

public class NotificationChat {

    private List<String> registration_ids;

    private Notification notification;

    private Data data;

    public NotificationChat(List<String> registration_ids, String title, String body, String chatId) {
        this.registration_ids = registration_ids;
        this.data = new Data(chatId);
        this.notification= new Notification(title,body);
    }

    public List<String> getRegistration_ids() {
        return registration_ids;
    }

    public void setRegistration_ids(List<String> registration_ids) {
        this.registration_ids = registration_ids;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    private class Notification {
        private String title;
        private String body;

        public Notification(String title, String body) {
            this.title = title;
            this.body = body;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }
    }

    private class Data {
        private String chatId;

        public Data(String chatId) {
            this.chatId = chatId;
        }

        public String getChatId() {
            return chatId;
        }

        public void setChatId(String chatId) {
            this.chatId = chatId;
        }
    }
}
