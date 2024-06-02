package com.example.fthangoutv03;

import java.nio.Buffer;

public class Contact {
    private String firstname;
    private String lastname;
    private String phone;
    private String picturePath;

    Contact() {

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

    public String getPicturePath() {
        return picturePath;
    }
    public void setPicturePath( String picturePath) {
        this.picturePath = picturePath;
    }
}
