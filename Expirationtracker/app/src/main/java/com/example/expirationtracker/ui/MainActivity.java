package com.example.expirationtracker.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.expirationtracker.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(getString(R.string.main_act), getString(R.string.on_create));

        super.onCreate(savedInstanceState);
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_main);
        checkPermission();


    }
    public void normalCreate(){
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            Intent intent = new Intent(this,NavActivity.class);
            intent.putExtra("content", "home");
            startActivity(intent);
        }else{
            ViewPager viewPager = findViewById(R.id.viewPager);
            AuthenticationPagerAdapter pagerAdapter = new AuthenticationPagerAdapter(getSupportFragmentManager());
            pagerAdapter.addFragment(new LoginFragment());
            pagerAdapter.addFragment(new RegisterFragment());
            viewPager.setAdapter(pagerAdapter);
        }

    }

    public void checkPermission(){
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_CALENDAR);
            if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_CALENDAR},222);
            }else{
                normalCreate();
            }
        } else {
            normalCreate();

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults){
        if(grantResults[0] != 0) {
            this.finish();
        }else{
            normalCreate();
        }

    }

    protected void onStart() {
        Log.i(getString(R.string.main_act), getString(R.string.on_start));
        super.onStart();
    }

    public void onResume() {
        Log.i(getString(R.string.main_act), getString(R.string.on_resume));
        super.onResume();
    }

    public void onPause() {
        Log.i(getString(R.string.main_act), getString(R.string.on_pause));
        super.onPause();
    }

    public void onStop() {
        Log.i(getString(R.string.main_act), getString(R.string.on_stop));
        super.onStop();
    }

    class AuthenticationPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragmentList = new ArrayList<>();

        public AuthenticationPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return fragmentList.get(i);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        void addFragment(Fragment fragment) {
            fragmentList.add(fragment);
        }
    }


}
