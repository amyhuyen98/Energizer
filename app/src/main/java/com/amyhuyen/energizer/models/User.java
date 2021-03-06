package com.amyhuyen.energizer.models;

import org.parceler.Parcel;

@Parcel
public class User {

    // user fields
    String email;
    String name;
    String phone;
    String userID;
    String userType;
    String latLong;
    String address;

    public User () { }

    public User (String email, String name, String phone, String userID, String userType, String latLong, String address) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.userID = userID;
        this.userType = userType;
        this.latLong = latLong;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getUserID() {
        return userID;
    }

    public String getUserType() {
        return userType;
    }

    public String getLatLong() { return latLong; }

    public String getAddress() { return address; }

    @Override
    public String toString() {
        return "User{" +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", userID='" + userID + '\'' +
                ", userType='" + userType + '\'' +
                '}';
    }
}
