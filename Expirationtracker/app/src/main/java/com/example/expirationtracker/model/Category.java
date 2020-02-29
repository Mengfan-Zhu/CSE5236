package com.example.expirationtracker.model;

import java.util.HashSet;
import java.util.Set;

public class Category {
    private String mName;
    private String mBegin;
    private String mFrequency;
    private String mTime;

    public Category(String name, String begin, String frequency, String time) {
        mName = name;
        mBegin = begin;
        mFrequency = frequency;
        mTime = time;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }


}
