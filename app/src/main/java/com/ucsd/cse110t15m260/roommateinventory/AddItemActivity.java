package com.ucsd.cse110t15m260.roommateinventory;

import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.SaveCallback;

import java.text.ParseException;

import Model.InventoryItem;
import Model.Person;

public class AddItemActivity extends AppCompatActivity {

    // UI references
    private ImageView mItemImageView;
    private EditText mNameView;
    private EditText mCategoryView;
    private EditText mQuantityView;
    private EditText mDescriptionView;

    private TextView mUserName;

    private InventoryItem theItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mNameView = (EditText) findViewById(R.id.text_item_name);
        mCategoryView = (EditText) findViewById(R.id.text_item_category);
        mQuantityView = (EditText) findViewById(R.id.text_item_count);
        mDescriptionView = (EditText) findViewById(R.id.text_description);

        mUserName = (TextView) findViewById(R.id.text_username_created_by);

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            /* New item needs to be created */
            theItem = new InventoryItem();
        } else {
            String item = extras.getString("item");

        }


        /* Already existing item needs to be viewed */




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    public void attemptToCreateNewItem() {
        mNameView.setError(null);
        mCategoryView.setError(null);
        mQuantityView.setError(null);
        mDescriptionView.setError(null);

        boolean cancel = false;
        View focusView = null;

        String itemName = mNameView.getText().toString();
        String category = mNameView.getText().toString();
        Number quantity = (Number) mNameView.getText();
        String description = mNameView.getText().toString();

        if (TextUtils.isEmpty(itemName)) {
            mNameView.setError("This field is required");
            focusView = mNameView;
            cancel = true;
        }
        if (TextUtils.isEmpty(category)) {
            mCategoryView.setError("This field is required");
            focusView = mCategoryView;
            cancel = true;
        }
        if (quantity == null) {
            mQuantityView.setError("This field is required");
            focusView = mQuantityView;
            cancel = true;
        }

        if (cancel) {
            // One of the required fields wasn't entered
            focusView.requestFocus();
        } else {

            Log.d("AddItemActivity", "Creating a new inventory item");
            theItem = InventoryItem.createInventoryItem(itemName, category, quantity, description, Person.getCurrentPerson(), new SaveCallback() {
                @Override
                public void done(com.parse.ParseException e) {
                    if (e == null) {
                        //Hooray! Inventory item has been successfully created
                        finishCreateInventoryItem();
                    } else {
                        Log.e("createInventoryItem", e.toString());
                    }
                }
            });
        }
    }

    private void finishCreateInventoryItem() {

        // TODO: Set inventory item to be in current Inventory

        finish();
    }



    public void takeNewImageOnButtonClick() {


    }


}
