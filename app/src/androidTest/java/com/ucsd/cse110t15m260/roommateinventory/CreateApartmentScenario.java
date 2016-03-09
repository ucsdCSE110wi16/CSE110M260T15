package com.ucsd.cse110t15m260.roommateinventory;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;
import android.widget.TextView;

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
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Leo Wong on 3/7/2016.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class CreateApartmentScenario extends ApartmentTestCase {

    @Rule
    public ActivityTestRule<RegisterActivity> mActivityRule = new ActivityTestRule<>(RegisterActivity.class);
    protected String mEmail;
    protected String mPassword;
    protected String mName;
    protected String mAptName;
    protected String mStreet1;
    protected String mStreet2;
    protected String mCity;
    protected String mState;
    protected String mZipCode;

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

        Thread.sleep(1000);

        //Check that an actual apartment was made
        Person person = Person.getCurrentPerson();
        String aptID = person.getApartment().getObjectId();
        onView(withId(R.id.apartment_invitation_id)).check(matches(withText(aptID)));

        Thread.sleep(1000);

        //Press the actionbar back button
        onView(withContentDescription("Navigate up")).perform(click());

        //Now back in the main activity main fragment
        String mainStr = "Welcome, " + person.getString("name") + "!\n" +
                "Your User ID is: " + person.getObjectId() + "\n" +
                "Your Apartment is: " + person.getApartment().getObjectId();

        //Check that the main fragment string matches what it's supposed to be
        onView(withId(R.id.textview_welcome)).check(matches(withText(mainStr)));

        Thread.sleep(1000);
    }

    @Test
    public void deleteUser() {

    }

    @After
    public void tearDown() throws Exception {
        cleanUp();
    }
}
