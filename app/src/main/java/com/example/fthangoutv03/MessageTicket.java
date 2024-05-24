package com.example.fthangoutv03;

import android.content.Context;

import java.util.Date;
import java.util.UUID;

public class MessageTicket {
    private String number;
    private String name;
    private String message;
    private Date receivedDate;
    private boolean read;

    private Context context;

    public MessageTicket(String number, String message, Date receivedDate) {
        this.message = message;
        this.receivedDate = receivedDate;
        this.read = false;
        this.number = number;
        this.name = "";
    }

    public MessageTicket(String number,  String message, Date receivedDate, boolean read, String name) {
        this.name = name;
        this.message = message;
        this.receivedDate = receivedDate;
        this.read = read;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public Date getReceivedDate() {
        return receivedDate;
    }

    public Boolean isRead() {return read;}

    public String getNumber() {return number;}

    public void setRead() {this.read = true;}
}
