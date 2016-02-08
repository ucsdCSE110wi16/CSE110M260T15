package com.ucsd.cse110t15m260.roommateinventory;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.SaveCallback;

import java.util.List;

import Model.Apartment;
import Model.Person;

public class MainActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateInfo();
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
     * Updates the information on the screen
     */
    private void updateInfo() {
        Person person = Person.getCurrentPerson();

        TextView welcome = (TextView) findViewById(R.id.textview_welcome);
        Button apt = (Button) findViewById(R.id.button_apartment);
        Button createApt = (Button) findViewById(R.id.create_apartment);
        Button joinApt = (Button) findViewById(R.id.button_join);
        Button leave = (Button) findViewById(R.id.button_leave);
        Button login = (Button) findViewById(R.id.login);
        Button logout = (Button) findViewById(R.id.logout);

        apt.setVisibility(View.GONE);
        createApt.setVisibility(View.GONE);
        joinApt.setVisibility(View.GONE);
        leave.setVisibility(View.GONE);
        login.setVisibility(View.GONE);
        logout.setVisibility(View.GONE);


        if (person == null) {
            login.setVisibility(View.VISIBLE);
            welcome.setText("Welcome, user! Please log in.");
        } else {
            logout.setVisibility(View.VISIBLE);

            if (person.hasApartment()) {
                leave.setVisibility(View.VISIBLE);
                apt.setVisibility(View.VISIBLE);
            } else {
                createApt.setVisibility(View.VISIBLE);
                joinApt.setVisibility(View.VISIBLE);
            }

            welcome.setText(
                    "Welcome, " + person.getString("name") + "!\n" +
                            "Your User ID is: " + person.getObjectId() + "\n" +
                            "Your Apartment is: " + (person.hasApartment() ? person.getApartment().getObjectId() : null)
            );
        }
    }

    /**
     * Logs out
     */
    public void logout(View view) {
        Person person = Person.getCurrentPerson();
        TextView bye = (TextView) findViewById(R.id.textview_welcome);
        Button login = (Button) findViewById(R.id.login);
        Button createApt = (Button) findViewById(R.id.create_apartment);
        login.setVisibility(View.VISIBLE);
        createApt.setVisibility(View.GONE);
        if (person != null) {
            Snackbar.make(
                    findViewById(android.R.id.content),
                    "Goodbye, " + person.getString("name") + ".",
                    Snackbar.LENGTH_LONG
            ).show();
            bye.setText("Goodbye");
            Person.logoutPerson(new LogOutCallback() {
                @Override
                public void done(ParseException e) {
                    updateInfo();
                }
            });
        }

        onResume();
    }

    /**
     * Removes the current user from his/her apartment
     */
    public void leaveApartment(View view) {
        Person person = Person.getCurrentPerson();

        if (person != null) {
            person.leaveApartment(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    updateInfo();
                }
            });
        }
    }

    /**
     * Starts RegisterActivity
     */
    public void showLogin(View view) {
        Intent intent = new Intent(getBaseContext(), LoginActivity.class);
        startActivity(intent);
    }

    /**
     * Starts ApartmentActivity
     */
    public void showApartment(View view) {
        Intent intent = new Intent(getBaseContext(), ApartmentActivity.class);
        startActivity(intent);
    }

    /**
     * Starts CreateApartmentActivity
     */
    public void showCreateApartment(View view) {
        Intent intent = new Intent(getBaseContext(), CreateApartmentActivity.class);
        startActivity(intent);
    }

    /**
     * Creates virtual apartment
     */
    public void showJoinApartment(View view) {
        Intent intent = new Intent(getBaseContext(), JoinApartmentActivity.class);
        startActivity(intent);
    }
}
