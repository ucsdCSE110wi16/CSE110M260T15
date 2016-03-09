package com.ucsd.cse110t15m260.roommateinventory;

import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import Model.Apartment;
import Model.InventoryItem;
import Model.Managers.InventoryManager;
import Model.Person;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.action.ViewActions.typeTextIntoFocusedView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Isaac Diamond on 3/8/2016.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class CreateApartmentCreateItemScenario extends ApartmentTestCase {
    @Rule
    public ActivityTestRule<RegisterActivity> mActivityRule = new ActivityTestRule<>(RegisterActivity.class);
    public String mItemName;
    public String mItemCategory;
    public String mQuanitity;
    public String mDesciption;
    public String mEmail;
    public String mPassword;
    public String mName;
    public String mAptName;
    public String mStreet1;
    public String mStreet2;
    public String mCity;
    public String mState;
    public String mZipCode;

    @Before
    public void initItem() {
        mItemName = "Fake Item";
        mItemCategory = "Fake Category";
        mQuanitity = "100";
        mDesciption = "It's not real!";
    }

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
    public void addItem() throws InterruptedException {
        initItem();
        initValidString();

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
        onView(withContentDescription("Navigate up")).perform(click());

        addInventoryItem(
                mItemName,
                mQuanitity,
                mItemCategory,
                mDesciption
        );

        Thread.sleep(1000);

        InventoryItem i = Person.getCurrentPerson().getApartment().getInventory().getItems().get(0);
        Assert.assertEquals(i.getName(), mItemName);
        Assert.assertEquals(String.valueOf(i.getQuantity()), mQuanitity);
        Assert.assertEquals(i.getCategory(), mItemCategory);
        Assert.assertEquals(i.getDescription(), mDesciption);

    }

    @After
    public void tearDown() throws Exception {
        cleanUp();
    }
}
