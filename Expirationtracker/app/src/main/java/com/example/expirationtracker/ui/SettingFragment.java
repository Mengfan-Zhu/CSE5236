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
import android.widget.TextView;

import com.example.expirationtracker.R;
import com.example.expirationtracker.model.Item;
import com.example.expirationtracker.model.User;
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
    private String mName;
    private String mUserName;
    private String mPassword;
    private Button mSaveButton;

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
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_setting, container, false);
        mActivity = getActivity();
        mAuth = FirebaseAuth.getInstance();
      //  final Intent intent = mActivity.getIntent();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        mUserReference = FirebaseDatabase.getInstance().getReference("users" + uid);
        mUserReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                User u = dataSnapshot.getValue(User.class);
                ((TextView)mView.findViewById(R.id.text_setting_username)).setText(u.getUserName());
                ((EditText)mView.findViewById(R.id.text_setting_name)).setText(u.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // save
        mSaveButton = mView.findViewById(R.id.btn_item_save);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mName = ((EditText) mView.findViewById(R.id.text_setting_name)).getText().toString();
                mUserName = ((TextView) mView.findViewById(R.id.text_setting_username)).getText().toString();
                mPassword = ((EditText) mView.findViewById(R.id.text_setting_password)).getText().toString();
                user.updatePassword(mPassword)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "User password updated.");
                                }
                            }
                        });
                //TODO: not sure if it's correct

                User i = new User(mName, mUserName);
                mUserReference.child(uid).setValue(i);

                Intent newIntent = new Intent(mActivity, SettingActivity.class);
                startActivity(newIntent);
            }
        });
        return mView;
    }

}
