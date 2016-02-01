package Model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseRelation;

/**
 * Created by satre on 1/31/16.
 */

@ParseClassName(Inventory.className)
public class Inventory extends ParseObject {
    public final static String className = "Inventory";

    /**
     * The run-time array that contains the items stored in this list.
     * Note that when this object is fetch, this variable is not populated by default,
     * it must be fetched separately.
     */
    protected InventoryItem[] items = new InventoryItem[0];

    /**
     * Getter for the name of this inventory list.
     * @return The name as a String.
     */
    public String getName() {
        return getString("name");
    }

    /**
     * Updates the name for this object.
     * @param newName The name to set.
     */
    public void setName(String newName) {
        put("name", newName);
    }

    /**
     * Accessor the returns the relation that holds the items stored in this object.
     * @return The relation, {@code ParseRelation<InventoryItem>}
     */
    public ParseRelation<InventoryItem> getInventoryItemsRelation() {
        ParseRelation<InventoryItem> items = getRelation("items");
        return items;
    }
}
