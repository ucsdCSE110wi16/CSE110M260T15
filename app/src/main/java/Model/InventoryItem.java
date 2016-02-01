package Model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;

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


    /*****************************
     * Properties
     *****************************/

    /**
     * Returns the name of this item.
     * @return The name
     */
    public String getName() {
        return getString("name");
    }

    /**
     * Sets the name for this item.
     * @param newName
     */
    public void setName( String newName) {
        put("name", newName);
    }

    /**
     * Gets the quantity of this item. It is returned as a generic Number class, as whether the data type is an int or float can be dependent on the item.
     * @return The quantity registered for this item.
     */
    public Number getQuantity() {
        return getNumber("quantity");
    }

    /**
     * Update the
     * @param newQuantity The updated quantity to add to this item.
     */
    public void setQuantity(Number newQuantity) {
        put("quantity", newQuantity);
    }

    /**
     * Returns the ParseFile wrapper of the image of this item.
     * @return The image in its ParseFile wrapper.
     */
    public ParseFile getImageFile() {
        return getParseFile("image");
    }

    /**
     * Sets the image for this item. Also updates the run time image bitmap property and saves the object back to parse.
     * @param imageFile The image wrapped in a ParseFile object.
     */
    public void setImageFile(ParseFile imageFile) {
        put("image", imageFile);
        imageFile.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                saveInBackground();
            }
        });
        try {
            byte[] data = imageFile.getData();
            image = BitmapFactory.decodeByteArray(data, 0, data.length);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * **Asynchronously** fetches the image and stores it in the instance variable of this item.
     * @param callback The function to call upon completion of the download. The downloaded image is delivered in the callback.
     */
    public void fetchImageFile(final GetImageFromParseCallback callback) {
        ParseFile imageFile = getImageFile();

        imageFile.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] data, ParseException e) {
                image = BitmapFactory.decodeByteArray(data, 0, data.length);
                callback.done(image, e);
            }
        });
    }

    /**
     * Returns the image of this item. Note that it can be null.
     * @return
     */
    public Bitmap getImage() {
        return image;
    }
}
