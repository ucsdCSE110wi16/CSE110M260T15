package Model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.parse.GetDataCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;

import Model.Managers.PushNotifsManager;

/**
 * Created by satre on 1/31/16.
 */
@ParseClassName(InventoryItem.className)
public class InventoryItem extends ParseObject {
    public final static String className = "InventoryItem";

    private Bitmap image;

    public InventoryItem() {
        super();
    }

    /**
     * Factory method to create a new item with all of its information.
     *
     * @param itemName    The name of the new item
     * @param category    The string category it belongs to
     * @param quantity    Arbitrary number.
     * @param description Optional description of the item.
     * @param person      The logged in user who created this item.
     * @param inventory   The container that this object is a part of.
     * @param sc          The save callback to call upon completion. Error is passed forward.
     * @return The constructed object.
     */
    public static InventoryItem createInventoryItem(String itemName, String category, Number quantity, String description, Person person, Inventory inventory, SaveCallback sc) {

        InventoryItem item = new InventoryItem();

        item.setName(itemName);
        item.setCategory(category);
        item.setQuantity(quantity);
        item.setDescription(description);
        item.setCreator(person);
        item.setInventory(inventory);
        item.saveInBackground(sc);

        return item;
    }

    public static InventoryItem createInventoryItemWithImage(String itemName, String category, Number quantity, String description, Person person, Inventory inventory, Bitmap image, SaveCallback sc) {
        InventoryItem item = new InventoryItem();

        item.setName(itemName);
        item.setCategory(category);
        item.setQuantity(quantity);
        item.setDescription(description);
        item.setCreator(person);
        item.setInventory(inventory);
        item.setImage(image);
        item.saveInBackground(sc);
        return item;
    }

    public static InventoryItem getInventoryItemById(String oid) {
        ParseQuery q = new ParseQuery(InventoryItem.class);

        try {
            return (InventoryItem) q.get(oid);
        } catch (Exception e) {
            Log.d("ParseException", e.getMessage());
        }

        return null;
    }

    /**
     * Creates an empty item.
     * **Note** This method DOES NOT save the empty object. This is to prevent empty objects from accumulating in the database.
     * In the event that the created object is actually used, it is up to the caller to save the object to the database.
     *
     * @param inventory The container to place the item in.
     * @return The constructed item.
     */
    public static InventoryItem createEmptyInventoryItem(Inventory inventory) {
        InventoryItem inventoryItem = new InventoryItem();

        inventoryItem.setInventory(inventory);
        return inventoryItem;
    }

    /*****************************
     * Properties
     *****************************/

    /**
     * Returns the inventory object that this item is a part of.
     *
     * @return The @code{Inventory} container.
     */
    public Inventory getInventory() {
        return (Inventory) getParseObject("inventory");
    }

    /**
     * Updates the inventory container that this object is a part of.
     * Method is private, as it should only be used at instantiation.
     *
     * @param inventory the container.
     */
    private void setInventory(Inventory inventory) {
        put("inventory", inventory);
    }

    /**
     * Returns the name of this item.
     *
     * @return The name
     */
    public String getName() {
        return getString("name");
    }

    /**
     * Sets the name for this item.
     *
     * @param newName
     */
    public void setName(String newName) {
        put("name", newName);
    }

    /**
     * Gets the category of this item.
     *
     * @return The category of this item.
     */
    public String getCategory() {
        return getString("category");
    }

    /**
     * Sets the category of this item.
     *
     * @param category The category of this item.
     */
    public void setCategory(String category) {
        put("category", category);
    }

    /**
     * Gets the quantity of this item. It is returned as a generic Number class, as whether the data type is an int or float can be dependent on the item.
     *
     * @return The quantity registered for this item.
     */
    public Number getQuantity() {
        return getNumber("quantity");
    }

    /**
     * Update the
     *
     * @param newQuantity The updated quantity to add to this item.
     */
    public void setQuantity(Number newQuantity) {
        if (newQuantity.doubleValue() <= 0) {
            newQuantity = new Integer(0);
        }
        put("quantity", newQuantity);
    }

    public void incrementQuantity() {
        int newQuantity = (int) this.getQuantity() + 1;
        this.setQuantity(newQuantity);

        // TODO: Refactor this into the controller to decouple the Notifs and the Item model
        if (newQuantity == 1)
            PushNotifsManager
                    .getInstance()
                    .sendReplenishedItem(this);
    }

    public void decrementQuantity() {
        int newQuantity = (int) this.getQuantity() - 1;

        if (newQuantity == 0)
            PushNotifsManager
                    .getInstance()
                    .sendOutOfItem(this);

        if (newQuantity >= 0)
            this.setQuantity(newQuantity);
    }

    /**
     * Get description field for this item as a String.
     *
     * @return The description of this item
     */
    public String getDescription() {
        return getString("description");
    }

    /**
     * Set the description field for this item.
     *
     * @param description The description of this item.
     */
    public void setDescription(String description) {
        if (description == null || description.equals("")) {
            put("description", "");
        } else {
            put("description", description);
        }
    }

    /**
     * Gets the creator of this item.
     *
     * @return The person that created this item.
     */
    public Person getCreator() {
        return (Person) getParseObject("creator");
    }

    /**
     * Sets the creator of the item.
     *
     * @param person The person that created this item.
     */
    public void setCreator(Person person) {
        put("creator", person);
    }

    /**
     * Returns the ParseFile wrapper of the image of this item.
     *
     * @return The image in its ParseFile wrapper.
     */
    public ParseFile getImageFile() {
        return getParseFile("image");
    }

    /**
     * Sets the image for this item. Also updates the run time image bitmap property and saves the object back to parse.
     *
     * @param imageFile The image wrapped in a ParseFile object.
     */
    public void setImageFile(ParseFile imageFile) {
        put("image", imageFile);
        imageFile.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Log.d("SETIMAGEFILE: ", "IMAGE SAVED");
                saveInBackground();
            }
        });
        //Makes the image not square and blocky
        try {
            byte[] data = imageFile.getData();
            //decodeByteArray(data(byte array of compressed image data),offset into data, # of bytes to parse))
            image = BitmapFactory.decodeByteArray(data, 0, data.length);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    //Creates a ParseFile to hold the image
    private ParseFile convertBitmapToParseFile(Bitmap image) {

        //create a new ParseFile for the item
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        this.image = image;
        if (byteArray != null) {
            ParseFile pf = new ParseFile("photo.jpg", byteArray);
            setImageFile(pf);
            return pf;
        }
        return null;
    }

    /**
     * **Asynchronously** fetches the image and stores it in the instance variable of this item.
     *
     * @param callback The function to call upon completion of the download. The downloaded image is delivered in the callback.
     */
    public void fetchImageFile(final GetImageFromParseCallback callback) {
        ParseFile imageFile = getImageFile();

        if (imageFile != null) {
            imageFile.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    image = BitmapFactory.decodeByteArray(data, 0, data.length);
                    if (callback != null)
                        callback.done(image, e);
                }
            });
        } else {
            Log.d("fetchImageFile:", "No Image");
        }

    }

    /**
     * Returns the image of this item. Note that it can be null.
     *
     * @return
     */
    public Bitmap getImage() {
        return image;
    }

    /**
     * Sets the image for this item. Also updates the bitmap property and saves the object back to parse.
     *
     * @param image The image to assign to this item.
     */
    public void setImage(Bitmap image) {
        this.image = image;
        ParseFile pf = convertBitmapToParseFile(image);
        setImageFile(pf);
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
