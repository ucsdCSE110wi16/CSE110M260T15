package com.ucsd.cse110t15m260.roommateinventory;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Locale;

import Model.Person;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Person person = Person.getCurrentPerson();

        if(person == null)
        {
            Intent intent = new Intent(getBaseContext(), LoginActivity.class);
            startActivity(intent);
        }

        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle("Home Page");
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Home Page");
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);
        Menu menuNav = navView.getMenu();
        MenuItem logoutItem = menuNav.findItem(R.id.nav_logout);
        if (person == null) {
            logoutItem.setVisible(false);
        }
        Fragment frag = new Frag();
        FragmentTransaction fragManager = getFragmentManager().beginTransaction();
        fragManager.replace(R.id.content_frame, frag);
        fragManager.commit();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Log.d("ok", "right here");
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.d("ok", "case home");
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            Log.d("ok", "drawerToggle");
            return true;
        }
        // Handle your other action bar items...

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = new Frag();

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getFragmentManager();

        if (id == R.id.nav_first) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();
        } else if (id == R.id.nav_second) {
            Log.d("OnNavigation", "GALLERY PRESSED");
        } else if (id == R.id.nav_third) {
            Log.d("OnNavigation", "CREATE APARTMENT");
            Intent intent = new Intent(getBaseContext(), CreateApartmentActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_fourth) {
            Log.d("OnNavigation", "JOIN APARTMENT");
            Intent intent = new Intent(getBaseContext(), JoinApartmentActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_login) {
            Log.d("OnNavigation", "LOGIN PRESSED");
            Intent intent = new Intent(getBaseContext(), LoginActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            Log.d("OnNavigation", "LOGOUT PRESSED");
            Person person = Person.getCurrentPerson();
            TextView bye = (TextView) findViewById(R.id.textview_welcome);
            if (person != null) {
                Snackbar.make(
                        findViewById(android.R.id.content),
                        "Goodbye, " + person.getString("name") + ".",
                        Snackbar.LENGTH_LONG
                ).show();
                bye.setText("Goodbye");
                Person.logoutPerson();
            }
            Intent intent = new Intent(getBaseContext(), LoginActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Person person = Person.getCurrentPerson();

        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);
        Menu menuNav = navView.getMenu();
        MenuItem logoutItem = menuNav.findItem(R.id.nav_logout);
        MenuItem loginItem = menuNav.findItem(R.id.nav_login);
        if (person == null) {
            logoutItem.setVisible(false);
            loginItem.setVisible(true);
        }
        else {
            loginItem.setVisible(false);
            logoutItem.setVisible(true);
        }
        TextView text = (TextView) findViewById(R.id.textview_welcome);
        text.setText("Hello");
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
     * Fragment that appears in the "content_frame"
     */
    public static class Frag extends Fragment {
        public static final String FRAG_NUM = "fragment_number";

        public Frag() {
            // Empty constructor required for fragment subclasses
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            //Person person = Person.getCurrentPerson();

            TextView text = (TextView) rootView.findViewById(R.id.textview_welcome);
            text.setText("Hello");
            getActivity().setTitle("Home Page");
            return rootView;
        }


    }
}

/*
            TextView welcome = (TextView) rootView.findViewById(R.id.textview_welcome);
            Button createApt = (Button) rootView.findViewById(R.id.create_apartment);
            Button joinApt = (Button) rootView.findViewById(R.id.join_apartment_button);
            Button login = (Button) rootView.findViewById(R.id.login);
            Button logout = (Button) rootView.findViewById(R.id.logout);
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
                if (person.getApartment() == null) {
                    joinApt.setVisibility(View.VISIBLE);
                } else {
                    joinApt.setVisibility(View.GONE);
                }

                welcome.setText(
                        "Welcome, " + person.getString("name") + "!\n" +
                                "Your User ID is: " + person.getObjectId() + "\n" +
                                "Your Session Token is: " + person.getSessionToken() + "\n" +
                                "Your Apartment is: " + (person.hasApartment() ? person.getApartment().getObjectId() : null)
                );
            }
*/
