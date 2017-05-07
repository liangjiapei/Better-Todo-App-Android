package com.liangjiapei.bettertodoapp;

/**
 * Created by liangjiapei on 5/6/17.
 */

import java.io.Serializable;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class TodoItem implements Serializable {
    private String description;

    private String createdAt;

    private long timestamp;

    private boolean isCompleted;

    public TodoItem(String description) {
        this.description = description;
        this.createdAt = createdAt;

        this.timestamp = new Date().getTime();
        this.createdAt = getDate(this.timestamp);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    private String getDate(long timeStamp){

        try{
            DateFormat sdf = new SimpleDateFormat("MMM dd, yyyy @ hh:mm:ss a");
            Date netDate = (new Date(timeStamp));
            return sdf.format(netDate);
        }
        catch(Exception ex){
            return "xx";
        }
    }
}
