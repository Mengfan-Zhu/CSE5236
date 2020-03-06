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


    Button mSaveButton;
    ImageView mAddButton;
    ImageView mMinusButton;
    String mName;
    String mQuantity;
    String mDescription;

    private OnFragmentInteractionListener mListener;

    public ItemEditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ItemEditFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ItemEditFragment newInstance(String param1, String param2) {
        ItemEditFragment fragment = new ItemEditFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_item_edit, container, false);
        mAuth = FirebaseAuth.getInstance();
        mActivity = getActivity();
        final Intent intent = mActivity.getIntent();

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
                mItemReference = FirebaseDatabase.getInstance().getReference().child("items").child(mAuth.getUid());
                Item i = new Item(mName, "" + year + "" + month + "" + day, Integer.parseInt(mQuantity), 0, mDescription);
                if((intent.getStringExtra("operation")).equals("Edit")){
                    mItemReference.child(intent.getStringExtra("itemId")).setValue(i);
                }else {
                    mItemReference.push().setValue(i);
                }
                Intent newIntent = new Intent(mActivity, ItemListActivity.class);
                startActivity(newIntent);

            }
        });

        mAddButton = mView.findViewById(R.id.btn_add_quantity);
        mAddButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                String quantity = ((TextView)mView.findViewById(R.id.quantity)).getText().toString();
                ((TextView)mView.findViewById(R.id.quantity)).setText(Integer.parseInt(quantity) + 1 + "");
            }
        });
        mMinusButton = mView.findViewById(R.id.btn_minus_quantity);
        mMinusButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                String quantity = ((TextView)mView.findViewById(R.id.quantity)).getText().toString();
                if(Integer.parseInt(quantity) > 0) {
                    ((TextView) mView.findViewById(R.id.quantity)).setText(Integer.parseInt(quantity) - 1 + "");
                }
            }
        });

        return mView;
    }

//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

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
