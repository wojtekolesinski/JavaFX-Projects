package com.example.tpo6.dto;

public class MessageDto {
    private String user;
    private String message;
    private long timestamp;

    public MessageDto(String user, String message, long timestamp) {
        this.user = user;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
