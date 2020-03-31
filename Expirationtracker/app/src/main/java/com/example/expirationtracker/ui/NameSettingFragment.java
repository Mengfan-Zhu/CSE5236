package com.example.expirationtracker.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.expirationtracker.R;
import com.example.expirationtracker.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NameSettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NameSettingFragment extends Fragment {
    private Activity mActivity;
    private FirebaseAuth mAuth;
    private DatabaseReference mUserReference;
    private View mView;
    private String mName;
    private Button mSaveButton;

    public NameSettingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NameSettingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NameSettingFragment newInstance() {
        return new NameSettingFragment();
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
                ((EditText) mView.findViewById((R.id.name_setting_text))).setText(u.getName());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mSaveButton = mView.findViewById(R.id.btn_name_setting_save);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mName = ((TextView) mView.findViewById(R.id.name_setting_text)).getText().toString();
                mUserReference.child("name").setValue(mName);
                Intent newIntent = new Intent(mActivity, NavActivity.class);
                newIntent.putExtra("content", "nameSetting");
                startActivity(newIntent);
            }
        });

        // Inflate the layout for this fragment
        return mView;
    }

}
