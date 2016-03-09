package com.ucsd.cse110t15m260.roommateinventory;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

import Model.Apartment;
import Model.Managers.ApartmentManager;
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

        ParseQuery<Apartment> query = ParseQuery.getQuery(Apartment.className);
        query.getInBackground(apartmentId, new GetCallback<Apartment>() {
            @Override
            public void done(Apartment apartment, ParseException e) {
                Person person = Person.getCurrentPerson();

                if (!person.hasApartment()) {
                    if (e == null && apartment != null) {
                        // Set person's apartment to be apartment queried
                        person.setApartment(apartment);

                        // Set apartment to contain person
                        ApartmentManager.apartmentManager.addPersonToCurrentApartment(person);
                        ApartmentManager.apartmentManager.fetchMembersOfApartment(new FindCallback<Person>() {
                            @Override
                            public void done(List<Person> objects, ParseException e) {
                                finish();
                            }
                        });

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
