package com.example.expirationtracker.model;

public class User {
    private String mUserName;
    private String mPassword;
    private String mName;

    public User(String userName, String password, String name) {
        mUserName = userName;
        mPassword = password;
        mName = name;
    }

    public String getUserName() {
        return mUserName;
    }

    public String getPassword() {
        return mPassword;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

}
