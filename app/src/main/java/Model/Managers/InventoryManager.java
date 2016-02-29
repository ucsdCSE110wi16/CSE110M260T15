package Model.Managers;

/**
 * Created by satre on 1/31/16.
 */

import android.util.Log;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import Model.Apartment;
import Model.Inventory;
import Model.InventoryItem;

/**
 * This class defined the business as well as model layer logic associated with {@code Inventory} and {@code InventoryItem}.
 * It enforces the singleton instance paradigm. The singleton instance can be accessed as a static variable.
 */
public class InventoryManager {
    /**
     * The singleton instance of this class.
     */
    public static final InventoryManager inventoryManager = new InventoryManager();

    /**
     * Default constructor, made private to enforce singleton.
     */
    private InventoryManager(){}

    public Inventory getInventory() {
        return ApartmentManager.apartmentManager.getCurrentApartment().getInventory();
    }


    /**
     * This method fetches the inventory and all of its items.
     * The result is stored in the run time variable and passed in the callback.
     * @param callback Function to call upon completion.
     */
    public void fetchInventoryItems(final FindCallback<InventoryItem> callback) {
        Apartment currentApartment = ApartmentManager.apartmentManager.getCurrentApartment();
        if (currentApartment == null) {
            return;
        }

        //get the inventory from the apartment
        final Inventory inventory = currentApartment.getInventory();
        //get the relation to InventoryItem
        ParseRelation<InventoryItem> itemRelation = inventory.getInventoryItemsRelation();

        //fetch the objects
        ParseQuery<InventoryItem> itemQuery =  itemRelation.getQuery();
        itemQuery.findInBackground(new FindCallback<InventoryItem>() {
            @Override
            public void done(List<InventoryItem> objects, ParseException e) {
                if (e == null && objects != null) {
                    inventory.setItems(new ArrayList<InventoryItem>(objects));
                }
                if (callback != null) {
                    callback.done(objects, e);
                }
            }
        });
    }

    public void fetchInventory( final GetCallback<Inventory> callback) {
        Apartment currentApartment = ApartmentManager.apartmentManager.getCurrentApartment();
        if( currentApartment == null) {
            return;
        }

        Inventory inventory = currentApartment.getInventory();

        inventory.fetchIfNeededInBackground(new GetCallback<Inventory>() {
            @Override
            public void done(Inventory object, ParseException e) {
                callback.done(object, e);
            }
        });
    }

    /**
     * Puts the given item in the given inventory.
     * @param item The object to store in the inventory.
     * @param inventory The container within which to store
     * @param callback
     */
    public void addItemToInventory( InventoryItem item, Inventory inventory, final SaveCallback callback) {
        ParseRelation<InventoryItem> itemsRelation = inventory.getInventoryItemsRelation();
        inventory.getItems().add(item);
        itemsRelation.add(item);
        inventory.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                callback.done(e);
            }
        });
    }

    /**
     * Deletes the given item from Parse.
     * @param item
     */
    public void deleteItem(InventoryItem item) {
        item.deleteInBackground();
        getInventory().getItems().remove(item);
        ParseRelation<InventoryItem> itemsRelation = getInventory().getInventoryItemsRelation();
        itemsRelation.remove(item);
        getInventory().saveInBackground();
    }
}
