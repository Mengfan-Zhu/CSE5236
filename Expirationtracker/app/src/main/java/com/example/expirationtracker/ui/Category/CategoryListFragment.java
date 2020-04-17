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

import java.util.ArrayList;
import java.util.Objects;


public class CategoryListFragment extends Fragment implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private DatabaseReference mCategoryReference;
    private ValueEventListener mCategoryListener;
    private Query mCategoryQuery;
    private LinearLayout mCategoryLayout;
    private Activity mActivity;
    private String TAG = "Category List Fragment";
    private ArrayList<Button> mEditButtons = new ArrayList<Button>();
    private ArrayList<View.OnClickListener> mEditListeners = new ArrayList<View.OnClickListener>();
    private View view;
    private Runnable r;

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
        view = inflater.inflate(R.layout.fragment_category_list, container, false);
        // set up firebase reference
        mAuth = FirebaseAuth.getInstance();

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
        mCategoryReference = FirebaseDatabase.getInstance().getReference().child("categories").child(Objects.requireNonNull(mAuth.getUid()));
        mCategoryQuery = FirebaseDatabase.getInstance().getReference().child("categories").child(Objects.requireNonNull(mAuth.getUid())).orderByChild("name");
        showCategoryList();
    }
    public void showCategoryList(){
        mCategoryListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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
                            LinearLayout categoryContent = new LinearLayout(view.getContext());
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
                            TextView name = new TextView(view.getContext());
                            name.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                            name.setText(Objects.requireNonNull(category).getName());
                            name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
                            categoryContent.addView(name);
                            // TextView for contents
                            TextView contents = new TextView(view.getContext());
                            contents.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                            contents.setText(String.format("Begin: %s\nFrequency: %s\nTime: %s", category.getBegin(), category.getFrequency(), category.getTime()));
                            categoryContent.addView(contents);
                            // linearLayout for buttons
                            LinearLayout buttonsLayout = new LinearLayout(view.getContext());
                            buttonsLayout.setOrientation(LinearLayout.HORIZONTAL);
                            buttonsLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            // edit button
                            Button editButton = new Button(view.getContext());
                            editButton.setText(R.string.edit);
                            editButton.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
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
                            Button deleteButton = new Button(view.getContext());
                            deleteButton.setText(R.string.delete);
                            deleteButton.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            deleteButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mCategoryReference.child(Objects.requireNonNull(categoryId)).removeValue();
                                    DatabaseReference itemReference = FirebaseDatabase.getInstance().getReference().child("items").child(Objects.requireNonNull(mAuth.getUid())).child(categoryId);
                                    itemReference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for (DataSnapshot currentSnapshot : dataSnapshot.getChildren()) {
                                                Item item = currentSnapshot.getValue(Item.class);
                                                ContentResolver cr = mActivity.getContentResolver();
                                                Uri deleteEvent = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, Objects.requireNonNull(item).getEventId());
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
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "FAIL TO UPDATE");
            }

        };
        mCategoryQuery.addValueEventListener(mCategoryListener);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_add_category) {
            Intent intent = new Intent(mActivity, NavActivity.class);
            intent.putExtra("content", "CATEGORY_EDIT");
            intent.putExtra("operation", "Add");
            startActivity(intent);
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
