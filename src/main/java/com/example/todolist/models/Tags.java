package com.example.todolist.models;

import java.util.Objects;

public class Tags {
    int idTags;
    String description;

    public Tags(){

    }

    public Tags(int idTags, String description) {
        this.idTags = idTags;
        this.description = description;
    }

    public int getIdTags() {
        return idTags;
    }

    public void setIdTags(int idTags) {
        this.idTags = idTags;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public String toString() {
        return description;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Tags tag = (Tags) obj;
        return idTags == tag.idTags &&
                Objects.equals(description, tag.description);
    }

}
