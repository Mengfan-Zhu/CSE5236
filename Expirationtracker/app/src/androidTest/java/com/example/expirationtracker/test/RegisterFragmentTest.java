package com.example.expirationtracker.test;

import android.view.View;
import android.widget.LinearLayout;

import androidx.test.rule.ActivityTestRule;

import com.example.expirationtracker.R;
import com.example.expirationtracker.ui.MainActivity;
import com.example.expirationtracker.ui.RegisterFragment;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static junit.framework.TestCase.assertNotNull;

public class RegisterFragmentTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);
    private MainActivity mActivity = null;
    @Before
    public void setUp() throws Exception {
        mActivity = mActivityTestRule.getActivity();
    }

    @Test
    public void testLaunch(){
        LinearLayout container = (LinearLayout) mActivity.findViewById(R.id.registerFrag);
        assertNotNull(container);
        RegisterFragment test = new RegisterFragment();
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