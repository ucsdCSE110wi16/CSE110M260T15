package com.ucsd.cse110t15m260.roommateinventory;

import android.support.test.annotation.Beta;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by satre on 3/6/16.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class UpdateInventoryItem {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void prepare() {

    }


    @Test
    public void updateQuantity() {
        
    }

    @After
    public void tearDown() {

    }
}
