package Model.Managers;

/**
 * Created by satre on 1/31/16.
 */

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

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

    private Apartment apartment;

    /**
     * Default constructor, made private to enforce singleton.
     */
    private InventoryManager(){}


    /**
     * This method fetches the inventory and all of its items.
     * The result is stored in the run time variable and passed in the callback.
     * @param callback Function to call upon completion.
     */
    public void fetchInventory(final FindCallback<InventoryItem> callback) {
        //get the inventory from the apartment
        final Inventory inventory = apartment.getInventory();
        //get the relation to InventoryItem
        ParseRelation<InventoryItem> itemRelation = inventory.getInventoryItemsRelation();

        //fetch the objects
        ParseQuery<InventoryItem> itemQuery =  itemRelation.getQuery();
        itemQuery.findInBackground(new FindCallback<InventoryItem>() {
            @Override
            public void done(List<InventoryItem> objects, ParseException e) {
                if (e == null && objects != null) {
                    inventory.items = objects;
                }
                callback.done(objects, e);
            }
        });
    }

}
