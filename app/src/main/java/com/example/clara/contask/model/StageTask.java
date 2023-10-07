package com.example.clara.contask.model;

import java.util.List;

public class StageTask {

    private  String title;

    private String description;

    private List<String> dependecyStageTasksIds;
    private long begin;
    private long end;

    private String chatId;

    private List<String> usersIds;

    public StageTask() {
    }

    public StageTask(String title, String description, List<String> dependecyStageTasksIds, long begin, long end, String chatId, List<String> usersIds) {
        this.title = title;
        this.description = description;
        this.dependecyStageTasksIds = dependecyStageTasksIds;
        this.begin = begin;
        this.end = end;
        this.chatId = chatId;
        this.usersIds = usersIds;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getDependecyStageTasksIds() {
        return dependecyStageTasksIds;
    }

    public void setDependecyStageTasksIds(List<String> dependecyStageTasksIds) {
        this.dependecyStageTasksIds = dependecyStageTasksIds;
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

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public List<String> getUsersIds() {
        return usersIds;
    }

    public void setUsersIds(List<String> usersIds) {
        this.usersIds = usersIds;
    }
}
