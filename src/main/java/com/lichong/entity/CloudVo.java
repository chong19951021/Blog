package com.lichong.entity;

import java.io.Serializable;

public class CloudVo implements Serializable {
    private Long id;
    private String text;
    private int weight;

    public CloudVo(Long id, String text, int weight) {
        this.id = id;
        this.text = text;
        this.weight = weight;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
