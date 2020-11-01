package com.lichong.entity;

public class TypeVo {
    private int size;
    private Type type;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public TypeVo(int size, Type type) {
        this.size = size;
        this.type = type;
    }




}
