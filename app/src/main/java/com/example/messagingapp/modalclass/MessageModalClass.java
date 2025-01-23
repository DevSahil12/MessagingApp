package com.example.messagingapp.modalclass;

public class MessageModalClass {
    String message;
    String SenderId;
    long timeStamp;

    public MessageModalClass() {
    }

    public MessageModalClass(String message, String SenderId, long timeStamp) {
        this.message = message;
        this.SenderId = SenderId;
        this.timeStamp = timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return SenderId;
    }

    public void setSenderId(String SenderId) {
        this.SenderId = SenderId;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}

