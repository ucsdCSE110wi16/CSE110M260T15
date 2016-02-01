package com.ucsd.cse110t15m260.roommateinventory;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import Model.Apartment;
import Model.Person;

public class CreateApartmentActivity extends AppCompatActivity {

    private EditText mStreet1View;
    private EditText mStreet2View;
    private EditText mStateView;
    private EditText mCityView;
    private EditText mZipCodeView;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_apartment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Set up Address Form
        mStreet1View = (EditText) findViewById(R.id.street_1);
        mStreet2View = (EditText) findViewById(R.id.street_2);
        mStateView = (EditText) findViewById(R.id.state);
        mCityView = (EditText) findViewById(R.id.city);
        mZipCodeView = (EditText) findViewById(R.id.zip_code);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void createNewApartment(View view) {
        String street_1 = mStreet1View.getText().toString();
        String street_2 = mStreet2View.getText().toString();
        String state = mStateView.getText().toString();
        String city = mCityView.getText().toString();
        String zip_code = mZipCodeView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        /* Check that user filled in street_1, street_2 is optional */
        if (TextUtils.isEmpty(street_1)) {
            mStreet1View.setError("This field is required");
            focusView = mStreet1View;
            cancel = true;
        }

        /* Check that user filled in state */
        if (TextUtils.isEmpty(state)) {
            mStateView.setError("This field is required");
            focusView = mStateView;
            cancel = true;
        }
        /* Check that user filled in city */
        if (TextUtils.isEmpty(city)) {
            mCityView.setError("This field is required");
            focusView = mCityView;
            cancel = true;
        }

        /* Check that user filled in zip code */
        if (TextUtils.isEmpty(zip_code)) {
            mZipCodeView.setError("This field is required");
            focusView = mZipCodeView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            Apartment apt = Apartment.createApartment(street_1, street_2, state, city, zip_code);
            Person person = Person.getCurrentPerson();
            apt.addPersonToApartment(person);
            person.setApartment(apt);
            finish();
        }

    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "CreateApartment Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.ucsd.cse110t15m260.roommateinventory/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "CreateApartment Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.ucsd.cse110t15m260.roommateinventory/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
