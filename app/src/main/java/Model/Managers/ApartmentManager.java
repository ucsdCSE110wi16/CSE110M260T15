package Model.Managers;

/**
 * Created by satre on 1/31/16.
 */

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import Model.Apartment;
import Model.Inventory;
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


    /**
     * Deletes the current Apartment object completely. Note that deletion is only permitted if
     * there is one or none people in the apartment.
     * @return Indication of operation permissionc, boolean
     */
    public boolean deleteCurrentApartment() {
        if( currentApartment == null ) {
            return false;
        }

        if (currentApartment.getMembers().size() > 1) {
            return false;
        }

        currentApartment.deleteInBackground();
        return true;
    }

    /**
     * Fetches the apartment of the currently logged in user.
     * If there is no logged in user, method does nothing.
     */
    public void fetchApartment(final SaveCallback saveCallback) {
        Person user = Person.getCurrentPerson();

        if( user == null ) {
            return;
        }

        user.getApartment().fetchIfNeededInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                currentApartment = (Apartment) object;
                if (saveCallback != null) {
                    saveCallback.done(e);
                }
            }
        });
    }
}
