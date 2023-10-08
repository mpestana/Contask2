package com.example.clara.contask.model;

import java.util.List;

public class Chat {

    private String nameChat;

    private List<Message> messages;

    private List<String> usersIds;

    private String chatId;
    private String campaignId;
    private String typeChat;
    private String chatPhotoUrl;

    private List<String> usersOnline;

    private List<String> allTokensMessagingUsers;

    public Chat() {

    }

    public Chat(String nameChat, List<Message> messages, List<String> usersIds, String chatId, String campaignId, String typeChat, String chatPhotoUrl, List<String> usersOnline, List<String> allTokensMessagingUsers) {
        this.nameChat = nameChat;
        this.messages = messages;
        this.usersIds = usersIds;
        this.chatId = chatId;
        this.campaignId = campaignId;
        this.typeChat = typeChat;
        this.chatPhotoUrl = chatPhotoUrl;
        this.usersOnline = usersOnline;
        this.allTokensMessagingUsers = allTokensMessagingUsers;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
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

    public List<String> getUsersOnline() {
        return usersOnline;
    }

    public void setUsersOnline(List<String> usersOnline) {
        this.usersOnline = usersOnline;
    }

    public List<String> getAllTokensMessagingUsers() {
        return allTokensMessagingUsers;
    }

    public void setAllTokensMessagingUsers(List<String> allTokensMessagingUsers) {
        this.allTokensMessagingUsers = allTokensMessagingUsers;
    }

    public String getTypeChat() {
        return typeChat;
    }

    public void setTypeChat(String typeChat) {
        this.typeChat = typeChat;
    }
}
