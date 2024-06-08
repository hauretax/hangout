package com.example.fthangoutv03;

import java.util.Date;

public class MessageTicket extends Message {

    public MessageTicket(String number, String message, Date receivedDate, boolean read, boolean isReceived) {
        super(number, message, receivedDate, read, isReceived);
    }

}
