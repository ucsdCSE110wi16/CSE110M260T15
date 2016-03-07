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
public class CreateApartmentScenario {

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

    @Before
    public void initValidString() {
        mName = "newUser";
        mEmail = "new@user.com";
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

        // Type text and then press the button
        onView(withId(R.id.name)).perform(typeText(mName), closeSoftKeyboard());
        onView(withId(R.id.email)).perform(typeText(mEmail), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText(mPassword), closeSoftKeyboard());

        onView(withId(R.id.name)).check(matches(withText("newUser")));
        onView(withId(R.id.email)).check(matches(withText("new@user.com")));
        onView(withId(R.id.password)).check(matches(withText("newUser")));
        onView(withId(R.id.email_register_button)).perform(click());

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());

        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.my_apt));

        Thread.sleep(1000);

        //Enter the create apartment activity
        onView(withId(R.id.create_apt)).perform(click());

        //Typing in all required fields
        onView(withId(R.id.apartment_name)).perform(typeText(mAptName), closeSoftKeyboard());
        onView(withId(R.id.street_1)).perform(typeText(mStreet1), closeSoftKeyboard());
        onView(withId(R.id.street_2)).perform(typeText(mStreet2), closeSoftKeyboard());
        onView(withId(R.id.city)).perform(typeText(mCity), closeSoftKeyboard());
        onView(withId(R.id.state)).perform(typeText(mState), closeSoftKeyboard());
        onView(withId(R.id.zip_code)).perform(typeText(mZipCode), closeSoftKeyboard());

        //Check that all textviews are filled
        onView(withId(R.id.apartment_name)).check(matches(withText(mAptName)));
        onView(withId(R.id.street_1)).check(matches(withText(mStreet1)));
        onView(withId(R.id.street_2)).check(matches(withText(mStreet2)));
        onView(withId(R.id.city)).check(matches(withText(mCity)));
        onView(withId(R.id.state)).check(matches(withText(mState)));
        onView(withId(R.id.zip_code)).check(matches(withText(mZipCode)));

        Thread.sleep(1000);

        //Press create apaartment to create the apartment
        onView(withId(R.id.create_apt_btn)).perform(click());

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
        Person newUser = Person.getCurrentPerson();
        newUser.leaveApartment(null);
        newUser.deleteInBackground();
    }
}
