package com.example.expirationtracker.model;

import java.util.Date;

public class Item {

    private String mName;
    private int mId;
    private Date mExpirationDate;
    private int mQuantity;
    private String mDescription;
    private int mDaysToExpiry;

    public Item(String name, int id, Date expirationDate, int quantity, String description, int daysToExpiry) {

        mName = name;
        mId = id;
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

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public Date getExpirationDate() {
        return mExpirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
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
