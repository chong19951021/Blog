package com.lichong.entity;

import java.util.ArrayList;

public class RadarVo {
    private ArrayList list ;
    private ArrayList list2 ;

    public ArrayList getList() {
        return list;
    }

    public void setList(ArrayList list) {
        this.list = list;
    }

    public ArrayList getList2() {
        return list2;
    }

    public void setList2(ArrayList list2) {
        this.list2 = list2;
    }

    public RadarVo(ArrayList list, ArrayList list2) {
        this.list = list;
        this.list2 = list2;
    }
}
