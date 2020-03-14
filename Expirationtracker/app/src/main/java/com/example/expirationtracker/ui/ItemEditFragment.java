package com.example.expirationtracker.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


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
        if(intent.getStringExtra("operation") != null) {
            // deal with edit
            if (intent.getStringExtra("operation").equals("Edit")) {
                ((EditText)mView.findViewById(R.id.text_item_name)).setText(intent.getStringExtra("itemName"));
                String date = intent.getStringExtra("itemExpirationDate");
                ((DatePicker) mView.findViewById(R.id.date_picker)).updateDate(Integer.parseInt(date.substring(0,4)), Integer.parseInt(date.substring(4,6))-1 , Integer.parseInt(date.substring(6,8)));
                ((TextView)mView.findViewById((R.id.quantity))).setText(intent.getStringExtra("itemQuantity"));
                ((EditText)mView.findViewById(R.id.description)).setText(intent.getStringExtra("itemDescription"));
            }
            else if(intent.getStringExtra("operation").equals("Scan")){
                ((EditText)mView.findViewById(R.id.text_item_name)).setText(intent.getStringExtra("itemName"));
            }
        }
        // save
        mSaveButton = mView.findViewById(R.id.btn_item_save);
        mSaveButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                mName = ((EditText)mView.findViewById(R.id.text_item_name)).getText().toString();
                int day = ((DatePicker) mView.findViewById(R.id.date_picker)).getDayOfMonth();
                int month = ((DatePicker) mView.findViewById(R.id.date_picker)).getMonth()+1;
                int year = ((DatePicker) mView.findViewById(R.id.date_picker)).getYear();
                String date = "" + year;
                if(month < 10){
                    date  = date + "0" + month;
                }
                else{
                    date = date + month;
                }
                if(day < 10){
                    date  = date + "0" + day;
                }
                else{
                    date = date + day;
                }
                mQuantity = ((TextView)mView.findViewById(R.id.quantity)).getText().toString();
                mDescription = ((EditText)mView.findViewById(R.id.description)).getText().toString();
                mItemReference = FirebaseDatabase.getInstance().getReference().child("items").child(mAuth.getUid()).child(mCategoryId);
                Item i = new Item(mName, date, Integer.parseInt(mQuantity),  mDescription);
                if((intent.getStringExtra("operation")).equals("Edit")){
                    mItemReference.child(intent.getStringExtra("itemId")).setValue(i);
                }else {
                    mItemReference.push().setValue(i);
                }
                Intent newIntent = new Intent(mActivity, ItemListActivity.class);
                newIntent.putExtra("categoryId",mCategoryId);
                startActivity(newIntent);

            }
        });
        return mView;
    }
}
