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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.expirationtracker.R;
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
 * Use the {@link PasswordSettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PasswordSettingFragment extends Fragment {
    private Activity mActivity;
    private FirebaseAuth mAuth;
    private DatabaseReference mUserReference;
    private View mView;
    private String mOldPassword;
    private String mNewPassword;
    private String mConfirmPassword;
    private Button mSaveButton;
    public PasswordSettingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PasswordSettingFragment.
     */
    public static PasswordSettingFragment newInstance() {
        return new PasswordSettingFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_name_setting, container, false);
        mActivity = getActivity();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        mUserReference = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getUid());
        mUserReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                User u = dataSnapshot.getValue(User.class);
//                ((TextView)mView.findViewById(R.id.text_setting_username)).setText(u.getUserName());
                ((EditText)mView.findViewById(R.id.name_setting_text)).setText(u.getName());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mSaveButton = mView.findViewById(R.id.btn_name_setting_save);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOldPassword = ((EditText) mView.findViewById(R.id.new_password)).getText().toString();
                mNewPassword = ((EditText) mView.findViewById(R.id.old_password)).getText().toString();
                mConfirmPassword = ((EditText) mView.findViewById(R.id.confirm)).getText().toString();
                if( mConfirmPassword.equals(mNewPassword) && mNewPassword.length() > 0 && !mOldPassword.equals(mNewPassword)){
                    mAuth.getCurrentUser().updatePassword(mNewPassword)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(mActivity.getApplicationContext(), "Password update successfully. ",
                                                Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(mActivity.getApplicationContext(), "Password update failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                /*
                else if(!mOldPassword.equals()){
                    Toast.makeText(mActivity.getApplicationContext(), "Password does not correct.",
                            Toast.LENGTH_SHORT).show();
                } */
                else if(mNewPassword.length() == 0){
                    Toast.makeText(mActivity.getApplicationContext(), "Password cannot be empty. ",
                            Toast.LENGTH_SHORT).show();
                }else if(!mNewPassword.equals(mConfirmPassword)){
                    Toast.makeText(mActivity.getApplicationContext(), "New Password does not match.",
                            Toast.LENGTH_SHORT).show();
                }else if(mOldPassword.equals(mNewPassword)){
                    Toast.makeText(mActivity.getApplicationContext(), "Password should not be same.",
                            Toast.LENGTH_SHORT).show();
                }
                Intent newIntent = new Intent(mActivity, NavActivity.class);
                newIntent.putExtra("content", "passwordSetting");
                startActivity(newIntent);
            }
        });

        // Inflate the layout for this fragment
        return mView;
    }

}
