package com.ucsd.cse110t15m260.roommateinventory;

import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;

import com.ucsd.cse110t15m260.roommateinventory.LoginActivity;
import com.ucsd.cse110t15m260.roommateinventory.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.rule.ActivityTestRule;

import Model.Person;

import static android.support.test.espresso.Espresso.getIdlingResources;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by jlsmith on 2/20/16.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginTest {

    private String mEmail;
    private String mPassword;

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(LoginActivity.class);

    /*public LoginTest() {
        super(LoginActivity.class);
    }
    */

    /* @Override
    public void setUp() throws Exception {
        super.setUp();
        getActivity();

    }
    */
    public void initValidString() {
        // Assuming that this is legal
        mEmail = "a@a.com";
        mPassword = "a";
    }

    public void initInvalidString() {
        mEmail = "fakeEmail@a.com";
        mPassword = "fake";
    }

    public void enterFields() {

            // Type text and then press the button
        onView(withId(R.id.email)).perform(typeText(mEmail),  closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText(mPassword), closeSoftKeyboard());

        onView(withId(R.id.email)).check(matches(withText(mEmail)));
        onView(withId(R.id.password)).check(matches(withText(mPassword)));
        onView(withId(R.id.email_sign_in_button)).perform(click());
    }

    @Test
    public void ensureTextChangesWork() throws InterruptedException {
        initValidString();

        // Type text and then press the button
        enterFields();
        Thread.sleep(1000);
        onView(withId(R.id.textview_welcome)).check(matches(withId(R.id.textview_welcome)));
    }


    @Test
    public void ensureLoginFails() throws InterruptedException{
        initInvalidString();

        // Type text and then press the button
        enterFields();
        Thread.sleep(1000);
        onView(withId(R.id.password))
                .check(matches(hasErrorText("This password is incorrect")));
    }

    @After
    public void tearDown() throws Exception {
        Person.logOut();
    }
}
