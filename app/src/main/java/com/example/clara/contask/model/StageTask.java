package com.example.clara.contask.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;

public class StageTask implements Parcelable {

    private  String title;

    private String description;


    private List<String> descriptionImagesUrls;

    private long begin;

    private String chatId;

    private int requirePhoto;

    private List<String> usersIds;

    public StageTask() {
    }

    public StageTask(String title, String description,  List<String> descriptionImagesUrls, long begin, long end, String chatId, int requirePhoto, List<String> usersIds) {
        this.title = title;
        this.description = description;
        this.descriptionImagesUrls = descriptionImagesUrls;
        this.begin = begin;
        this.chatId = chatId;
        this.requirePhoto = requirePhoto;
        this.usersIds = usersIds;
    }



    public int getRequirePhoto() {
        return requirePhoto;
    }

    public void setRequirePhoto(int requirePhoto) {
        this.requirePhoto = requirePhoto;
    }

    public List<String> getDescriptionImagesUrls() {
        return descriptionImagesUrls;
    }

    public void setDescriptionImagesUrls(List<String> descriptionImagesUrls) {
        this.descriptionImagesUrls = descriptionImagesUrls;
    }


    protected StageTask(Parcel in) {
        title = in.readString();
        description = in.readString();
        begin = in.readLong();
        chatId = in.readString();
        usersIds = in.createStringArrayList();
        descriptionImagesUrls = in.createStringArrayList();
        requirePhoto=in.readInt();
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



    public long getBegin() {
        return begin;
    }

    public void setBegin(long begin) {
        this.begin = begin;
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
        dest.writeLong(begin);
        dest.writeString(chatId);
        dest.writeStringList(usersIds);
        dest.writeStringList(descriptionImagesUrls);
        dest.writeInt(requirePhoto);

    }
}
