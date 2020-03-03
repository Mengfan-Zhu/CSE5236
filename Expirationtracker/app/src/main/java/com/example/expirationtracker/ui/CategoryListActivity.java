package com.example.expirationtracker.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.example.expirationtracker.R;

public class CategoryListActivity extends AppCompatActivity {

//    protected int getLayoutResId() {
//        return R.layout.fragment_category_list;
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);
    }
}
