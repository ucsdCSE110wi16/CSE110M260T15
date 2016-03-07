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

        Thread.sleep(5000);


        /*
        long currentTime = System.currentTimeMillis();
        while(System.currentTimeMillis() - currentTime == 10000) {
            Log.d("CreateUserTest", "WAITING");
        }
        */
    }

    @Test
    public void deleteUser() {


    }


    @After
    public void tearDown() throws Exception {
        ParseUser newUser = getCurrentUser();

        newUser.deleteInBackground();
    }
}
