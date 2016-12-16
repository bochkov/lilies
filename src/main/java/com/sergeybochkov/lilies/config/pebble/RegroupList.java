package com.sergeybochkov.lilies.config.pebble;

import java.util.ArrayList;
import java.util.List;

public final class RegroupList implements Comparable<RegroupList> {

    private final String grouper;
    private final List<Object> list;

    public RegroupList(String grouper) {
        this.grouper = grouper;
        this.list = new ArrayList<>();
    }

    public String grouper() {
        return grouper;
    }

    public List<Object> objects() {
        return list;
    }

    public void add(Object object) {
        list.add(object);
    }

    @Override
    public int compareTo(RegroupList o) {
        return o != null ? grouper().compareTo(o.grouper()) : 0;
    }
}
