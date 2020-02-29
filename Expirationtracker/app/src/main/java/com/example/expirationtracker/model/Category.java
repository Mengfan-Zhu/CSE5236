package com.example.expirationtracker.model;

import java.util.HashSet;
import java.util.Set;

public class Category {
    private String mName;
    private String mUserName;
    private Set<Item> mItemSet;

    public Category(String name, String userName) {
        mName = name;
        mUserName = userName;
        mItemSet = new HashSet<>();
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public Set<Item> getItemSet() {
        return mItemSet;
    }

    public void setItemSet(Set<Item> itemSet) {
        mItemSet = itemSet;
    }
}
