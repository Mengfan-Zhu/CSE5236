
package com.example.expirationtracker.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
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
    String mParent= null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        Intent intent = this.getIntent();
        if (intent != null) {
            switch(intent.getStringExtra("content")){
                case "home":
                    openFragment(HomeFragment.newInstance());
                    mParent = "home";
                    break;
                case "itemList":
                    openFragment(ItemListFragment.newInstance());
                    mParent = "categoryList";
                    break;
                case "categoryList":
                    openFragment(CategoryListFragment.newInstance());
                    mParent = "home";
                    break;
                case "itemEdit":
                    openFragment(ItemEditFragment.newInstance());
                    mParent = "itemList";
                    break;
                case "itemEditFromHome":
                    openFragment(ItemEditFragment.newInstance());
                    mParent = "home";
                    break;
                case "categoryEdit":
                    openFragment(CategoryEditFragment.newInstance());
                    mParent = "categoryList";
                    break;
                case "setting":
                    openFragment(SettingFragment.newInstance());
                    mParent = "home";
                    break;
                case "nameSetting":
                    openFragment(NameSettingFragment.newInstance());
                    mParent = "setting";
                    break;
                case "passwordSetting":
                    openFragment(PasswordSettingFragment.newInstance());
                    mParent = "setting";
                    break;

//                default:
//                    openFragment(HomeFragment.newInstance());
//                    break;
            }

        }else{
            openFragment(HomeFragment.newInstance());
        }


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(mParent == "home") {
                    openFragment(HomeFragment.newInstance());
                }else if(mParent == "categoryList"){
                    openFragment(CategoryListFragment.newInstance());
                    mParent = "home";
                }else if(mParent == "itemList"){
                    openFragment(ItemListFragment.newInstance());
                    mParent = "categoryList";
                }else if(mParent == "setting"){
                    openFragment(SettingFragment.newInstance());
                    mParent = "home";
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
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
                            mParent = "home";
                            return true;
                        case R.id.navigation_category:
                            openFragment(CategoryListFragment.newInstance());
                            mParent = "home";
                            return true;
                     //   case R.id.navigation_add:
                       //     openFragment(AddFragment.newInstance("", ""));
                         //   return true;
                        //TODO: do we have to have add button in nav bar?

                        case R.id.navigation_setting:
                            openFragment(SettingFragment.newInstance());
                            mParent = "home";
                            return true;
                    }
                    return false;
                }
            };
}
