package com.example.expirationtracker.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class CategoryListFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
    private FirebaseAuth mAuth;
    private DatabaseReference mCategoryReference;
    private View mView;
    private ScrollView mCategoryList;
    private LinearLayout mCategoryLayout;
    private Activity mActivity;
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;

//    private OnFragmentInteractionListener mListener;

    public CategoryListFragment() {
        // Required empty public constructor
    }


    public void showCategoryList(Query categoryQuery){

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // add each layout
                for (DataSnapshot currentSnapshot : dataSnapshot.getChildren()) {
                    final String categoryId = currentSnapshot.getKey();
                    final Category category = currentSnapshot.getValue(Category.class);
                    // linearLayout for one category content
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
                            Intent intent = new Intent(mActivity, ItemListActivity.class);
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
                    editButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mActivity, CategoryEditActivity.class);
                            intent.putExtra("categoryName",category.getName());
                            intent.putExtra("categoryFrequency",category.getFrequency());
                            intent.putExtra("categorytTime",category.getTime());
                            intent.putExtra("categoryBegin",category.getBegin());

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
                            if(itemReference != null){
                                itemReference.removeValue();
                            }

                            mCategoryLayout.removeAllViews();
                        }
                    });
                    buttonsLayout.addView(deleteButton);
                    categoryContent.addView(buttonsLayout);
                    mCategoryLayout.addView(categoryContent);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                // ...
            }

        };
        categoryQuery.addValueEventListener(postListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.e("category","onCreateView");

        // TODO: Need to load all categories from database.
        mView = inflater.inflate(R.layout.fragment_category_list, container, false);
        mActivity = getActivity();
        mAuth = FirebaseAuth.getInstance();
        mCategoryReference = FirebaseDatabase.getInstance().getReference().child("categories").child(mAuth.getUid());
        mCategoryList = (ScrollView) mView.findViewById(R.id.category_layout);
        mCategoryLayout = new LinearLayout(mActivity);
        mCategoryLayout.setPadding(10,10,10,400);
        mCategoryLayout.setOrientation(LinearLayout.VERTICAL);
        mCategoryList.addView(mCategoryLayout);

        Category newCategory1 = new Category("food","20200802","1","8:00");
        Category newCategory2 = new Category("medicine","20200802","1","8:00");
        mCategoryReference.push().setValue(newCategory1);
        mCategoryReference.push().setValue(newCategory2);
        Query categoryQuery = mCategoryReference.orderByChild("name");
        showCategoryList(categoryQuery);
        Button addButton = mView.findViewById(R.id.btn_add_category);
        if (addButton != null) {
            addButton.setOnClickListener(this);
        }
        Button logoutButton = mView.findViewById(R.id.btn_logout);
        if (logoutButton != null) {
            logoutButton.setOnClickListener(this);
        }
        return mView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_category:
                Intent intent = new Intent(mActivity, CategoryEditActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_logout:
                // [START auth_fui_signout]
                mAuth.signOut();
                Intent logoutIntent = new Intent(mActivity, MainActivity.class);
                startActivity(logoutIntent);
                break;
        }
    }
}
