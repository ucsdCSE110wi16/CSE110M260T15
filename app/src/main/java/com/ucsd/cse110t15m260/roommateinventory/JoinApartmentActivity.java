package com.ucsd.cse110t15m260.roommateinventory;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import Model.Apartment;
import Model.Person;

public class JoinApartmentActivity extends AppCompatActivity {

    private EditText mApartmentIdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_apartment);

        this.setTitle("Join an Apartment");
    }

    public void joinApartment(View view) {

        mApartmentIdView = (EditText) findViewById(R.id.join_apartment_id);
        String apartmentId = mApartmentIdView.getText().toString();

        /* TODO: remove log statements (for debugging) */
        ParseQuery<Apartment> query = ParseQuery.getQuery("Apartment");
        query.getInBackground(apartmentId, new GetCallback<Apartment>() {
            @Override
            public void done(Apartment apartment, ParseException e) {
                Person person = Person.getCurrentPerson();

                if (!person.hasApartment()) {
                    if (e == null && apartment != null) {
                        Log.d("JoinApartmentActivity", "Apartment attempted to join (name): " + apartment.getName());

                        /* Person doesn't already have apartment */

                        // Set person's apartment to be apartment queried
                        person.setApartment(apartment);

                        // Set apartment to contain person
                        apartment.addPersonToApartment(person);
                        finish();
                    } else {
                        mApartmentIdView.setError("Incorrect PIN!");
                        mApartmentIdView.requestFocus();
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
