package com.example.fthangoutv03;

import java.util.Date;

public class Message {
    protected String number;
    protected String message;
    protected Date receivedDate;
    protected boolean read;
    protected String sendTo;

    public Message(String number, String message, Date receivedDate,boolean read, boolean isReceived) {
        this.message = message;
        this.receivedDate = receivedDate;
        this.read = read;
        if (isReceived) {
            this.number = number;
            this.sendTo = "";
        } else {
            this.number = "";
            this.sendTo = number;
        }
    }

    public String getMessage() {
        return message;
    }

    public Date getReceivedDate() {
        return receivedDate;
    }

    public Boolean isRead() {
        return read;
    }

    public String getSendTo() {return sendTo;}

    public String getNumber() {
        return number;
    }

    public void setRead() {
        this.read = true;
    }
}
