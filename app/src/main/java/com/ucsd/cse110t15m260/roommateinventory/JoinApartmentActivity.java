package com.ucsd.cse110t15m260.roommateinventory;

import android.os.Bundle;
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
        query.whereEqualTo("id", apartment_id);
        query.findInBackground(new FindCallback<Apartment>() {
            public void done(List<Apartment> apartments, ParseException e) {
                if (e == null) {
                    // object will be Apartment
                    Log.d("JoinApartmentActivity", "Apartment attempted to join (name): " + apartments.get(0).getName());
                } else {
                    // something went wrong
                    Log.d("JoinApartmentActivity", "id was returned as null");

                }
            }
        });

        /* TODO: Set apartment to have current user */

    }

}
