package com.example.taskmanager;

public class Task {
    //    public static final int STATUS_PENDING = 1;
//    public static final int STATUS_COMPLETED = 2;
    private int id;
    private String title;
    private String description;
    private String dueDate;
    private String time;
    private String priority;
    private String status;

    public Task() {
    }

    public Task(int id, String title, String description, String dueDate, String time, String priority, String status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.time = time;
        this.priority = priority;
        this.status = status;
    }

    public Task(String title, String description, String dueDate, String time, String priority, String status) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.time = time;
        this.priority = priority;
        this.status = status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getPriority() {
        return priority;
    }
    public String getTime() {
        return time;
    }
    public String getStatus() {
        return status;
    }
}