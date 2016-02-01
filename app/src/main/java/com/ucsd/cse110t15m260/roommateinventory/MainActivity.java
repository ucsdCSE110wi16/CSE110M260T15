package com.ucsd.cse110t15m260.roommateinventory;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import Model.Person;

public class MainActivity extends AppCompatActivity {


    private String[] mPages;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mPages = new String[3];
        mPages[0] = "main_fragment";
        mPages[1] = "second_fragment";
        mPages[2] = "third_fragment";//getResources().getStringArray();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.content_main, mPages));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    /** Swaps fragments in the main content view */
    private void selectItem(int position) {
        // Create a new fragment and specify the planet to show based on position
        Fragment fragment = new Fragment();
        Bundle args = new Bundle();
        //args.putInt(t.ARG_PLANET_NUMBER, position);
        fragment.setArguments(args);

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mPages[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Person person = Person.getCurrentPerson();

        TextView welcome = (TextView) findViewById(R.id.textview_welcome);
        Button createApt = (Button) findViewById(R.id.create_apartment);
        Button joinApt = (Button) findViewById(R.id.join_apartment_button);
        Button login = (Button) findViewById(R.id.login);
        Button logout = (Button) findViewById(R.id.logout);
        if (person == null) {
            joinApt.setVisibility(View.GONE);
            logout.setVisibility(View.GONE);
            createApt.setVisibility(View.GONE);
            login.setVisibility(View.VISIBLE);
            welcome.setText("Welcome, user! Please log in.");
        } else {
            login.setVisibility(View.GONE);
            logout.setVisibility(View.VISIBLE);
            createApt.setVisibility(View.VISIBLE);
            if(person.getApartment() == null) {
                joinApt.setVisibility(View.VISIBLE);
            }
            else {
                joinApt.setVisibility(View.GONE);
            }

            welcome.setText(
                    "Welcome, " + person.getString("name") + "!\n" +
                            "Your User ID is: " + person.getObjectId() + "\n" +
                            "Your Session Token is: " + person.getSessionToken() + "\n" +
                            "Your Apartment is: " + (person.hasApartment() ? person.getApartment().getObjectId() : null)
            );
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
            Person.logoutPerson();
        }

        onResume();
    }

    /**
     * Starts RegisterActivity
     */
    public void goToLogin(View view) {
        Intent intent = new Intent(getBaseContext(), LoginActivity.class);
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
