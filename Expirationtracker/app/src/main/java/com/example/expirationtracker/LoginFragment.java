package com.example.expirationtracker;


import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {
    private EditText mUsernameEditText;
    private EditText mPasswordEditText;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(getString(R.string.login_frg), getString(R.string.on_create_view));
        View v;
        Activity activity = getActivity();

        if (activity != null) {
            int rotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
            if (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) {
                v = inflater.inflate(R.layout.fragment_login, container, false);
            } else {
                v = inflater.inflate(R.layout.fragment_login, container, false);
            }
        }
        else {
            v = inflater.inflate(R.layout.fragment_login, container, false);
        }

        mUsernameEditText = v.findViewById(R.id.et_email);
        mPasswordEditText = v.findViewById(R.id.et_password);

        Button loginButton = v.findViewById(R.id.btn_login);
        if (loginButton != null) {
            loginButton.setOnClickListener(this);
        }
        return v;
        // Inflate the layout for this fragment
    }

    @Override
    public void onClick(View view) {
        
    }

}