//package com.example.expirationtracker.test;
//
//import android.content.Intent;
//import android.view.View;
//import android.widget.LinearLayout;
//
//import androidx.test.espresso.intent.rule.IntentsTestRule;
//import androidx.test.rule.ActivityTestRule;
//
//import com.example.expirationtracker.R;
//import com.example.expirationtracker.ui.Category.CategoryListFragment;
//import com.example.expirationtracker.ui.NavActivity;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//
//import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
//import static junit.framework.TestCase.assertNotNull;
//
//public class CategoryListFragmentTest {
//
//    @Rule
//    public IntentsTestRule<NavActivity> mIntentsRule  = new IntentsTestRule<NavActivity>(NavActivity.class){
//        @Override
//        protected Intent getActivityIntent() {
//            Intent intent = new Intent();
//            intent.putExtra("content", "CATEGORY_LIST");
//            return intent;
//        }
//    };
//    private NavActivity mActivity = null;
//    @Before
//    public void setUp() throws Exception {
//        mActivity = mIntentsRule.getActivity();
//    }
//
//    @Test
//    public void testLaunch(){
////        View view = mActivity.findViewById(R.id.fragment_category_list);
////        assertNotNull(view);
////        LinearLayout container = (LinearLayout) mActivity.findViewById(R.id.fragment_category_list);
////        assertNotNull(container);
////        CategoryListFragment test = new CategoryListFragment();
////        mActivity.getSupportFragmentManager().beginTransaction().add(container.getId(), test).commitAllowingStateLoss();
////        getInstrumentation().waitForIdleSync();
////        View view = test.getView();
////        assertNotNull(view);
//    }
//
//    @After
//    public void tearDown() throws Exception {
//        mActivity = null;
//    }
//}