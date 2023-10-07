package com.example.clara.contask.model;

import java.util.List;

public class Campaign {

    private String name;

    private List<Message> messagesFeed;

    private List<String> usersIds;

    private List<Task> tasks;
    private String campaignId;

    private String campaignPhotoUrl;

    private long begin;
    private long end;

    public Campaign() {

    }
    public Campaign(String name, List<Message> messagesFeed, List<String> usersIds, List<Task> tasks, String campaignId, String campaignPhotoUrl, long begin, long end) {
        this.name = name;
        this.messagesFeed = messagesFeed;
        this.usersIds = usersIds;
        this.tasks = tasks;
        this.campaignId = campaignId;
        this.campaignPhotoUrl = campaignPhotoUrl;
        this.begin = begin;
        this.end = end;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Message> getMessagesFeed() {
        return messagesFeed;
    }

    public void setMessagesFeed(List<Message> messagesFeed) {
        this.messagesFeed = messagesFeed;
    }

    public List<String> getUsersIds() {
        return usersIds;
    }

    public void setUsersIds(List<String> usersIds) {
        this.usersIds = usersIds;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public String getCampaignPhotoUrl() {
        return campaignPhotoUrl;
    }

    public void setCampaignPhotoUrl(String campaignPhotoUrl) {
        this.campaignPhotoUrl = campaignPhotoUrl;
    }

    public long getBegin() {
        return begin;
    }

    public void setBegin(long begin) {
        this.begin = begin;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

}
