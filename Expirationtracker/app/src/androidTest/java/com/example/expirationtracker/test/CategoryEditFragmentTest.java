package com.example.expirationtracker.test;

import android.view.View;
import android.widget.LinearLayout;

import androidx.test.rule.ActivityTestRule;

import com.example.expirationtracker.R;
import com.example.expirationtracker.ui.CategoryEditFragment;
import com.example.expirationtracker.ui.NavActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static junit.framework.TestCase.assertNotNull;

public class CategoryEditFragmentTest {

    @Rule
    public ActivityTestRule<NavActivity> mActivityTestRule = new ActivityTestRule<>(NavActivity.class);
    private NavActivity mActivity = null;
    @Before
    public void setUp() throws Exception {
        mActivity = mActivityTestRule.getActivity();
    }

    @Test
    public void testLaunch(){
        LinearLayout container = (LinearLayout) mActivity.findViewById(R.id.categoryEditFrag);
        assertNotNull(container);
        CategoryEditFragment test = new CategoryEditFragment();
        mActivity.getSupportFragmentManager().beginTransaction().add(container.getId(), test).commitAllowingStateLoss();
        getInstrumentation().waitForIdleSync();
        View view = test.getView();
        assertNotNull(view);
    }

    @After
    public void tearDown() throws Exception {
        mActivity = null;
    }
}