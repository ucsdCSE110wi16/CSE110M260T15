package com.ucsd.cse110t15m260.roommateinventory;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.parse.LogOutCallback;
import com.parse.ParseException;

import Model.InventoryItem;
import Model.Managers.AccountManager;
import Model.Person;

public class MainActivity extends AbstractActivity implements NavigationView.OnNavigationItemSelectedListener, MainFragment.OnFragmentInteractionListener, ApartmentFragment.OnFragmentInteractionListener, InventoryFragment.OnFragmentInteractionListener {

    public DrawerLayout mDrawerLayout;
    public NavigationView navView;
    public Menu menuNav;
    public MenuItem aptInventory;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mTitle;
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        MainFragment fragger = null;
        Class<MainFragment> frag = MainFragment.class;
        try {
            fragger = frag.newInstance();
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        }
        android.support.v4.app.FragmentTransaction fragManager = getSupportFragmentManager().beginTransaction();
        fragManager.replace(R.id.content_frame, fragger);
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

        Log.d("OnPrepareOptionsMenu", "Preparing Menu");

        navView = (NavigationView) findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);

        Person person = Person.getCurrentPerson();

        Log.d("OnPrepareOptionsMenu", "SETTING HEADER TITLE");

        View header = navView.getHeaderView(0);
        TextView headerTitle = (TextView) header.findViewById(R.id.nav_header_title);
        TextView headerDescription = (TextView) header.findViewById(R.id.nav_header_description);
        menuNav = navView.getMenu();
        aptInventory = menuNav.findItem(R.id.apt_inventory);

        if(person != null) {
            headerTitle.setText(person.getString("name"));
            headerDescription.setText("User ID: " + person.getObjectId());

            //Hide the apt inventory if no apartment
            if (person.getApartment() == null)
                aptInventory.setVisible(false);
            else
                aptInventory.setVisible(true);
        }

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

        Person person = Person.getCurrentPerson();

        android.support.v4.app.FragmentTransaction fragManager = getSupportFragmentManager().beginTransaction();

        if (id == R.id.home_page) {
            MainFragment mainFrag = null;
            Class<MainFragment> frag = MainFragment.class;
            try {
                mainFrag = frag.newInstance();
            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            fragManager.replace(R.id.content_frame, mainFrag);
            fragManager.commit();
        } else if (id == R.id.my_apt) {
            ApartmentFragment aptFrag = null;
            Class<ApartmentFragment> apt = ApartmentFragment.class;
            try {
                aptFrag = apt.newInstance();
            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            fragManager.replace(R.id.content_frame, aptFrag);
            fragManager.commit();
        } else if (id == R.id.apt_inventory) {
            Log.d("OnNavigation", "INVENTORY");
            if (person.hasApartment()) {
                InventoryFragment inventoryFrag = null;
                Class<InventoryFragment> inv = InventoryFragment.class;
                try {
                    inventoryFrag = inv.newInstance();
                } catch (Exception e) {
                    Log.d("Exception", e.toString());
                }
                fragManager.replace(R.id.content_frame, inventoryFrag);
                fragManager.commit();
            } else {
                Toast.makeText(MainActivity.this, "You are not in an apartment!", Toast.LENGTH_SHORT).show();
                aptInventory.setChecked(false);
            }
        } else if (id == R.id.nav_logout) {
            Log.d("OnNavigation", "LOGOUT PRESSED");
            logout();
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

        if (Person.getCurrentPerson() == null) {
            showLogin();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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

    public void showLogin() {
        Intent intent = new Intent(getBaseContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void logout() {
        AccountManager.accountManager.logoutPerson(new LogOutCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    showLogin();
                    finish();
                } else {
                    Log.d("LOGOUT", e.toString());
                }
            }
        });

    }

    public void onFragmentInteraction(Uri uri) {
        Log.d("Ok", "Wat is lyfe");
    }
}
