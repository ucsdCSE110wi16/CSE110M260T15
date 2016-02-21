package com.ucsd.cse110t15m260.roommateinventory;

import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by jlsmith on 2/20/16.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginTest extends ActivityInstrumentationTestCase2<LoginActivity>{

    private String mStringToBetyped;
    private LoginActivity mActivity;

    public LoginTest() {
        super(LoginActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mActivity = getActivity();

    }

    @Before
    public void initValidString() {
        mStringToBetyped = "a@a.com";
    }

    @Test
    public void ensureTextChangesWork() {
        // Type text and then press the button
        onView(withId(R.id.email)).perform(typeText(mStringToBetyped),  closeSoftKeyboard());
        onView(withId(R.id.email_sign_in_button)).perform(click());



    }


}
