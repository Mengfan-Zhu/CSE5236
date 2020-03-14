package com.example.expirationtracker.ui;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.expirationtracker.R;
import com.example.expirationtracker.model.Item;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class ItemListFragment extends Fragment implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private DatabaseReference mItemReference;
    private View mView;
    private String mCategoryId;
    private LinearLayout mItemLayout;
    private Activity mActivity;
    String TAG = "Item List Fragment";
    public ItemListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mActivity = getActivity();
        Log.e("List","onCreateView");
        // TODO: Need to load all categories from database.
        mView = inflater.inflate(R.layout.fragment_item_list, container, false);
        mAuth = FirebaseAuth.getInstance();
        Intent intent = mActivity.getIntent();
        if (intent != null) {
            mCategoryId = intent.getStringExtra("categoryId");
        }
        mItemReference = FirebaseDatabase.getInstance().getReference().child("items").child(mAuth.getUid()).child(mCategoryId);
        Query itemQuery = mItemReference.orderByChild("name");
        ScrollView itemList = (ScrollView) mView.findViewById(R.id.item_layout);
        itemList.setFillViewport(true);
        mItemLayout = new LinearLayout(mActivity);
        mItemLayout.setPadding(10,10,10,400);
        mItemLayout.setOrientation(LinearLayout.VERTICAL);
        itemList.addView(mItemLayout);
        ValueEventListener itemListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // add wrap layout
                // add each layout
                mItemLayout.removeAllViews();
                for (DataSnapshot currentSnapshot : dataSnapshot.getChildren()) {
                    final String itemId = currentSnapshot.getKey();
                    final Item item = currentSnapshot.getValue(Item.class);
                    // linearLayout for one item content
                    LinearLayout itemContent = new LinearLayout(mActivity);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(10, 10, 10, 10);
                    itemContent.setOrientation(LinearLayout.VERTICAL);
                    itemContent.setLayoutParams(layoutParams);
                    itemContent.setDividerPadding(10);
                    itemContent.setBackgroundResource(R.drawable.bg_item);
                    itemContent.setClickable(true);
                    itemContent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //TODO
                        }
                    });
                    // TextView for name
                    TextView name = new TextView(mActivity);
                    name.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    name.setText(item.getName());
                    name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
                    itemContent.addView(name);
                    // TextView for contents
                    TextView contents = new TextView(mActivity);
                    contents.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    contents.setText("Expiration Date: " + item.getExpirationDate()
                            + "\nQuantity: " + item.getQuantity()
                            + "\nDescription: " + item.getDescription());
                    itemContent.addView(contents);
                    // linearLayout for buttons
                    LinearLayout buttonsLayout = new LinearLayout(mActivity);
                    buttonsLayout.setOrientation(LinearLayout.HORIZONTAL);
                    buttonsLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    // edit button
                    Button editButton = new Button(mActivity);
                    editButton.setText("Edit");
                    editButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    editButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mActivity, ItemEditActivity.class);
                            intent.putExtra("itemName",item.getName());
                            intent.putExtra("itemExpirationDate",item.getExpirationDate());
                            intent.putExtra("itemQuantity",Integer.toString(item.getQuantity()));
                            intent.putExtra("itemDescription",item.getDescription());
                            intent.putExtra("itemId",itemId);
                            intent.putExtra("categoryId",mCategoryId);
                            intent.putExtra("operation","Edit");
                            startActivity(intent);
                        }
                    });
                    buttonsLayout.addView(editButton);
                    // delete button
                    Button deleteButton = new Button(mActivity);
                    deleteButton.setText("Delete");
                    deleteButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    deleteButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mItemReference.child(itemId).removeValue();
                            mItemLayout.removeAllViews();
                        }
                    });
                    buttonsLayout.addView(deleteButton);
                    itemContent.addView(buttonsLayout);
                    mItemLayout.addView(itemContent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "FAIL TO UPDATE");
            }
        };
        itemQuery.addValueEventListener(itemListener);
        Button addButton = mView.findViewById(R.id.btn_add_item);
        Button scanButton = mView.findViewById(R.id.btn_scan_item);
        addButton.setOnClickListener(this);
        scanButton.setOnClickListener(this);
        return mView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_item:
                Intent addIntent = new Intent(mActivity, ItemEditActivity.class);
                addIntent.putExtra("operation", "Add");
                addIntent.putExtra("categoryId",mCategoryId);
                startActivity(addIntent);
                break;
            case R.id.btn_scan_item:
                Intent scanIntent = new Intent(mActivity, ScanActivity.class);
                scanIntent.putExtra("operation", "Scan");
                scanIntent.putExtra("categoryId",mCategoryId);
                startActivity(scanIntent);
                break;
        }
    }
}
