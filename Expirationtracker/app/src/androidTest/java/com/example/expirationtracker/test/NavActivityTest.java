package com.example.expirationtracker.test;

import android.content.Intent;
import android.view.View;

import androidx.test.rule.ActivityTestRule;

import com.example.expirationtracker.R;
import com.example.expirationtracker.ui.NavActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.TestCase.assertNotNull;

public class NavActivityTest {

    @Rule
    public ActivityTestRule<NavActivity> mActivityTestRule = new ActivityTestRule<NavActivity>(NavActivity.class);
    private NavActivity mActivity = null;

    @Before
    public void setUp() throws Exception {
//        mActivity = mActivityTestRule.getActivity();
        Intent intent = new Intent();
        intent.putExtra("content", "HOME");
        mActivity = mActivityTestRule.launchActivity(intent);
    }

    @Test
    public void testLaunch(){
//        View view = mActivity.findViewById(R.id.navAct);
//        assertNotNull(view);
    }

    @After
    public void tearDown() throws Exception {
        mActivity = null;
    }
}