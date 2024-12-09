package com.example.mcfinalproject;

public class Task {

    private int id;
    private String title;
    private String desc;
    private int priority;
    private String dueDate;

    public Task(int id, String title, String desc, int priority, String dueDate){
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.priority = priority;
        this.dueDate = dueDate;
    }

    public int getId() {
        return  id;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public int getPriority() {
        return priority;
    }

    public String getDueDate() {
        return dueDate;
    }
}
