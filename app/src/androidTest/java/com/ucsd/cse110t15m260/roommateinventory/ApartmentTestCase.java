package com.ucsd.cse110t15m260.roommateinventory;

import android.support.test.espresso.contrib.DrawerActions;

import Model.InventoryItem;
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
 * Created by Isaac on 3/8/2016.
 */
public abstract class ApartmentTestCase {
    public String genFakeUser() {
        return String.valueOf(System.currentTimeMillis() / 10000) + "new@user.com";
    }

    public void createUser(String mName, String mEmail, String mPassword) throws InterruptedException {
        onView(withId(R.id.name)).perform(typeText(mName), closeSoftKeyboard());
        onView(withId(R.id.email)).perform(typeText(mEmail), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText(mPassword), closeSoftKeyboard());

        onView(withId(R.id.name)).check(matches(withText(mName)));
        onView(withId(R.id.email)).check(matches(withText(mEmail)));
        onView(withId(R.id.password)).check(matches(withText(mPassword)));
        onView(withId(R.id.email_register_button)).perform(click());

    }

    public void createApartment(String mAptName,
                                String mStreet1,
                                String mStreet2,
                                String mCity,
                                String mState,
                                String mZipCode) throws InterruptedException {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());

        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.my_apt));

        Thread.sleep(1000);
        // Type text and then press the button
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

    }

    public void addInventoryItem(String mItemName,
                                 String mQuanitity,
                                 String mItemCategory,
                                 String mDesciption) throws InterruptedException {
        Thread.sleep(1000);
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.apt_inventory));

        Thread.sleep(1000);

        onView(withId(R.id.addfab)).perform(click());

        onView(withId(R.id.text_item_name)).perform(typeText(mItemName), closeSoftKeyboard());
        onView(withId(R.id.text_item_quantity)).perform(typeText(mQuanitity), closeSoftKeyboard());
        onView(withId(R.id.text_item_category)).perform(typeText(mItemCategory), closeSoftKeyboard());
        onView(withId(R.id.text_description)).perform(typeText(mDesciption), closeSoftKeyboard());

        onView(withId(R.id.text_item_name)).check(matches(withText(mItemName)));
        onView(withId(R.id.text_item_category)).check(matches(withText(mItemCategory)));
        onView(withId(R.id.text_description)).check(matches(withText(mDesciption)));
        onView(withId(R.id.text_item_quantity)).check(matches(withText(mQuanitity)));
        onView(withId(R.id.fab)).perform(click());

        Thread.sleep(2000);
        //onView(withId(R.id.itemName)).check(matches(withText(mItemName)));
        //onView(withId(R.id.itemCount)).check(matches(withText(mQuanitity)));

        //onView(withId(R.id.incrementButton)).perform(click());
        //onView(withId(R.id.itemCount)).check(matches(withText(String.valueOf(q + 1))));
        //Thread.sleep(2000);

    }

    public void cleanUp() {
        Person.logOut();
        if (Person.getCurrentPerson() != null) {
            Person newUser = Person.getCurrentPerson();

            if (newUser.getApartment() != null) {
                newUser.leaveApartment(null);
            }

            newUser.deleteInBackground();
        }
    }

}
