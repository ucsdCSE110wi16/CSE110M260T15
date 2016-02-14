package com.ucsd.cse110t15m260.roommateinventory;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.List;

import Model.Apartment;
import Model.Inventory;
import Model.InventoryItem;
import Model.Person;

public class MainActivity extends AbstractActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
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

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();


        ParseUser.getCurrentUser().logOut();
        if(ParseUser.getCurrentUser() == null){
            Person.loginPerson("leo@leo.com","leowong",new LogInCallback() {
                public void done(ParseUser user, ParseException e) {
                    if (e == null && user != null) {
                        Apartment apartment = (Apartment)ParseUser.getCurrentUser().get("apartment");
                        ParseRelation aRelation = (ParseRelation)apartment.getUserRelation();
                        aRelation.add(ParseUser.getCurrentUser());
                        apartment.fetchIfNeededInBackground(new GetCallback<ParseObject>() {
                            @Override
                            public void done(ParseObject object, ParseException e) {
                                Apartment apartment =(Apartment)ParseUser.getCurrentUser().get("apartment");
                                final Inventory inventory = (Inventory) apartment.get("inventory");
                                InventoryItem item = new InventoryItem();
                                item.setName("Banana");
                                item.setQuantity(5);

                                try {
                                    item.save();
                                } catch (ParseException e1) {
                                    e1.printStackTrace();
                                }
                                inventory.getInventoryItemsRelation().add(item);
                                inventory.saveInBackground();
                                ParseUser.getCurrentUser().saveInBackground();
                                inventory.fetchIfNeededInBackground();
                                ParseQuery<InventoryItem> itemQuery = inventory.getInventoryItemsRelation().getQuery();
                                itemQuery.orderByAscending("quantity");

                                itemQuery.findInBackground(new FindCallback<InventoryItem>() {
                                    @Override
                                    public void done(List<InventoryItem> objects, ParseException e) {
                                        if (e == null && objects != null) {
                                            inventory.items = objects;
                                        }

                                       // callback.done(objects, e);
                                    }
                                });

                            }
                        });

                    } else if (user == null) {
                        System.out.println("error" + e);
                    } else {
                        System.out.println("error");
                    }
                }
            });
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        Person person = Person.getCurrentPerson();

        TextView welcome = (TextView) findViewById(R.id.textview_welcome);

        if (person == null) {
            welcome.setText("Welcome, user! Please log in.");
        } else {
            welcome.setText(
                    "Welcome, "+ person.getString("name") + "!\n" +
                    "Your User ID is: " + person.getObjectId() + "\n" +
                    "Your Session Token is: " + person.getSessionToken() + "\n" +
                    "Your Apartment is: " + (person.getApartment() == null ? null : person.getApartment().toString())
            );

            Snackbar.make(
                    findViewById(android.R.id.content),
                    "Welcome, " + person.getString("name") + "!",
                    Snackbar.LENGTH_LONG
            ).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void goToInventory(View view) {
        Intent intent = new Intent(getBaseContext(), InventoryActivity.class);
        startActivity(intent);

        finish();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
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
                "Main Page", // TODO: Define a title for the content shown.
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

    /**
     * Logs out
     */
    public void logout(View view) {
        Person person = Person.getCurrentPerson();

        if (person != null) {
            Snackbar.make(
                    findViewById(android.R.id.content),
                    "Goodbye, " + person.getString("name") + ".",
                    Snackbar.LENGTH_LONG
            ).show();

            Person.logoutPerson();
        }
    }

    /**
     * Starts RegisterActivity
     */
    public void goToLogin(View view) {
        Intent intent = new Intent(getBaseContext(), LoginActivity.class);
        startActivity(intent);
    }

}
