package com.example.expirationtracker.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.app.Activity;
import com.example.expirationtracker.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CategoryEditFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CategoryEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryEditFragment extends Fragment {
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_category_edit, container, false);
        mAuth = FirebaseAuth.getInstance();


        //bindViews(mView);
        // Inflate the layout for this fragment
        return mView;
    }

    private void bindViews(View v){
        spin = (Spinner) v.findViewById(R.id.notification_setting);
        mData.add("1 day before");
        mData.add("3 days before");
        mData.add("1 week before");
        mData.add("10 days before");
        mData.add("1 month before");
        mData.add("3 months before");
        spin.setOnItemSelectedListener(new NotificationOnItemSelectedListener());


    }

    private class  NotificationOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
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
