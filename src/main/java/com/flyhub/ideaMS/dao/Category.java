package com.flyhub.ideaMS.dao;

public enum Category {
    TECHNOLOGY("TECHNOLOGY"),
    AGRICULTURE("AGRICULTURE"),
    OTHER("OTHER");
    private final String category;

    private Category(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
}
