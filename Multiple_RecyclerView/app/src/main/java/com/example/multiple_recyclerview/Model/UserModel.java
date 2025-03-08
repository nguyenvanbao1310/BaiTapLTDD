package com.example.multiple_recyclerview.Model;

import java.io.Serializable;

public class UserModel implements Serializable
{
    private String name;
    private String address;

    public UserModel(String address, String name) {
        this.address = address;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
