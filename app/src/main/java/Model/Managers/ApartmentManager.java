package Model.Managers;

/**
 * Created by satre on 1/31/16.
 */

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.SaveCallback;

import java.util.List;

import Model.Apartment;
import Model.Person;

/**
 * This class defines logic associated with the Apartment class. It also holds the single instance of the current Apartment object in memory.
 * This class also conforms to the singleton instance paradigm.
 */
public class ApartmentManager {

    public static final ApartmentManager apartmentManager = new ApartmentManager();

    private ApartmentManager() {
    }

    /**
     * The apartment that the user lives in. Note that it can be null if there is no logged in user, or if he/she has not been added to an apartment.
     * WARNING: This is the ONLY place that the current apartment should be stored. No other class should have an instance var that holds a copy.
     */
//    private Apartment currentApartment;
    public final Apartment getCurrentApartment() {
        return Person.getCurrentPerson().getApartment();
    }

    /**
     * Adds the given person to the apartment
     *
     * @param newMate The person who is moving in.
     * @return True if the addition succeeds.
     */
    public boolean addPersonToCurrentApartment(Person newMate) {
        if (getCurrentApartment() == null) {
            return false;
        }

        return getCurrentApartment().addPersonToApartment(newMate);
    }

    /**
     * Removes the given person from the apartment.
     *
     * @param formerMate The person who is moving out.
     * @return True if the removal succeeds.
     */
    public void removePersonFromCurrentApartment(Person formerMate, SaveCallback callback) {
        if (getCurrentApartment() == null) {
            return;
        }

        getCurrentApartment().removePersonFromApartment(formerMate, callback);
    }

    public void fetchMembersOfApartment(final FindCallback<Person> callback) {
        Apartment apt = getCurrentApartment();
        apt.fetchMembersOfApartment(new FindCallback<Person>() {
            @Override
            public void done(List<Person> objects, ParseException e) {
                if (callback != null) {
                    callback.done(objects, e);
                }
            }
        });
    }


    /**
     * Deletes the current Apartment object completely. Note that deletion is only permitted if
     * there is one or none people in the apartment.
     *
     * @return Indication of operation permissionc, boolean
     */
    public boolean deleteCurrentApartment() {
        if (getCurrentApartment() == null) {
            return false;
        }

        if (getCurrentApartment().getMembers().size() > 1) {
            return false;
        }

        getCurrentApartment().deleteInBackground();
        return true;
    }

    /**
     * Fetches the apartment of the currently logged in user.
     * If there is no logged in user, method does nothing.
     */
    public void fetchApartment(final SaveCallback saveCallback) {
        Person user = Person.getCurrentPerson();
        if (user == null) {
            return;
        }

        if (user.getApartment() == null) {
            return;
        }

        user.getApartment().fetchIfNeededInBackground(new GetCallback<Apartment>() {
            @Override
            public void done(Apartment object, ParseException e) {
                if (saveCallback != null) {
                    saveCallback.done(e);
                }
            }
        });
    }
}
