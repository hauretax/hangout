package com.example.fthangoutv03;

import android.util.Log;

import java.nio.Buffer;

public class Contact {
    private String firstName;
    private String secondName;
    private String phone;
    private Buffer image;

    Contact(){

    }
    Contact(String phone){
        phone = phone;
        Log.d("TAG", "Contacte:"+phone);
    }
    Contact(String firstName, String secondName, String phone, Buffer image){
        firstName = firstName;
        secondName = secondName;
        phone = phone;
        image = image;
    }

    void updateContacte(String firstName, String secondName, String phone, Buffer image){
        firstName = firstName;
        secondName = secondName;
        phone = phone;
        image = image;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getSecondName() {
        return secondName;
    }
}
