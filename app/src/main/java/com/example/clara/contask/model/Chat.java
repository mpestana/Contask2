package com.example.clara.contask.model;

import java.util.List;

public class Chat {

    private String nameChat;

    private List<Message> messages;

    private List<String> usersIds;

    private String chatId;

    private String chatPhotoUrl;

    public Chat() {

    }

    public Chat(String nameChat, List<Message> messages, List<String> usersIds, String chatId, String chatPhotoUrl) {
        this.nameChat = nameChat;
        this.messages = messages;
        this.usersIds = usersIds;
        this.chatId = chatId;
        this.chatPhotoUrl = chatPhotoUrl;
    }

    public String getNameChat() {
        return nameChat;
    }

    public void setNameChat(String nameChat) {
        this.nameChat = nameChat;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public List<String> getUsersIds() {
        return usersIds;
    }

    public void setUsersIds(List<String> usersIds) {
        this.usersIds = usersIds;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getChatPhotoUrl() {
        return chatPhotoUrl;
    }

    public void setChatPhotoUrl(String chatPhotoUrl) {
        this.chatPhotoUrl = chatPhotoUrl;
    }
}
