package com.example.expirationtracker.model;

public class Item {
    private String mName;
    private String mExpirationDate;
    private int mQuantity;
    private String mDescription;
    private int mDaysToExpiry;

    public Item(){
        mName = "";
        mExpirationDate = "";
        mQuantity = 0;
        mDescription = "";
        mDaysToExpiry = 0;
    }
    public Item(String name, String expirationDate, int quantity, int daysToExpiry, String description) {
        mName = name;
        mExpirationDate = expirationDate;
        mQuantity = quantity;
        mDescription = description;
        mDaysToExpiry = daysToExpiry;
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

    public int getDaysToExpiry() {
        return mDaysToExpiry;
    }

    public void setDaysToExpiry(int daysToExpiry) {
        mDaysToExpiry = daysToExpiry;
    }
}
