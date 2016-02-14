package com.ucsd.cse110t15m260.roommateinventory;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseInstallation;

import Model.Apartment;
import Model.Inventory;
import Model.InventoryItem;
import Model.Managers.AccountManager;
import Model.Person;

/**
 * Created by Kevin on 1/27/2016.
 */
public class App extends Application {
    private static final String APPLICATION_ID = "uymjS3lXDmOqNv0IPYhLS2HFhkzoVhLaCyVAyM6o";
    private static final String CLIENT_KEY = "2CTlHXNSyVRYSRJ0l9OUuXNgQUX5MTwZyNvYCgzX";

    @Override
    /**
     * Performs Parse initialization stuff apart from MainActivity in order to avoid multiple
     * initializations, which causes crashes.
     */
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Person.class);
        ParseObject.registerSubclass(Apartment.class);
        ParseObject.registerSubclass(InventoryItem.class);
        ParseObject.registerSubclass(Inventory.class);

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, APPLICATION_ID, CLIENT_KEY);
        AccountManager.accountManager.fetchAllData();
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}
