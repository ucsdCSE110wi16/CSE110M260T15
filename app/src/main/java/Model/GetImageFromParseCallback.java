package Model;
/**
 * Created by satre on 1/31/16.
 */

import android.graphics.Bitmap;

import com.parse.ParseException;

/**
 * Use this interface to respond to functions that fetch images from Parse.
 * <p/>
 * When a file is fetched from Parse it is delivered as a byte array, which is then turned into a bitmap image.
 * The best way to use this class is to pass it as an anonymous inner class. When creating methods that use this class, simply define a parameter that takes an object of this type.
 * Override the {@code done} function to receive the image and an error object, if there was one.
 * </p>
 *
 * <p/>
 * When receiving the object, always null-check the error first. If the exception is not null, handle accordingly.
 * </p>
 */
public interface GetImageFromParseCallback {

    /**
     * The function that is called upon completion of the download and image construction.
     * @param downloadedImage The fetched image.
     * @param e Download error, if there was one.
     */
    public void done( Bitmap downloadedImage, ParseException e);
}
