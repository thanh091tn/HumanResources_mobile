package com.example.swd_project.Model;

import java.io.Serializable;

public class CourseDTO implements Serializable {
    private String id;
    private String name;
    private Double time;
    private boolean status;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getTime() {
        return time;
    }

    public void setTime(Double time) {
        this.time = time;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public CourseDTO(String id, String name, Double time, boolean status) {
        this.id = id;
        this.name = name;
        this.time = time;
        this.status = status;
    }
}
