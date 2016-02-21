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
import android.speech.tts.TextToSpeech;
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
import android.widget.Toast;

import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.text.ParseException;

import Model.Apartment;
import Model.Inventory;
import Model.InventoryItem;
import Model.Managers.ApartmentManager;
import Model.Managers.InventoryManager;
import Model.Person;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.view.View.*;

public class AddItemActivity extends AbstractActivity {

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

        /* TODO: Make sure it's passed on the other end as InventoryItem  */
        Serializable item =  getIntent().getSerializableExtra("InventoryItem");
        TextView createdByTextView = (TextView) findViewById(R.id.text_created_by);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        TextView usernameTextView = (TextView) findViewById(R.id.text_username_created_by);
        if(item == null) {
            /* Creation Mode: New item needs to be created */
            theItem = InventoryItem.createEmptyInventoryItem(ApartmentManager.apartmentManager.getCurrentApartment().getInventory());

            fab.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    attemptToCreateNewItem();
                }
            });

            createdByTextView.setVisibility(GONE);
            usernameTextView.setVisibility(GONE);
            fab.setVisibility(VISIBLE);

        } else {
            /* Viewing Mode: item already exists. */
            theItem = (InventoryItem) item;
            fab.setVisibility(GONE);
            createdByTextView.setVisibility(VISIBLE);
            usernameTextView.setVisibility(VISIBLE);

            if(theItem.getCreator() != null) {
                usernameTextView.setText(theItem.getCreator().getUsername());
            }

        }


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
        String category = mCategoryView.getText().toString();
        Number quantity = Integer.parseInt(mQuantityView.getText().toString());
        String description = mDescriptionView.getText().toString();

        Log.d("AddItemActivity", "Before checking fields");
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
            Person person = Person.getCurrentPerson();
            Inventory aptInventory = ApartmentManager.apartmentManager.getCurrentApartment().getInventory();
            theItem = InventoryItem.createInventoryItem(itemName, category, quantity, description, person, aptInventory, new SaveCallback() {
                @Override
                public void done(com.parse.ParseException e) {
                    if (e == null) {
                        Log.d("AddItemActivity", "Finishing creating item");
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
        Apartment apartment = ApartmentManager.apartmentManager.getCurrentApartment();
        InventoryManager.inventoryManager.addItemToInventory(theItem, apartment.getInventory(), new SaveCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                if (e == null) {
                    Toast.makeText(AddItemActivity.this, "New Item Created", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddItemActivity.this, "Error Occured: please try again.", Toast.LENGTH_SHORT).show();

                }
            }
        });

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
                Bitmap scaledImage = scaleImageToResolution(2000, 2000, image);
                updateImageButtonWithImage(scaledImage);

                //save the image to the object.
                theItem.setImage(scaledImage);
            } else if (resultCode == RESULT_CANCELED) {
                //user cancelled image capture
            } else {
                //image capture failed.
                Toast.makeText(AddItemActivity.this, "Error with image. Please try again.", Toast.LENGTH_SHORT).show();
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
     * Scales the image to the given resolution and height
     * @param resWidth The width to scale to
     * @param resHeight The height to scale to.
     * @param unscaledImage The image to scale
     * @return The scaled image.
     */
    private Bitmap scaleImageToResolution(int resWidth, int resHeight, Bitmap unscaledImage) {
        return Bitmap.createScaledBitmap(unscaledImage, resWidth, resHeight, true);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "AddItem Page", // TODO: Define a title for the content shown.
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
                "AddItem Page", // TODO: Define a title for the content shown.
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
}
