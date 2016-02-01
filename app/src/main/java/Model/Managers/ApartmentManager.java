package Model.Managers;

/**
 * Created by satre on 1/31/16.
 */

import Model.Apartment;

/**
 * This class defines logic associated with the Apartment class. It also holds the single instance of the current Apartment object in memory.
 * This class also conforms to the singleton instance paradigm.
 */
public class ApartmentManager {

    public static final ApartmentManager apartmentManager = new ApartmentManager();

    private ApartmentManager(){}

    /**
     * The apartment that the user lives in. Note that it can be null if there is logged in user, or if he/she has not been added to an apartment.
     * WARNING: This is the ONLY place that the current apartment should be stored. No other class should have an instance var that holds a copy.
     */
    private Apartment currentApartment;


    public final Apartment getCurrentApartment() {
        return currentApartment;
    }

}
