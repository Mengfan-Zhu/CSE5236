package com.example.expirationtracker.ui;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.CalendarContract;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.expirationtracker.R;
import com.example.expirationtracker.model.Category;
import com.example.expirationtracker.model.Item;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.ZoneId;
import java.util.Calendar;

import static android.Manifest.permission.WRITE_CALENDAR;
import static androidx.core.content.ContextCompat.checkSelfPermission;


public class ItemEditFragment extends Fragment {

    private Activity mActivity;
    private FirebaseAuth mAuth;
    private DatabaseReference mItemReference;
    private View mView;
    private String mCategoryId;
    private Button mSaveButton;
    private String mName;
    private String mQuantity;
    private String mDescription;
    private String mDate;
    private Category mCategory;
    private long mEventId;


    public ItemEditFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_item_edit, container, false);
        mAuth = FirebaseAuth.getInstance();
        mActivity = getActivity();
        final Intent intent = mActivity.getIntent();
        mCategoryId = intent.getStringExtra("categoryId");
        if (intent.getStringExtra("operation") != null) {
            // deal with edit
            if (intent.getStringExtra("operation").equals("Edit")) {
                ((EditText) mView.findViewById(R.id.text_item_name)).setText(intent.getStringExtra("itemName"));
                String date = intent.getStringExtra("itemExpirationDate");
                ((DatePicker) mView.findViewById(R.id.date_picker)).updateDate(Integer.parseInt(date.substring(0, 4)), Integer.parseInt(date.substring(4, 6)) - 1, Integer.parseInt(date.substring(6, 8)));
                ((TextView) mView.findViewById((R.id.quantity))).setText(intent.getStringExtra("itemQuantity"));
                ((EditText) mView.findViewById(R.id.description)).setText(intent.getStringExtra("itemDescription"));
            } else if (intent.getStringExtra("operation").equals("Scan")) {
                ((EditText) mView.findViewById(R.id.text_item_name)).setText(intent.getStringExtra("itemName"));
            }
        }
        // save
        mSaveButton = mView.findViewById(R.id.btn_item_save);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mName = ((EditText) mView.findViewById(R.id.text_item_name)).getText().toString();
                int day = ((DatePicker) mView.findViewById(R.id.date_picker)).getDayOfMonth();
                int month = ((DatePicker) mView.findViewById(R.id.date_picker)).getMonth() + 1;
                int year = ((DatePicker) mView.findViewById(R.id.date_picker)).getYear();
                mDate = "" + year;
                if (month < 10) {
                    mDate = mDate + "0" + month;
                } else {
                    mDate = mDate + month;
                }
                if (day < 10) {
                    mDate = mDate + "0" + day;
                } else {
                    mDate = mDate + day;
                }
                mQuantity = ((TextView) mView.findViewById(R.id.quantity)).getText().toString();
                mDescription = ((EditText) mView.findViewById(R.id.description)).getText().toString();
                mItemReference = FirebaseDatabase.getInstance().getReference().child("items").child(mAuth.getUid()).child(mCategoryId);
                addReminder(intent.getStringExtra("operation"));
                Item i = new Item(mName, mDate, Integer.parseInt(mQuantity), mDescription, mEventId);
                if ((intent.getStringExtra("operation")).equals("Edit")) {
                    mItemReference.child(intent.getStringExtra("itemId")).setValue(i);
                } else {
                    mItemReference.push().setValue(i);
                }
                Intent newIntent = new Intent(mActivity, ItemListActivity.class);
                newIntent.putExtra("categoryId", mCategoryId);
                startActivity(newIntent);
            }
        });
        return mView;
    }

    public void addReminder(String operation) {
        // get category info
        FirebaseDatabase.getInstance().getReference().child("categories").child(mAuth.getUid()).child(mCategoryId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user information
                        mCategory = dataSnapshot.getValue(Category.class);
                        if (operation != "Edit") {
                            //add new reminder
                            // get date
                            int year = Integer.parseInt(mDate.substring(0, 4));
                            int month = Integer.parseInt(mDate.substring(4, 6)) - 1;
                            int day = Integer.parseInt(mDate.substring(6, 8));
                            // get time
                            String[] time = mCategory.getTime().split(":");
                            int hour = Integer.parseInt(time[0]);
                            int minute = Integer.parseInt(time[1])+10;
                            // ids
                            long calendar_id = 1;
//                            long event_id = 222988778;
                            // set up time
                            Calendar start = Calendar.getInstance();
                            Calendar end = Calendar.getInstance();
//                            start.set(year, month, day, hour, minute);
                            end.set(year, month, day, hour, minute);

                            String duration = "P1D";
                            switch(mCategory.getBegin()){
                                case "1 day before":
                                    duration = "P1D";
                                    start.set(year, month, day-1, hour, minute);
                                    break;
                                case "2 day before":
                                    duration = "P2D";
                                    start.set(year, month, day-2, hour, minute);
                                    break;
                                case "1 week before":
                                    duration = "P1W";
                                    start.set(year, month, day-7, hour, minute);
                                    break;
                                case "2 week before":
                                    duration = "P2W";
                                    start.set(year, month, day-14, hour, minute);
                                    break;
                                case "1 month before":
                                    duration = "P1M";
                                    start.set(year, month-1, day, hour, minute);
                                    break;
                            }
                            String rrule = "";
                            switch (mCategory.getFrequency()) {
                                // 每天
                                case "everyday":
                                    rrule = "FREQ=DAILY;UNTIL=" + mDate + "T235959Z";
                                    break;
                                // 每周
                                case "2 days":
                                    rrule = "FREQ=DAILY;INTERVAL=2;UNTIL=" + mDate + "T235959Z";
                                    break;
                                // 每两周
                                case "3 days":
                                    rrule = "FREQ=DAILY;INTERVAL=3;UNTIL=" + mDate + "T235959Z";
                                    break;
                                // 每月
                                case "1 week":
                                    rrule = "FREQ=WEEKLY;UNTIL=" + mDate + "T235959Z";
                                    break;
                                // 每年
                                case "2 weeks":
                                    rrule = "FREQ=WEEKLY;INTERVAL=2;UNTIL=" + mDate + "T235959Z";
                                    break;
                                default:
                                    break;
                            }
                            // get timezone
                            String timezone = "America/New_York";
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                timezone = ZoneId.systemDefault().toString();
                            }

                            // set event
                            ContentValues event = new ContentValues();
                            event.put(CalendarContract.Events.TITLE, "Expiration tracker reminder");
                            event.put(CalendarContract.Events.DESCRIPTION, mName + " will be expired on " + mDate);
                            event.put(CalendarContract.Events.CALENDAR_ID, calendar_id);
//                            event.put(CalendarContract.Events._ID, event_id);
                            event.put(CalendarContract.Events.DTSTART, start.getTimeInMillis());
                            event.put(CalendarContract.Events.DTEND, start.getTimeInMillis());
//                            event.put(CalendarContract.Events.DURATION, duration);
//
                            event.put(CalendarContract.Events.RRULE, rrule);
                            event.put(CalendarContract.Events.HAS_ALARM, 1);
                            event.put(CalendarContract.Events.EVENT_TIMEZONE, timezone);
                            ContentResolver context = mActivity.getContentResolver();
                            int checkCalenderPermission = ContextCompat.checkSelfPermission(mActivity,
                                    Manifest.permission.WRITE_CALENDAR);
                            Uri newEvent = null;
                            Uri newReminder = null;
                            if (checkCalenderPermission != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.WRITE_CALENDAR}, 2);

                            } else {
                                newEvent = context.insert(CalendarContract.Events.CONTENT_URI, event);
                                mEventId = Long.parseLong(newEvent.getLastPathSegment());
                                ContentValues reminder = new ContentValues();
                                reminder.put(CalendarContract.Reminders.EVENT_ID, mEventId);
                                reminder.put(CalendarContract.Reminders.MINUTES, 10);
                                reminder.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
                                newReminder = context.insert(CalendarContract.Reminders.CONTENT_URI, reminder);
                                if(newEvent == null || newReminder == null){
                                    return;
                                }
                            }
                        }
                        else{
                            // update
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

}
