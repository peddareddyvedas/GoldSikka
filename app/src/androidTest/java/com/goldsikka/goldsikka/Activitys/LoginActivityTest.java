package com.goldsikka.goldsikka.Activitys;

import android.app.Activity;
import android.app.Instrumentation;
import android.view.View;
import android.widget.Button;

import androidx.test.rule.ActivityTestRule;

import com.goldsikka.goldsikka.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;

public class LoginActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<LoginActivity>(LoginActivity.class);

    public LoginActivity mActivity = null;

    Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(OTPActivity.class.getName(),null ,false);

    @Before
    public void setUp() throws Exception {

        mActivity = mActivityTestRule.getActivity();

    }


    @Test
    public void testLaunch(){

        Button view = mActivity.findViewById(R.id.btn_login);
        assertNotNull(view);

        onView(withId(R.id.btn_login)).perform(click());

//       Activity otpactivity = getInstrumentation().waitForMonitorWithTimeout(monitor,50000);
//       assertNotNull(otpactivity);
//       otpactivity.finish();
    }

    @After
    public void tearDown() throws Exception {
        mActivity = null;
    }
}