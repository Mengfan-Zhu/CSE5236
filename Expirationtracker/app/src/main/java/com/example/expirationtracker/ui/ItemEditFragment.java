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

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.CalendarContract;
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
                Item i = new Item(mName, mDate, Integer.parseInt(mQuantity), mDescription);
                if ((intent.getStringExtra("operation")).equals("Edit")) {
                    mItemReference.child(intent.getStringExtra("itemId")).setValue(i);
                } else {
                    mItemReference.push().setValue(i);
                }
                addReminder(intent.getStringExtra("operation"));
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
                            int month = Integer.parseInt(mDate.substring(4, 6))-1;
                            int day = Integer.parseInt(mDate.substring(6, 8));
                            // get time
                            String[] time = mCategory.getTime().split(":");
                            int hour = Integer.parseInt(time[0]);
                            int minute = Integer.parseInt(time[1]);

                            Calendar start = Calendar.getInstance();
                            Calendar end = Calendar.getInstance();
                            start.set(year, month, day, hour, minute);
                            end.set(year, month, day, hour, minute + 1);

                            // get timezone
                            String timezone = "America/New_York";
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                timezone = ZoneId.systemDefault().toString();
                            }
                            // set event
                            ContentValues event = new ContentValues();
                            event.put("title", "Expiration tracker reminder");
                            event.put("description", mName + " will be expired on " + mDate);
                            event.put("calendar_id", 1); //插入账户的id
                            event.put(CalendarContract.Events.DTSTART, start.getTimeInMillis());
                            event.put(CalendarContract.Events.DTEND, end.getTimeInMillis());
                            event.put(CalendarContract.Events.HAS_ALARM, 1);
                            event.put(CalendarContract.Events.EVENT_TIMEZONE, timezone);
                            ContentResolver context = mActivity.getContentResolver();
                            int checkCalenderPermission = ContextCompat.checkSelfPermission(mActivity,
                                    Manifest.permission.WRITE_CALENDAR);
                            Uri newEvent = null;
                            if(checkCalenderPermission != PackageManager.PERMISSION_GRANTED){
                                ActivityCompat.requestPermissions(mActivity,new String[]{Manifest.permission.WRITE_CALENDAR},2);
                            }else{
                                newEvent = context.insert(CalendarContract.Events.CONTENT_URI, event);
                                if (newEvent == null) {
                                    return;
                                }
                            }
//            //set reminder
//            ContentValues values = new ContentValues();
//            values.put(CalendarContract.Reminders.EVENT_ID, ContentUris.parseId(newEvent));
//            values.put(CalendarContract.Reminders.MINUTES, previousDate * 24 * 60);
//            values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
//            Uri uri = context.getContentResolver().insert(Uri.parse(CALENDER_REMINDER_URL), values);
//            if(uri == null) {
//                return;
//            }
                        }
                        else {
                            //update reminder
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


    }
}
