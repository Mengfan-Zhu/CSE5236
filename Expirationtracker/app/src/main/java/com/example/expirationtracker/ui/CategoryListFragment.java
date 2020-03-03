package com.example.expirationtracker.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import androidx.fragment.app.Fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.view.LayoutInflater;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import com.example.expirationtracker.R;
import com.example.expirationtracker.model.Category;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link CategoryListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
    private FirebaseAuth mAuth;
    private DatabaseReference mCategoryReference;
    private View mView;

//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;

//    private OnFragmentInteractionListener mListener;

    public CategoryListFragment() {
        // Required empty public constructor
    }

//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment CategoryListFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static CategoryListFragment newInstance(String param1, String param2) {
//        CategoryListFragment fragment = new CategoryListFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // TODO: Need to load all categories from database.
        mView = inflater.inflate(R.layout.fragment_category_list, container, false);
        mAuth = FirebaseAuth.getInstance();
//        mCategories = view.findViewById(R.id.category_list);
        //Button addButton = v.findViewById(R.id.btn_add);
        //if(addButton != null){
        //
        //}
        mCategoryReference = FirebaseDatabase.getInstance().getReference().child("categories").child(mAuth.getUid());
        Query categoryQuery = mCategoryReference.orderByChild("name");

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Activity activity = getActivity();
                // add wrap layout
                ScrollView categoryList = (ScrollView) mView.findViewById(R.id.category_layout);
                LinearLayout categoryLayout = new LinearLayout(activity);
                categoryLayout.setOrientation(LinearLayout.VERTICAL);
                categoryList.addView(categoryLayout);
                // add each layout
                for (DataSnapshot currentSnapshot: dataSnapshot.getChildren()) {
                    Category category = currentSnapshot.getValue(Category.class);
                    LinearLayout categoryContent = new LinearLayout(activity);
                    categoryContent.setOrientation(LinearLayout.VERTICAL);
                    categoryContent.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    categoryContent.setDividerPadding(10);
                    categoryContent.setBackgroundResource(R.drawable.bg_item);
                    TextView name = new TextView(activity);
                    name.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                    name.setText(category.getName());
                    name.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                    categoryContent.addView(name);
                    TextView contents = new TextView(activity);
                    contents.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                    contents.setText("Begin: "+ category.getBegin()+"\nFrequency: " + category.getFrequency()+"\nTime: "+ category.getTime());
                    categoryContent.addView(contents);
                    LinearLayout buttonsLayout = new LinearLayout(activity);
                    buttonsLayout.setOrientation(LinearLayout.HORIZONTAL);
                    buttonsLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    Button editButton = new Button(activity);
                    editButton.setText("Edit");
                    editButton.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    buttonsLayout.addView(editButton);
                    Button deleteButton = new Button(activity);
                    deleteButton.setText("Delete");
                    deleteButton.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    buttonsLayout.addView(deleteButton);
                    categoryContent.addView(buttonsLayout);
                    categoryLayout.addView(categoryContent);
              }
                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                // ...
            }
        };
        categoryQuery.addValueEventListener(postListener);

        return mView;
    }
//    @Override
//    public void onStart() {
//        super.onStart();
//
//
//    }

    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
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
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
