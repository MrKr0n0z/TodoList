package com.example.todolist.models;

public class tasks_tags {
    int idTasksTag;
    int idTasks;
    int idTag;

    public tasks_tags(){

    }

    public tasks_tags(int idTasksTag, int idTasks, int idTag) {
        this.idTasksTag = idTasksTag;
        this.idTasks = idTasks;
        this.idTag = idTag;
    }

    public int getIdTasksTag() {
        return idTasksTag;
    }

    public void setIdTasksTag(int idTasksTag) {
        this.idTasksTag = idTasksTag;
    }

    public int getIdTasks() {
        return idTasks;
    }

    public void setIdTasks(int idTasks) {
        this.idTasks = idTasks;
    }

    public int getIdTag() {
        return idTag;
    }

    public void setIdTag(int idTag) {
        this.idTag = idTag;
    }
}
