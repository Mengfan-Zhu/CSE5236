
package com.example.expirationtracker.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.expirationtracker.R;
import com.example.expirationtracker.model.Category;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

public class NavActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        Intent intent = this.getIntent();
        if (intent != null) {
            switch(intent.getStringExtra("content")){
                case "itemList":
                    openFragment(ItemListFragment.newInstance());
                    break;
                case "categoryList":
                    openFragment(CategoryListFragment.newInstance());
                    break;
                case "itemEdit":
                    openFragment(ItemEditFragment.newInstance());
                    break;
                case "categoryEdit":
                    openFragment(CategoryEditFragment.newInstance());
                    break;
                default:
                    openFragment(HomeFragment.newInstance());
                    break;
            }

        }else{
            openFragment(HomeFragment.newInstance());
        }


    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            openFragment(HomeFragment.newInstance());
                            return true;
                        case R.id.navigation_category:
                            openFragment(CategoryListFragment.newInstance());
                            return true;
                     //   case R.id.navigation_add:
                       //     openFragment(AddFragment.newInstance("", ""));
                         //   return true;
                        //TODO: do we have to have add button in nav bar?

                        case R.id.navigation_setting:
                            openFragment(SettingFragment.newInstance());
                            return true;
                    }
                    return false;
                }
            };
}
