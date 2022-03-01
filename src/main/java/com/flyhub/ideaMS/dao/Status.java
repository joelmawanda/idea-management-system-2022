package com.flyhub.ideaMS.dao;

public enum Status {
    INITIATED("INITIATED"),
    PENDING("PENDING"),
    SUCCESSFUL("SUCCESSFUL"),
    UNKNOWN_STATE("UNKNOWN_STATE"),
    FAILED("FAILED");

    private final String status;

    private Status(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }


}
