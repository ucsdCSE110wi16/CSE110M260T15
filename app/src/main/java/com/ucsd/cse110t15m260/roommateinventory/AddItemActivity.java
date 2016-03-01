package com.ucsd.cse110t15m260.roommateinventory;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Typeface;
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
import android.widget.Button;
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
    private EditText mNameView;
    private EditText mCategoryView;
    private EditText mQuantityView;
    private EditText mDescriptionView;
    private int CAMERA_ACTIVITY_INTENT_CODE = 100;

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    public static final int RESULT_OK = 9;

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
        mQuantityView = (EditText) findViewById(R.id.text_item_quantity);
        mDescriptionView = (EditText) findViewById(R.id.text_description);

        int index =  getIntent().getIntExtra("index", -1);

        Log.d("AddItemActivity", "The intent passed " + index);
        TextView createdByTextView = (TextView) findViewById(R.id.text_created_by);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        TextView usernameTextView = (TextView) findViewById(R.id.text_username_created_by);

        EditText itemName = (EditText) findViewById(R.id.text_item_name);
        EditText itemCategory = (EditText) findViewById(R.id.text_item_category);
        EditText itemQuantity = (EditText) findViewById(R.id.text_item_quantity);
        EditText itemDescription = (EditText) findViewById(R.id.text_description);

        //Register the listener for the capture image button
        ImageButton imageButton = (ImageButton) findViewById(R.id.image_button);
        imageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
            }
        });

        Button deleteItemButton = (Button) findViewById(R.id.delete_item_button);
        deleteItemButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(theItem != null)
                InventoryManager.inventoryManager.deleteItem(theItem);
                passItemBackToCallingActivity();
                finish();
            }
        });

        if(index == -1) {
            /* Creation Mode: New item needs to be created */
            theItem = InventoryItem.createEmptyInventoryItem(ApartmentManager.apartmentManager.getCurrentApartment().getInventory());

            itemName.setFocusable(true);
            itemName.setEnabled(true);
            itemCategory.setFocusable(true);
            itemCategory.setEnabled(true);
            itemDescription.setFocusable(true);
            itemDescription.setEnabled(true);
            itemQuantity.setFocusable(true);
            itemQuantity.setEnabled(true);

            fab.setVisibility(VISIBLE);
            createdByTextView.setVisibility(GONE);
            usernameTextView.setVisibility(GONE);
            deleteItemButton.setVisibility(GONE);

            /* Create item button on click listener */
            fab.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    attemptToCreateNewItem();
                }
            });

            this.setTitle("Create New Item");

        } else {

            /* Viewing Mode: item already exists. */
            theItem = InventoryManager.inventoryManager.getInventory().getItems().get(index);

            /* Make the EditTexts appear to be TextViews */
            itemName.setInputType(0);
            itemName.getBackground().clearColorFilter();
            itemName.setFocusable(false);
            itemName.setClickable(false);

            itemCategory.setInputType(0);
            itemCategory.getBackground().clearColorFilter();
            itemCategory.setFocusable(false);
            itemCategory.setClickable(false);

            itemQuantity.setInputType(0);
            itemQuantity.getBackground().clearColorFilter();
            itemQuantity.setFocusable(false);
            itemQuantity.setClickable(false);

            itemDescription.setInputType(0);
            itemDescription.getBackground().clearColorFilter();
            itemDescription.setFocusable(false);
            itemDescription.setClickable(false);

            /* Set the items text fields */
            itemName.setText(theItem.getName());
            itemCategory.setText(theItem.getCategory());
            itemQuantity.setText(theItem.getQuantity().toString());
            itemDescription.setText(theItem.getDescription());

            /* Set image of item */
            if(theItem.getImage() != null) {
                updateImageButtonWithImage(theItem.getImage());
            }

            /* Hide fab, show created by, username, and delete button */
            fab.setVisibility(GONE);
            createdByTextView.setVisibility(VISIBLE);
            usernameTextView.setVisibility(VISIBLE);
            deleteItemButton.setVisibility(VISIBLE);

            if(theItem.getCreator() != null) {
                usernameTextView.setText(theItem.getCreator().getUsername());
            }

            this.setTitle("Add an Item");
        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();


    }

    private void passItemBackToCallingActivity() {
        Intent passDataBack = new Intent();
        passDataBack.putExtra("item", theItem.getObjectId());
        setResult(RESULT_OK, passDataBack);
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
        String quantityString = mQuantityView.getText().toString();
        Number quantity;
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
        if ( TextUtils.isEmpty(quantityString)) {
            mQuantityView.setError("This field is required");
            focusView = mQuantityView;
            cancel = true;
            quantity = new Integer(0);
        } else {
            quantity = Integer.parseInt(quantityString);
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
        Apartment apartment = ApartmentManager.apartmentManager.getCurrentApartment();
        InventoryManager.inventoryManager.addItemToInventory(theItem, apartment.getInventory(), new SaveCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                if (e == null) {
                    Toast.makeText(AddItemActivity.this, "New Item Created", Toast.LENGTH_SHORT).show();
                    passItemBackToCallingActivity();
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
                Intent i;
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
