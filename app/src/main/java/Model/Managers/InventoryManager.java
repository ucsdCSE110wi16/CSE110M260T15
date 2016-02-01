package Model.Managers;

/**
 * Created by satre on 1/31/16.
 */

import Model.Inventory;

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



}
