package com.example.datenbankv5;

public class Task {

    private int id;
    private String task;
    private String description;
    private Priority priority;

    public Task(int id, String task, String description, Priority priority) {
        this.id = id;
        this.task = task;
        this.description = description;
        this.priority = priority; //
    }

    public int getId() {
        return id;
    }

    public String getTask() {
        return task;
    }

    public String getDescription() {
        return description;
    }
    
    public Priority getPriority() {
        return priority;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", task='" + task + '\'' +
                ", description='" + description + '\'' +
                ", priority=" + priority +
                '}';
    }


}
