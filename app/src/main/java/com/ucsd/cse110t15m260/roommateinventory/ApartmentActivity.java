package com.ucsd.cse110t15m260.roommateinventory;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

import Model.Person;

public class ApartmentActivity extends AppCompatActivity {

    List<String> people;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apartment);

        people = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, people);
        ((ListView) findViewById(R.id.listView)).setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Person person = Person.getCurrentPerson();

        if (person != null && person.hasApartment()) {
            person.getApartment().findMembers(new FindCallback<Person>() {
                @Override
                public void done(List<Person> objects, ParseException e) {
                    if (e == null) {
                        for (Person p: objects) {
                            people.add(p.toString());
                        }

                        Log.d("PEOPLE_LIST", people.toString());
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.d("PEOPLE_LIST", e.toString());
                    }
                }
            });
        }
    }
}
