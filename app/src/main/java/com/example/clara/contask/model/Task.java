package com.example.clara.contask.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;

public class Task implements Parcelable {

    private  String title;

    private String description;

    private List<String> dependecyTasksIds;
    private long begin;
    private long end;

    private String chatId;

    private List<String> usersIds;


    private List<StageTask> stageTasks;
    private List<String> descriptionImagesUrls;

    public Task() {

    }

    public Task(String title, String description, List<String> dependecyTasksIds, long begin, long end, String chatId, List<String> usersIds, List<StageTask> stageTasks, List<String> descriptionImagesUrls) {
        this.title = title;
        this.description = description;
        this.dependecyTasksIds = dependecyTasksIds;
        this.begin = begin;
        this.end = end;
        this.chatId = chatId;
        this.usersIds = usersIds;
        this.stageTasks = stageTasks;
        this.descriptionImagesUrls = descriptionImagesUrls;
    }

    public List<String> getDescriptionImagesUrls() {
        return descriptionImagesUrls;
    }

    public void setDescriptionImagesUrls(List<String> descriptionImagesUrls) {
        this.descriptionImagesUrls = descriptionImagesUrls;
    }

    protected Task(Parcel in) {
        title = in.readString();
        description = in.readString();
        dependecyTasksIds = in.createStringArrayList();
        begin = in.readLong();
        end = in.readLong();
        chatId = in.readString();
        usersIds = in.createStringArrayList();
        descriptionImagesUrls= in.createStringArrayList();
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeStringList(dependecyTasksIds);
        dest.writeLong(begin);
        dest.writeLong(end);
        dest.writeString(chatId);
        dest.writeStringList(usersIds);
        dest.writeStringList(descriptionImagesUrls);
    }
}
