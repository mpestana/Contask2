package com.example.clara.contask.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;

public class StageTasksAnswers  {


    private List<String> deliveryImagesUrls;
    private String deliveryText;
    private long end;

    public StageTasksAnswers() {
    }

    public StageTasksAnswers(List<String> deliveryImagesUrls, String deliveryText, long end) {
        this.deliveryImagesUrls = deliveryImagesUrls;
        this.deliveryText = deliveryText;
        this.end = end;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public List<String> getDeliveryImagesUrls() {
        return deliveryImagesUrls;
    }

    public void setDeliveryImagesUrls(List<String> deliveryImagesUrls) {
        this.deliveryImagesUrls = deliveryImagesUrls;
    }

    public String getDeliveryText() {
        return deliveryText;
    }

    public void setDeliveryText(String deliveryText) {
        this.deliveryText = deliveryText;
    }
}
