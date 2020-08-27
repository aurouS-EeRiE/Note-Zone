package com.example.notezone;

public class notes_push {

    private String title;
    private String description;
    private String lock;
    private String lock_key;
    private String time;
    private String date;
    private String child_name;

    public notes_push() {

    }

    public notes_push(String title, String description, String lock, String lock_key, String time, String date, String child_name) {
        this.title = title;
        this.description = description;
        this.lock = lock;
        this.lock_key = lock_key;
        this.time = time;
        this.date = date;
        this.child_name = child_name;

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

    public String getLock() {
        return lock;
    }

    public void setLock(String lock) {
        this.lock = lock;
    }

    public String getLock_key() {
        return lock_key;
    }

    public void setLock_key(String lock_key) {
        this.lock_key = lock_key;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getChild_name() {
        return child_name;
    }

    public void setChild_name(String child_name) {
        this.child_name = child_name;
    }

    @Override
    public String toString() {
        return "notes_push{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", lock='" + lock + '\'' +
                ", lock_key='" + lock_key + '\'' +
                ", time='" + time + '\'' +
                ", date='" + date + '\'' +
                ", child_name='" + child_name + '\'' +
                '}';
    }
}

