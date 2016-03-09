package Model;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by satre on 1/31/16.
 */

@ParseClassName(Inventory.className)
public class Inventory extends ParseObject {
    public final static String className = "Inventory";
    /**
     * The run-time array that contains the items stored in this list.
     * Note that when {@code this} object is fetched, the variable is not populated by default,
     * it must be fetched separately.
     */
    private ArrayList<InventoryItem> items = new ArrayList<InventoryItem>();

    public Inventory() {
        super();
    }

    //Inventory "Factory" constructor
    public static Inventory createNewInventoryWithName(String name) {
        Inventory inventory = new Inventory();
        inventory.setName(name);
        inventory.saveInBackground();
        return inventory;
    }

    public ArrayList<InventoryItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<InventoryItem> items) {
        this.items = items;
    }

    /**
     * Getter for the name of this inventory list.
     *
     * @return The name as a String.
     */
    public String getName() {
        return getString("name");
    }

    /**
     * Updates the name for this object.
     *
     * @param newName The name to set.
     */
    public void setName(String newName) {
        put("name", newName);
    }

    /**
     * Accessor the returns the relation that holds the items stored in this object.
     *
     * @return The relation, {@code ParseRelation<InventoryItem>}
     */
    public ParseRelation<InventoryItem> getInventoryItemsRelation() {
        ParseRelation<InventoryItem> items = getRelation("items");
        return items;
    }

    /**
     * Fetches the items in this inventory and stores them in the run time array.
     * Upon completion, the callback is called. The items and error (if existent) are delivered via the callback.
     *
     * @param callback {@code FindCallback<InventoryItem>}
     */
    public void fetchInventoryItems(final FindCallback<InventoryItem> callback) {
        ParseQuery<InventoryItem> itemQuery = getInventoryItemsRelation().getQuery();
        itemQuery.orderByAscending("quantity");

        itemQuery.findInBackground(new FindCallback<InventoryItem>() {
            @Override
            public void done(List<InventoryItem> objects, ParseException e) {
                if (e == null && objects != null) {
                    items = new ArrayList<InventoryItem>(objects);
                }
                if (callback != null) {
                    callback.done(objects, e);
                }
            }
        });

    }
}
