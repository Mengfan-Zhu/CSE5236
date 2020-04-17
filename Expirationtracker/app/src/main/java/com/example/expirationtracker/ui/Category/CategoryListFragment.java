package com.example.expirationtracker.ui.Category;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.util.TypedValue;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.view.LayoutInflater;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.example.expirationtracker.R;
import com.example.expirationtracker.model.Category;

import com.example.expirationtracker.model.Item;
import com.example.expirationtracker.ui.NavActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;


public class CategoryListFragment extends Fragment implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private DatabaseReference mCategoryReference;
    private ValueEventListener mCategoryListener;
    private Query mCategoryQuery;
    private LinearLayout mCategoryLayout;
    private Activity mActivity;
    private String TAG = "Category List Fragment";

    public CategoryListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment CategoryListFragment.
     */
    public static CategoryListFragment newInstance() {
        return new CategoryListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        mActivity = getActivity();
        View view = inflater.inflate(R.layout.fragment_category_list, container, false);
        // set up firebase reference
        mAuth = FirebaseAuth.getInstance();
        mCategoryReference = FirebaseDatabase.getInstance().getReference().child("categories").child(Objects.requireNonNull(mAuth.getUid()));
        // set up layouts
        ScrollView categoryList = (ScrollView) view.findViewById(R.id.category_layout);
        mCategoryLayout = new LinearLayout(mActivity);
        mCategoryLayout.setPadding(10,10,10,400);
        mCategoryLayout.setOrientation(LinearLayout.VERTICAL);
        categoryList.addView(mCategoryLayout);
        // set up buttons
        Button addButton = view.findViewById(R.id.btn_add_category);
        addButton.setOnClickListener(this);
        return view;
    }
    @Override
    public void onStart(){
        super.onStart();
        mCategoryQuery = mCategoryReference.orderByChild("name");
        showCategoryList(mCategoryQuery);
    }
    public void showCategoryList(Query categoryQuery){
        mCategoryListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // clear previous view
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mCategoryLayout.removeAllViews();
                    }
                });
                // add each layout
                for (DataSnapshot currentSnapshot : dataSnapshot.getChildren()) {
                    final String categoryId = currentSnapshot.getKey();
                    final Category category = currentSnapshot.getValue(Category.class);
                    // linearLayout for one category content
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            LinearLayout categoryContent = new LinearLayout(mActivity);
                            categoryContent.setOrientation(LinearLayout.VERTICAL);
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            layoutParams.setMargins(10, 10, 10, 10);
                            categoryContent.setLayoutParams(layoutParams);
                            categoryContent.setDividerPadding(10);
                            categoryContent.setBackgroundResource(R.drawable.bg_item);
                            categoryContent.setClickable(true);
                            categoryContent.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(mActivity, NavActivity.class);
                                    intent.putExtra("content", "ITEM_LIST");
                                    intent.putExtra("categoryId",categoryId);
                                    startActivity(intent);
                                }
                            });
                            // TextView for name
                            TextView name = new TextView(mActivity);
                            name.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                            name.setText(category.getName());
                            name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
                            categoryContent.addView(name);
                            // TextView for contents
                            TextView contents = new TextView(mActivity);
                            contents.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                            contents.setText("Begin: " + category.getBegin() + "\nFrequency: " + category.getFrequency() + "\nTime: " + category.getTime());
                            categoryContent.addView(contents);
                            // linearLayout for buttons
                            LinearLayout buttonsLayout = new LinearLayout(mActivity);
                            buttonsLayout.setOrientation(LinearLayout.HORIZONTAL);
                            buttonsLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            // edit button
                            Button editButton = new Button(mActivity);
                            editButton.setText("Edit");
                            editButton.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            editButton.setOnClickListener(CategoryListFragment.this);
                            editButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(mActivity, NavActivity.class);
                                    intent.putExtra("content", "CATEGORY_EDIT");
                                    intent.putExtra("categoryName",category.getName());
                                    intent.putExtra("categoryFrequency",category.getFrequency());
                                    intent.putExtra("categoryTime",category.getTime());
                                    intent.putExtra("categoryBegin",category.getBegin());
                                    intent.putExtra("categoryId",categoryId);
                                    intent.putExtra("operation","Edit");
                                    startActivity(intent);
                                }
                            });
                            buttonsLayout.addView(editButton);
                            // delete button
                            Button deleteButton = new Button(mActivity);
                            deleteButton.setText("Delete");
                            deleteButton.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            deleteButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mCategoryReference.child(categoryId).removeValue();
                                    DatabaseReference itemReference = FirebaseDatabase.getInstance().getReference().child("items").child(mAuth.getUid()).child(categoryId);
                                    itemReference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for (DataSnapshot currentSnapshot : dataSnapshot.getChildren()) {
                                                Item item = currentSnapshot.getValue(Item.class);
                                                ContentResolver cr = mActivity.getContentResolver();
                                                Uri deleteEvent = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, item.getEventId());
                                                cr.delete(deleteEvent, null, null);
                                            }

                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                    itemReference.removeValue();
                                }
                            });
                            buttonsLayout.addView(deleteButton);
                            categoryContent.addView(buttonsLayout);
                            mCategoryLayout.addView(categoryContent);
                        }
                    });
//                    return;
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "FAIL TO UPDATE");
            }

        };
        categoryQuery.addValueEventListener(mCategoryListener);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_category:
                Intent intent = new Intent(mActivity, NavActivity.class);
                intent.putExtra("content", "CATEGORY_EDIT");
                intent.putExtra("operation","Add");
                startActivity(intent);
                break;
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        // Remove post value event listener
        if (mCategoryListener != null) {
            mCategoryQuery.removeEventListener(mCategoryListener);
            mCategoryListener = null;
        }
        Runtime.getRuntime().gc();
    }
}
