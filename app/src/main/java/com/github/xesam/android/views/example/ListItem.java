package com.github.xesam.android.views.example;

import java.util.List;

public class ListItem {
    private String title;
    private List<String> tags;

    public ListItem(String title, List<String> tags) {
        this.title = title;
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getTags() {
        return tags;
    }
}