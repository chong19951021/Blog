package com.lichong.queryvo;

import java.util.List;

public class PageVo {
    public PageVo() {
    }

    public PageVo(List list) {
        this.list = list;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    private List list;
}
