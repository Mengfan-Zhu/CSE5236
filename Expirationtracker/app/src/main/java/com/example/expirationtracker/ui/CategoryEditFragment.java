package com.example.expirationtracker.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.app.Activity;
import android.widget.TimePicker;

import com.example.expirationtracker.R;
import com.example.expirationtracker.model.Category;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CategoryEditFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CategoryEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryEditFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    // TODO: Rename and change types of parameters
    private Spinner spin;
    private ArrayList<String> mData = null;
    private BaseAdapter myAdapter = null;
    private OnFragmentInteractionListener mListener;
    private Activity mActivity;
    private FirebaseAuth mAuth;
    private DatabaseReference mCategoryReference;
    private View mView;


    Button mSaveButton;
    String mName;
    String mNotification;
    String mFrequency;
    String mRemindingTime;
    public CategoryEditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CategoryEditFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoryEditFragment newInstance(String param1, String param2) {
        CategoryEditFragment fragment = new CategoryEditFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_category_edit, container, false);
        mAuth = FirebaseAuth.getInstance();
        mActivity = getActivity();
        final Intent intent = getActivity().getIntent();

        if(intent.getStringExtra("operation") != null){
            if( intent.getStringExtra("operation").equals("Edit")){
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


        // Inflate the layout for this fragment
        return mView;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
