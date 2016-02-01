package com.ucsd.cse110t15m260.roommateinventory;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import com.parse.ParseException;
import java.util.List;

import Model.Apartment;
import Model.Person;
public class JoinApartmentActivity extends AppCompatActivity {

    private EditText mJoinApartmentId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_apartment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void joinApartment(View view) {

        mJoinApartmentId = (EditText) findViewById(R.id.join_apartment_id);

        String apartment_id = mJoinApartmentId.getText().toString();

        /* TODO: remove log statements (for debugging) */
        ParseQuery<Apartment> query = ParseQuery.getQuery("Apartment");
        query.getInBackground("objectID", new GetCallback<Apartment>() {
            @Override
            public void done(Apartment apartment, ParseException e) {
                Person person = Person.getCurrentPerson();

                if (!person.hasApartment()) {
                    if (e == null && apartment != null) {
                        // object will be Apartment
                        Log.d("JoinApartmentActivity", "Apartment attempted to join (name): " + apartment.getName());

                    /* Person doesn't already have aparment */

                        // Set person's apartment to be apartment queried
                        person.setApartment(apartment);

                        // Set apartment to contain person
                        apartment.addPersonToApartment(person);
                        finish();
                    } else {

                        mJoinApartmentId.requestFocus();

                        Log.d("JoinApartmentActivity", "Apartment was returned as null");
                        Snackbar.make(
                                findViewById(android.R.id.content),
                                "You've entered an incorrect pin! Try again",
                                Snackbar.LENGTH_LONG
                        ).show();
                     }
                 } else {
                    Snackbar.make(
                            findViewById(android.R.id.content),
                            "You already have an apartment",
                            Snackbar.LENGTH_LONG
                    ).show();
                }
            }

        });
    }

}
