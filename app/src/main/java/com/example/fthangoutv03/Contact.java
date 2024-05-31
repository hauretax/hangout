package com.example.fthangoutv03;

import android.util.Log;

import java.nio.Buffer;

public class Contact {
    private String firstname;
    private String lastname;
    private String phone;
    private byte[] picture;

    Contact() {

    }

    Contact(String phone) {
        phone = phone;
        Log.d("TAG", "Contacte:" + phone);
    }

    Contact(String firstName, String secondName, String phone, Buffer picture) {
        firstName = firstName;
        secondName = secondName;
        phone = phone;
        picture = picture;
    }

    void updateContacte(String firstName, String secondName, String phone, Buffer image) {
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

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getLastname() {
        return lastname;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }
}
