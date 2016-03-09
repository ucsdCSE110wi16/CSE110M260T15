package com.ucsd.cse110t15m260.roommateinventory;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.parse.ParseUser;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import Model.Person;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.parse.ParseUser.getCurrentUser;
//import android.support.test.espresso.*;

/* Created by jlsmith on 2/20/16.
*/
@RunWith(AndroidJUnit4.class)
@LargeTest
public class CreateUserTest extends ApartmentTestCase {

    @Rule
    public ActivityTestRule<RegisterActivity> mActivityRule = new ActivityTestRule<>(RegisterActivity.class);
    private String mEmail;
    private String mPassword;
    private String mName;
    private String mAptName;
    private String mStreet1;
    private String mStreet2;
    private String mCity;
    private String mState;
    private String mZipCode;

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
    @Before
    public void initValidString() {
        mName = "newUser";
        mEmail = genFakeUser();
        mPassword = "newUser";
        mAptName = "apt";
        mStreet1 = "street1";
        mStreet2 = "street2";
        mCity = "city";
        mState = "state";
        mZipCode = "123";
    }

    @Test
    public void createNewUser() throws InterruptedException {

        createUser(mName, mEmail, mPassword);

        Thread.sleep(1000);

        createApartment(
                mAptName,
                mStreet1,
                mStreet2,
                mCity,
                mState,
                mZipCode
        );

    }

    @Test
    public void deleteUser() {

    }


    @After
    public void tearDown() throws Exception {
        cleanUp();
    }
}
