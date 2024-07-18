package com.example.mytime;

public class Task {
    private int id;
    private String description;
    private String date;
    private String time;

    public Task(int id, String description, String date, String time) {
        this.id = id;
        this.description = description;
        this.date = date;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}


