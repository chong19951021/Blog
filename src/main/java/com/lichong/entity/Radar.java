package com.lichong.entity;

public class Radar {
    private String name;
    private String max;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public Radar(String name, String max) {
        this.name = name;
        this.max = max;
    }
}
