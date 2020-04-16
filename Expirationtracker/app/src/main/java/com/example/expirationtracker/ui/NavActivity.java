
package com.example.expirationtracker.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.expirationtracker.R;
import com.example.expirationtracker.ui.Setting.NameSettingFragment;
import com.example.expirationtracker.ui.Setting.PasswordSettingFragment;
import com.example.expirationtracker.ui.Setting.SettingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

public class NavActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigation;
    String mParent= null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);
        if (!AppStatus.getInstance(this).isOnline()) {
            Toast.makeText(this, "Network connection issue",
                    Toast.LENGTH_SHORT).show();
        }else {
            ActionBar actionBar = this.getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            bottomNavigation = findViewById(R.id.bottom_navigation);
            bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
            Intent intent = this.getIntent();
            if (intent != null) {
                switch (intent.getStringExtra("content")) {
                    case "home":
                        openFragment("HOME");
                        mParent = "home";
                        break;
                    case "itemList":
                        openFragment("ITEM_LIST");
                        mParent = "categoryList";
                        break;
                    case "categoryList":
                        openFragment("CATEGORY_LIST");
                        mParent = "home";
                        break;
                    case "itemEdit":
                        openFragment("ITEM_EDIT");
                        mParent = "itemList";
                        break;
                    case "itemEditFromHome":
                        openFragment("ITEM_EDIT");
                        mParent = "home";
                        break;
                    case "categoryEdit":
                        openFragment("CATEGORY_EDIT");
                        mParent = "categoryList";
                        break;
                    case "setting":
                        openFragment("SETTING");
                        mParent = "home";
                        break;
                    case "nameSetting":
                        openFragment("NAME_SETTING");
                        mParent = "setting";
                        break;
                    case "passwordSetting":
                        openFragment("PASSWORD_SETTING");
                        mParent = "setting";
                        break;
//                    case "nameSetting":
//                        openFragment(NameSettingFragment.newInstance());
//                        mParent = "setting";
//                        break;
//                    case "passwordSetting":
//                        openFragment(PasswordSettingFragment.newInstance());
//                        mParent = "setting";
//                        break;

//                default:
//                    openFragment(HomeFragment.newInstance());
//                    break;
                }

            } else {
                openFragment("HOME");
            }

        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(mParent == "home") {
                    openFragment("HOME");
                }else if(mParent == "categoryList"){
                    openFragment("CATEGORY_LIST");
                    mParent = "home";
                }else if(mParent == "itemList"){
                    openFragment("ITEM_LIST");
                    mParent = "categoryList";
                }else if(mParent == "setting"){
//                    openFragment(SettingFragment.newInstance());
                    openFragment("SETTING");
                    mParent = "home";
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void openFragment(String tag) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment == null) {
            switch (tag){
                case "HOME":
                    fragment = HomeFragment.newInstance();
                    break;
                case "CATEGORY_LIST":
                    fragment = CategoryListFragment.newInstance();
                    break;
                case "CATEGORY_EDIT":
                    fragment = CategoryEditFragment.newInstance();
                    break;
                case "ITEM_LIST":
                    fragment = ItemListFragment.newInstance();
                    break;
                case "ITEM_EDIT":
                    fragment = ItemEditFragment.newInstance();
                    break;
                case "SETTING":
                    fragment = SettingFragment.newInstance();
                    break;
                case "NAME_SETTING":
                    fragment = NameSettingFragment.newInstance();
                    break;
                case "PASSWORD_SETTING":
                    fragment = PasswordSettingFragment.newInstance();
                    break;
            }
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment, tag);
        transaction.addToBackStack(null);
        transaction.commit();
    }

//    public void openFragment(Fragment fragment) {
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.container, fragment);
//        transaction.addToBackStack(null);
//        transaction.commit();
//    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            openFragment("HOME");
                            mParent = "home";
                            return true;
                        case R.id.navigation_category:
                            openFragment("CATEGORY_LIST");
                            mParent = "home";
                            return true;
                        case R.id.navigation_setting:
                            openFragment("SETTING");
                            mParent = "home";
                            return true;
                    }
                    return false;
                }
            };
}
