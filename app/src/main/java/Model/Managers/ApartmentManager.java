package Model.Managers;

/**
 * Created by satre on 1/31/16.
 */

import Model.Apartment;
import Model.Person;

/**
 * This class defines logic associated with the Apartment class. It also holds the single instance of the current Apartment object in memory.
 * This class also conforms to the singleton instance paradigm.
 */
public class ApartmentManager {

    public static final ApartmentManager apartmentManager = new ApartmentManager();

    private ApartmentManager(){}

    /**
     * The apartment that the user lives in. Note that it can be null if there is no logged in user, or if he/she has not been added to an apartment.
     * WARNING: This is the ONLY place that the current apartment should be stored. No other class should have an instance var that holds a copy.
     */
    private Apartment currentApartment;


    public final Apartment getCurrentApartment() {
        return currentApartment;
    }

    /**
     * Adds the given person to the apartment
     * @param newMate The person who is moving in.
     * @return True if the addition succeeds.
     */
    public boolean addPersonToCurrentApartment(Person newMate)  {
        if (currentApartment == null ) {
            return false;
        }

        return currentApartment.addPersonToApartment(newMate);
    }

    /**
     * Removes the given person from the apartment.
     * @param formerMate The person who is moving out.
     * @return True if the removal succeeds.
     */
    public boolean removePersonFromCurrentApartment( Person formerMate) {
        if (currentApartment == null) {
            return false;
        }

        return currentApartment.removePersonFromApartment(formerMate);
    }

}
