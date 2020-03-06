package com.example.expirationtracker.model;

public class Item {
    private String mName;
    private String mExpirationDate;
    private int mQuantity;
    private String mDescription;

    public Item(){
        mName = "";
        mExpirationDate = "";
        mQuantity = 0;
        mDescription = "";
    }
    public Item(String name, String expirationDate, int quantity, String description) {
        mName = name;
        mExpirationDate = expirationDate;
        mQuantity = quantity;
        mDescription = description;
    }


    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getExpirationDate() {
        return mExpirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        mExpirationDate = expirationDate;
    }

    public int getQuantity() {
        return mQuantity;
    }

    public void setQuantity(int quantity) {
        mQuantity = quantity;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

}
