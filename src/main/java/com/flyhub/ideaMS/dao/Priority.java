package com.flyhub.ideaMS.dao;

public enum Priority {
    VERY_HIGH("VERY_HIGH"),
    HIGH("HIGH"),
    MODERATE("MODERATE"),
    LOW("LOW"),
    VERY_LOW("VERY_LOW");
    private final String priority;

    private Priority(String priority) {
        this.priority = priority;
    }

    public String getPriority() {
        return priority;
    }




}
