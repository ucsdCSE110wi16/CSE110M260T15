package com.ucsd.cse110t15m260.roommateinventory;

import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class AddItemActivity extends AppCompatActivity {

    // UI references
    private ImageView mItemImageView;
    private EditText mNameView;
    private EditText mCategoryView;
    private EditText mQuantityView;
    private EditText mDescriptionView;

    private TextView mUserName;

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





        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    public void addItemToInventory() {
        mNameView.setError(null);
        mCategoryView.setError(null);
        mQuantityView.setError(null);
        mDescriptionView.setError(null);

        String itemName = mNameView.getText().toString();
        String category = mNameView.getText().toString();
        String quantity = mNameView.getText().toString();
        String description = mNameView.getText().toString();

        



    }

    public void takeNewImageOnButtonClick() {


    }


}
