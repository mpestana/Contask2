package com.example.clara.contask.model;

import java.util.List;

public class Task {

    private  String title;

    private String description;

    private List<String> dependecyTasksIds;
    private long begin;
    private long end;

    private String chatId;

    private List<String> usersIds;


    private List<StageTask> stageTasks;

    public Task() {

    }

    public Task(String title, String description, List<String> dependecyTasksIds, long begin, long end, String chatId, List<String> usersIds, List<StageTask> stageTasks) {
        this.title = title;
        this.description = description;
        this.dependecyTasksIds = dependecyTasksIds;
        this.begin = begin;
        this.end = end;
        this.chatId = chatId;
        this.usersIds = usersIds;
        this.stageTasks = stageTasks;
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

    public List<String> getDependecyTasksIds() {
        return dependecyTasksIds;
    }

    public void setDependecyTasksIds(List<String> dependecyTasksIds) {
        this.dependecyTasksIds = dependecyTasksIds;
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

    public List<StageTask> getStageTasks() {
        return stageTasks;
    }

    public void setStageTasks(List<StageTask> stageTasks) {
        this.stageTasks = stageTasks;
    }
}
