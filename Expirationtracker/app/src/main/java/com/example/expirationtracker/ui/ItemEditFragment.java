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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ItemEditFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ItemEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ItemEditFragment extends Fragment {

    private Activity mActivity;
    private FirebaseAuth mAuth;
    private DatabaseReference mItemReference;
    private View mView;

    private String mCategoryId;
    Button mSaveButton;
    String mName;
    String mQuantity;
    String mDescription;

    private OnFragmentInteractionListener mListener;

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
            if (intent.getStringExtra("operation").equals("Edit")) {
                ((EditText)mView.findViewById(R.id.text_item_name)).setText(intent.getStringExtra("itemName"));
                String date = intent.getStringExtra("itemExpirationDate");
                ((DatePicker) mView.findViewById(R.id.date_picker)).updateDate(Integer.parseInt(date.substring(0,4)), Integer.parseInt(date.substring(4,6)), Integer.parseInt(date.substring(6,8)));
                ((TextView)mView.findViewById((R.id.quantity))).setText(intent.getStringExtra("itemQuantity"));
                ((EditText)mView.findViewById(R.id.description)).setText(intent.getStringExtra("itemDescription"));

            }
        }

        mSaveButton = mView.findViewById(R.id.btn_item_save);
        mSaveButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                mName = ((EditText)mView.findViewById(R.id.text_item_name)).getText().toString();
                int day = ((DatePicker) mView.findViewById(R.id.date_picker)).getDayOfMonth();
                int month = ((DatePicker) mView.findViewById(R.id.date_picker)).getMonth();
                int year = ((DatePicker) mView.findViewById(R.id.date_picker)).getYear();

                mQuantity = ((TextView)mView.findViewById(R.id.quantity)).getText().toString();
                mDescription = ((EditText)mView.findViewById(R.id.description)).getText().toString();
                mItemReference = FirebaseDatabase.getInstance().getReference().child("items").child(mAuth.getUid()).child(mCategoryId);
                Item i = new Item(mName, "" + year + "" + month + "" + day, Integer.parseInt(mQuantity),  mDescription);
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