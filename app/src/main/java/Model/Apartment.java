package Model;

<<<<<<< HEAD
import android.util.Log;

import com.parse.*;
=======
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseRelation;
>>>>>>> develop

/**
 * Created by satre on 1/23/16.
 */
@ParseClassName(Apartment.className)
public class Apartment extends ParseObject {
    public final static String className = "Apartment";

    /**
     * Default Constructor
     */
    public Apartment() {
        super();
    }


    public Apartment(String apartment_id, String apartment_name, String street_1, String street_2, String state, String city, String zip_code) {

        Log.d("Apartment", "creating apartment");
        Apartment apartment = new Apartment();

        apartment.add("id", apartment_id);
        apartment.add("name", apartment_name);
        apartment.add("street_1", street_1);
        apartment.add("street_2", street_2);
        apartment.add("state", state);
        apartment.add("city", city);
        apartment.add("zip_code", zip_code);

        apartment.saveInBackground();
    }

    /***********************
     * Properties
     */


    /**
     * Fetches the name of this apartment
     * @return String
     */
    public String getName() {
        return getString("name");
    }

    /**
     * Updates the name of this apartment to the given value.
     * @param newName, the name to set.
     */
    public void setName(String newName){
        put("name", newName);
    }

    /**
     * Get the relation to User class that contains members of this apartment.
     * @return ParseRelation
     */
   public ParseRelation<Person> getUserRelation() {

       ParseRelation<Person> roomies = getRelation("users");
       return roomies;
   }

    /**
     * Adds the given person the relation that contains the members of this apartment.
     * @param person
     */
    public void addPersonToApartment( Person person) {
        if (person == null) {
            return;
        }

        ParseRelation<Person> relation = getUserRelation();
        relation.add(person);
        incrementNumberOfResidents();
        saveInBackground();
    }

    public void removePersonFromApartment( Person person) {
        if (person == null) {
            return;
        }

        ParseRelation<Person> relation = getUserRelation();
        relation.remove(person);
        decrementNumberOfResidents();
        saveInBackground();
    }

    /**
     * Returns the number of people the live in this apartment
     * @return the occupancy of this apartment.
     */
    public int getNumberOfResidents() {
        return getInt("numberOfResidents");
    }

    /**
     * Increments the number of people living here and returns the new value.
     * @return the new occupancy.
     */
    public int incrementNumberOfResidents() {
        increment("numberOfResidents");
        return getNumberOfResidents();
    }

    /**
     * Decrements the number of people living here and returns the updated value.
     * @return the new occupancy
     */
    public int decrementNumberOfResidents() {
        if( getNumberOfResidents() > 0) {
            increment("numberOfResidents", -1);
        }

        return getNumberOfResidents();
    }

    /**
     * Sets address fields for the apartment
     */
}
