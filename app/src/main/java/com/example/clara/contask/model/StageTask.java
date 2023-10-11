package com.example.clara.contask.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;

public class StageTask implements Parcelable {

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

    protected StageTask(Parcel in) {
        title = in.readString();
        description = in.readString();
        dependecyStageTasksIds = in.createStringArrayList();
        begin = in.readLong();
        end = in.readLong();
        chatId = in.readString();
        usersIds = in.createStringArrayList();
    }

    public static final Creator<StageTask> CREATOR = new Creator<StageTask>() {
        @Override
        public StageTask createFromParcel(Parcel in) {
            return new StageTask(in);
        }

        @Override
        public StageTask[] newArray(int size) {
            return new StageTask[size];
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeStringList(dependecyStageTasksIds);
        dest.writeLong(begin);
        dest.writeLong(end);
        dest.writeString(chatId);
        dest.writeStringList(usersIds);
    }
}
