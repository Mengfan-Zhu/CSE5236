
package com.example.expirationtracker.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.example.expirationtracker.AppStatus;
import com.example.expirationtracker.R;
import com.example.expirationtracker.ui.Category.CategoryEditFragment;
import com.example.expirationtracker.ui.Category.CategoryListFragment;
import com.example.expirationtracker.ui.Item.HomeFragment;
import com.example.expirationtracker.ui.Item.ItemEditFragment;
import com.example.expirationtracker.ui.Item.ItemListFragment;
import com.example.expirationtracker.ui.Setting.NameSettingFragment;
import com.example.expirationtracker.ui.Setting.PasswordSettingFragment;
import com.example.expirationtracker.ui.Setting.SettingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Objects;

public class NavActivity extends AppCompatActivity {
    private BottomNavigationView mBottomNavigation;
    private String mParent= null;
    private BottomNavigationView.OnNavigationItemSelectedListener mNavigationListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);
    }

    @Override
    public void onStart(){
        super.onStart();
        mNavigationListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        openFragment("HOME");
                        mParent = "HOME";
                        return true;
                    case R.id.navigation_category:
                        openFragment("CATEGORY_LIST");
                        mParent = "HOME";
                        return true;
                    case R.id.navigation_setting:
                        openFragment("SETTING");
                        mParent = "HOME";
                        return true;
                }
                return false;
            }
        };
        if (!AppStatus.getInstance(this).isOnline()) {
            Toast.makeText(this, "Network connection issue",
                    Toast.LENGTH_SHORT).show();
        }else {
            ActionBar actionBar = this.getSupportActionBar();
            Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);
            mBottomNavigation = findViewById(R.id.bottom_navigation);
            mBottomNavigation.setOnNavigationItemSelectedListener(mNavigationListener);
            Intent intent = this.getIntent();
            if (intent != null) {
                String content = intent.getStringExtra("content");
                openFragment(content);
                switch (Objects.requireNonNull(content)) {
                    case "HOME": case "CATEGORY_LIST":case "ITEM_EDIT_FROM_EDIT":case "SETTING":
                        mParent = "HOME";
                        break;
                    case "ITEM_LIST":case "CATEGORY_EDIT":
                        mParent = "CATEGORY_LIST";
                        break;
                    case "ITEM_EDIT":
                        mParent = "ITEM_LIST";
                        break;
                    case "NAME_SETTING": case "PASSWORD_SETTING":
                        mParent = "SETTING";
                        break;
                }

            } else {
                openFragment("HOME");
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            openFragment(mParent);
            if (mParent.equals("ITEM_LIST")) {
                mParent = "CATEGORY_LIST";
            } else {
                mParent = "HOME";
            }
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
                case "ITEM_EDIT": case "ITEM_EDIT_FROM_HOME":
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
        transaction.replace(R.id.container, Objects.requireNonNull(fragment), tag);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    public void onStop() {
        super.onStop();
        mBottomNavigation.setOnNavigationItemReselectedListener(null);
        mNavigationListener = null;
        mBottomNavigation = null;
        Runtime.getRuntime().gc();
    }
}
