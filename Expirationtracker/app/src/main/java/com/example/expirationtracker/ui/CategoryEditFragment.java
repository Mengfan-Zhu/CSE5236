package com.example.expirationtracker.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.app.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.example.expirationtracker.R;
import com.example.expirationtracker.model.Category;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class CategoryEditFragment extends Fragment{
    private Activity mActivity;
    private FirebaseAuth mAuth;
    private DatabaseReference mCategoryReference;
    private View mView;
    private Button mSaveButton;
    private String mName;
    private String mNotification;
    private String mFrequency;
    public CategoryEditFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_category_edit, container, false);
        mAuth = FirebaseAuth.getInstance();
        mActivity = getActivity();
        final Intent intent = getActivity().getIntent();

        if(intent.getStringExtra("operation") != null){
            // edit mode
            if( intent.getStringExtra("operation").equals("Edit")){
                // get current contents
                ((EditText)mView.findViewById(R.id.text_category_name)).setText(intent.getStringExtra("categoryName"));
                int pos = 0;
                switch (intent.getStringExtra("categoryBegin")){
                    case "1 day before":
                        pos = 0;
                        break;
                    case "3 days before":
                        pos = 1;
                        break;
                    case "1 week before":
                        pos = 2;
                        break;
                    case "10 days before":
                        pos = 3;
                        break;
                    case "1 month before":
                        pos = 4;
                        break;
                    case "3 months before":
                        pos = 5;
                        break;
                }
                ((Spinner)mView.findViewById(R.id.notification_setting)).setSelection(pos);
                switch (intent.getStringExtra("categoryFrequency")){
                    case "3 days":
                        ((RadioButton)mView.findViewById(R.id.btn_1)).setChecked(false);
                        ((RadioButton)mView.findViewById(R.id.btn_2)).setChecked(true);
                        break;
                    case "1 week":
                        ((RadioButton)mView.findViewById(R.id.btn_1)).setChecked(false);
                        ((RadioButton)mView.findViewById(R.id.btn_3)).setChecked(true);
                        break;
                    case "2 weeks":
                        ((RadioButton)mView.findViewById(R.id.btn_1)).setChecked(false);
                        ((RadioButton)mView.findViewById(R.id.btn_4)).setChecked(true);
                        break;
                    case "1 month":
                        ((RadioButton)mView.findViewById(R.id.btn_1)).setChecked(false);
                        ((RadioButton)mView.findViewById(R.id.btn_5)).setChecked(true);
                        break;
                }
                String[] s = intent.getStringExtra("categoryTime").split(":");
                ((TimePicker) mView.findViewById(R.id.time_picker)).setCurrentHour(Integer.parseInt(s[0]));
                ((TimePicker) mView.findViewById(R.id.time_picker)).setCurrentMinute(Integer.parseInt(s[1]));
            }
        }
        // save
        mSaveButton = mView.findViewById(R.id.btn_save);
        mSaveButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                mName = ((EditText)mView.findViewById(R.id.text_category_name)).getText().toString();
                mNotification = ((Spinner)mView.findViewById(R.id.notification_setting)).getSelectedItem().toString();
                int selectedId = ((RadioGroup)mView.findViewById(R.id.frequency)).getCheckedRadioButtonId();
                mFrequency = ((RadioButton)mView.findViewById(selectedId)).getText().toString();
                int mHourRemindingTime =((TimePicker) mView.findViewById(R.id.time_picker)).getCurrentHour();
                int mMinuteRemindingTime = ((TimePicker) mView.findViewById(R.id.time_picker)).getCurrentMinute();
                mCategoryReference = FirebaseDatabase.getInstance().getReference().child("categories").child(mAuth.getUid());
                Category c = new Category(mName, mNotification, mFrequency, mHourRemindingTime + ":" +mMinuteRemindingTime);
                if((intent.getStringExtra("operation")).equals("Edit")){
                    mCategoryReference.child(intent.getStringExtra("categoryId")).setValue(c);
                }else {
                    mCategoryReference.push().setValue(c);
                }
                Intent newIntent = new Intent(mActivity, CategoryListActivity.class);
                startActivity(newIntent);

            }
        });
        return mView;
    }

}
