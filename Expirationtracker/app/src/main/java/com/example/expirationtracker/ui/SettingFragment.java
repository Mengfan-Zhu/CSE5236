package com.example.expirationtracker.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.expirationtracker.R;
import com.example.expirationtracker.model.Item;
import com.example.expirationtracker.model.User;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment {
    private FirebaseAuth mAuth;
    private Activity mActivity;
    private DatabaseReference mUserReference;
    private View mView;
    private Button mName;
    private Button mPassword;
    private Button mLogout;


    public SettingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment SettingFragment.
     */
    public static SettingFragment newInstance() {
        return new SettingFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_setting, container, false);
        mActivity = getActivity();
        mAuth = FirebaseAuth.getInstance();
        mName = mView.findViewById(R.id.name_setting);
        mPassword = mView.findViewById(R.id.password_setting);
        mLogout = mView.findViewById(R.id.btn_logout);
        mName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent newIntent = new Intent(mActivity, NavActivity.class);
                newIntent.putExtra("content", "nameSetting");
                startActivity(newIntent);
            }
        });

        mPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent newIntent = new Intent(mActivity, NavActivity.class);
                newIntent.putExtra("content", "passwordSetting");
                startActivity(newIntent);
            }
        });

        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent logoutIntent = new Intent(mActivity, MainActivity.class);
                startActivity(logoutIntent);
            }
        });




        return mView;
    }


}
