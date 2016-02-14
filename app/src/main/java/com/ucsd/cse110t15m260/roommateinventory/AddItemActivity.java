package com.ucsd.cse110t15m260.roommateinventory;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseFile;
import com.parse.SaveCallback;

import java.nio.ByteBuffer;
import java.text.ParseException;

import Model.InventoryItem;
import Model.Person;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.view.View.*;

public class AddItemActivity extends AppCompatActivity {

    // UI references
    private ImageView mItemImageView;
    private EditText mNameView;
    private EditText mCategoryView;
    private EditText mQuantityView;
    private EditText mDescriptionView;
    private int CAMERA_ACTIVITY_INTENT_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;


    private TextView mUserName;
    private Uri imageFileUri;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

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
        fab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        //Register the listener for the captuer image button
        ImageButton imageButton = (ImageButton) findViewById(R.id.image_button);
        imageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
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


    private void launchCamera() {
        Intent cameraIntent = new Intent();
        cameraIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        //create a file to save the image result in
        imageFileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        //tell the activity where to store the image via the intent
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);

        startActivityForResult(cameraIntent, CAMERA_ACTIVITY_INTENT_CODE);
    }

    /** Create a file Uri for saving an image or video */
    private Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(String.valueOf(getBaseContext().getCacheDir()));

//        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_ACTIVITY_INTENT_CODE) {
            if (resultCode == RESULT_OK) {
                Bitmap image = processImageWithUri(data.getData());
                updateImageButtonWithImage(image);
                addImageToInventoryItem(image);
            } else if (resultCode == RESULT_CANCELED) {
                //user cancelled image capture
            } else {
                //image capture failed.
            }

        }
    }

    /**
     * Loads the image from the given file path and returns it.
     * @param imageUri The location of the file on disk.
     * @return The image bitmap.
     */
    private Bitmap processImageWithUri( Uri imageUri) {
        File imageFile = new File(imageUri.toString());
        Bitmap image = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        return image;
    }

    /**
     * Sets the image of the image button.
     * @param image
     */
    private void updateImageButtonWithImage( Bitmap image) {
        ImageButton imageButton = (ImageButton) findViewById(R.id.image_button);
        Bitmap scaledImage = Bitmap.createScaledBitmap(image, imageButton.getWidth(), imageButton.getHeight(), true);
        imageButton.setImageBitmap(scaledImage);
    }

    /**
     * Updates the inventory item to have the given image.
     * @param image the image to set.
     */
    private void addImageToInventoryItem( Bitmap image) {
        //create a new ParseFile for the item
        int byteCount = image.getByteCount();
        ByteBuffer buffer = ByteBuffer.allocate(byteCount);
        image.copyPixelsToBuffer(buffer);

        byte[] bytes = buffer.array();
        ParseFile imageFile = new ParseFile(theItem.getName(), bytes);
        theItem.setImageFile(imageFile);
    }
}
