package com.example.expirationtracker;


import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

/**
 * A simple {@link Fragment} subclass.
 */

public class LoginFragment extends Fragment implements View.OnClickListener {
    private EditText mUsernameEditText;
    private EditText mPasswordEditText;

    private FirebaseAuth mAuth;
    private static String TAG = "Login";

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(getString(R.string.login_frg), getString(R.string.on_create_view));
        View v;
        Activity activity = getActivity();
        mAuth = FirebaseAuth.getInstance();

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

    public void onStart() {
        // TODO: check input
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        // TODO: update UI

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                loginIn();
                break;
            /*
            case R.id.exit_button:
                FragmentActivity activity = getActivity();
                if (activity != null) {
                    activity.getSupportFragmentManager().popBackStack();
                }

             */
        }
    }
    private void loginIn() {
        String username = mUsernameEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();
        Log.d(TAG, "signIn:" + username);
        // TODO: check input
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    FragmentActivity activity = getActivity();
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(activity.getApplicationContext(), "Authentication success.",
                                    Toast.LENGTH_SHORT).show();
                            // TODO: update UI
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(activity.getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            // TODO: update UI
                        }

                    }
                });
        // [END sign_in_with_email]
    }
}