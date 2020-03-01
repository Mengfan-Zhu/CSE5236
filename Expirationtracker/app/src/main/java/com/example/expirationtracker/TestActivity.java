package com.example.expirationtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.example.expirationtracker.model.Category;
import com.google.firebase.database.Query;
import java.lang.Object;
import java.util.HashMap;

public class TestActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference mCategoryReference;
    private TextView mResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
//        Category newCategory1 = new Category("food","20200802","1","8:00");
//        Category newCategory2 = new Category("medicine","20200802","1","8:00");
//        mDatabase.child("categories").child(mAuth.getUid()).push().setValue(newCategory1);
//        mDatabase.child("categories").child(mAuth.getUid()).push().setValue(newCategory2);
        mCategoryReference = mDatabase.child("categories").child(mAuth.getUid());
        Query categoryQuery = mCategoryReference.orderByChild("name");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
//                Category category = dataSnapshot.child("categories").child(mAuth.getUid()).getValue(Category.class);
//                String result = dataSnapshot.getValue().toString();
//                Object datas = dataSnapshot.getValue();
                mResult = findViewById(R.id.search_result);
                String result = "";
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    result = result + postSnapshot.getValue(Category.class).getName() + "\n";
                }
                mResult.setText(result);
                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                // ...
            }
        };
        categoryQuery.addValueEventListener(postListener);


    }
}
