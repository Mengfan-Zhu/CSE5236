package com.example.expirationtracker.ui;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.CalendarContract;
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
import com.example.expirationtracker.model.Category;
import com.example.expirationtracker.model.Item;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private FirebaseAuth mAuth;
    private DatabaseReference mItemReference;
    private View mView;
    private Activity mActivity;
    private LinearLayout mItemLayout;
    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeFragment.
     */
    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mActivity = getActivity();
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        mAuth = FirebaseAuth.getInstance();
        mItemReference = FirebaseDatabase.getInstance().getReference().child("items").child(mAuth.getUid());
        Query itemQuery = mItemReference.orderByChild("name");
        ScrollView itemList = (ScrollView) mView.findViewById(R.id.home_layout);
        itemList.setFillViewport(true);
        mItemLayout = new LinearLayout(mActivity);
        mItemLayout.setPadding(10,10,10,400);
        mItemLayout.setOrientation(LinearLayout.VERTICAL);
        itemList.addView(mItemLayout);
        ValueEventListener itemListener = new ValueEventListener() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            List<Item> nonExpired = new ArrayList<>();
            List<Item> expired = new ArrayList<>();
            Map<Item, String[]> expiredId = new HashMap<>();
            Map<Item, String[]> nonExpiredId = new HashMap<>();
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mItemLayout.removeAllViews();

                for (DataSnapshot currentCategoty : dataSnapshot.getChildren()) {
                    final String categoryId = currentCategoty.getKey();
                    for(DataSnapshot currentSnapshot : currentCategoty.getChildren()){
                        final String itemId = currentSnapshot.getKey();
                        final Item item = currentSnapshot.getValue(Item.class);
                        String[] id = new String[2];
                        id[0] = itemId;
                        id[1] = categoryId;
                        Date expDate = null;
                        Date currDate = null;
                        try {
                            expDate = sdf.parse(item.getExpirationDate());
                            currDate = sdf.parse(sdf.format(new Date() ));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if(expDate.compareTo(currDate) > 0 ){
                            nonExpired.add(item);
                            nonExpiredId.put(item, id);
                        }else{
                            expired.add(item);
                            expiredId.put(item, id);
                        }
                    }
                }

                Collections.sort(expired, new Comparator<Item>(){
                    @Override
                    public int compare(Item i1, Item i2) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                        int diff = 0;
                        try {
                            diff = (int) (sdf.parse(i1.getExpirationDate()).getTime() - sdf.parse(i2.getExpirationDate()).getTime());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        return diff;
                    }
                });

                Collections.sort(nonExpired, new Comparator<Item>(){
                    @Override
                    public int compare(Item i1, Item i2) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                        int diff = 0;
                        try {
                            diff = (int) (sdf.parse(i2.getExpirationDate()).getTime() - sdf.parse(i1.getExpirationDate()).getTime());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        return diff;
                    }
                });
                TextView expiredItem = new TextView(mActivity);
                expiredItem.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                expiredItem.setText("Expired Items");
                expiredItem.setTextColor(Color.parseColor("#FF0000"));
                expiredItem.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
                mItemLayout.addView(expiredItem);
                for(Item item : expired){
                    String itemId = expiredId.get(item)[0];
                    String categoryId = expiredId.get(item)[1];
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
                    name.setTextColor(Color.parseColor("#FF0000"));
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
                            intent.putExtra("eventId",Long.toString(item.getEventId()));
                            intent.putExtra("categoryId",categoryId);
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
                            ContentResolver cr = mActivity.getContentResolver();
                            Uri deleteEvent = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, item.getEventId());
                            cr.delete(deleteEvent, null, null);
                            mItemReference.child(itemId).removeValue();
                            mItemLayout.removeAllViews();
                        }
                    });
                    buttonsLayout.addView(deleteButton);
                    itemContent.addView(buttonsLayout);
                    mItemLayout.addView(itemContent);
                }

                TextView nonExpiredItem = new TextView(mActivity);
                nonExpiredItem.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                nonExpiredItem.setText("Not Expired Items");
                nonExpiredItem.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
                mItemLayout.addView(nonExpiredItem);

                for(Item item : nonExpired){
                    String itemId = nonExpiredId.get(item)[0];
                    String categoryId = nonExpiredId.get(item)[1];
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
                            intent.putExtra("eventId",Long.toString(item.getEventId()));
                            intent.putExtra("categoryId",categoryId);
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
                            ContentResolver cr = mActivity.getContentResolver();
                            Uri deleteEvent = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, item.getEventId());
                            cr.delete(deleteEvent, null, null);
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
        return mView;
    }

}
