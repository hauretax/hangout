package com.example.fthangoutv03;

import java.util.Date;

public class MessageTicket extends Message {
    private String name;

    public MessageTicket(String number, String message, Date receivedDate, boolean read, boolean isReceived) {
        super(number, message, receivedDate, read, isReceived);
        this.name = "";
    }

    public MessageTicket(String number, String message, Date receivedDate, boolean read, String name, boolean isReceived) {
        super(number, message, receivedDate, read, isReceived);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
