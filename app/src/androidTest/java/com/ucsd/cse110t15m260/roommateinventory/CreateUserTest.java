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
public class CreateUserTest {

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

        //mActivityRule.wait((long) 5000);
        //MainActivity mainActivity = (MainActivity) mActivityRule.getActivity().getBaseContext();

        //MaimDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        //mainActivity.mDrawerLayout.openDrawer(GravityCompat.START);

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());

        //onData(anything()).inAdapterView(withId(R.id.list_view)).atPosition(0).perform(click());
        //openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        //onView(withText("My Apartment")).perform(click());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.my_apt));

        Thread.sleep(1000);

        //Enter the create apartment activity
        onView(withId(R.id.create_apt)).perform(click());

        Thread.sleep(1000);

        //Typing in all required fields
        onView(withId(R.id.apartment_name)).perform(typeText(mAptName), closeSoftKeyboard());
        onView(withId(R.id.street_1)).perform(typeText(mStreet1), closeSoftKeyboard());
        onView(withId(R.id.street_2)).perform(typeText(mStreet2), closeSoftKeyboard());
        onView(withId(R.id.city)).perform(typeText(mCity), closeSoftKeyboard());
        onView(withId(R.id.state)).perform(typeText(mState), closeSoftKeyboard());
        onView(withId(R.id.zip_code)).perform(typeText(mZipCode), closeSoftKeyboard());

        onView(withId(R.id.apartment_name)).check(matches(withText(mAptName)));
        onView(withId(R.id.street_1)).check(matches(withText(mStreet1)));
        onView(withId(R.id.street_2)).check(matches(withText(mStreet2)));
        onView(withId(R.id.city)).check(matches(withText(mCity)));
        onView(withId(R.id.state)).check(matches(withText(mState)));
        onView(withId(R.id.zip_code)).check(matches(withText(mZipCode)));

        onView(withId(R.id.create_apt_btn)).perform(click());
        
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
