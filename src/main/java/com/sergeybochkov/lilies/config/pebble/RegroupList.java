package com.sergeybochkov.lilies.config.pebble;

import java.util.ArrayList;
import java.util.List;

public class RegroupList {

    private String grouper;
    private List<Object> list = new ArrayList<>();

    public String getGrouper() {
        return grouper;
    }

    public void setGrouper(String grouper) {
        this.grouper = grouper;
    }

    public List<Object> getList() {
        return list;
    }

    public void setList(List<Object> list) {
        this.list = list;
    }

    public void addToList(Object object) {
        list.add(object);
    }
}
