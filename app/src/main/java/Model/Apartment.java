package Model;


import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

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


    public static Apartment createApartment(String apartment_name, String street_1, String street_2, String state, String city, String zip_code, SaveCallback sc) {

        Log.d("Apartment", "creating apartment");

        Apartment apartment = new Apartment();

        apartment.put("name", apartment_name);
        apartment.put("street_1", street_1);
        apartment.put("street_2", street_2);
        apartment.put("state", state);
        apartment.put("city", city);
        apartment.put("zip_code", zip_code);

        apartment.saveInBackground(sc);

        return apartment;
    }

    /**
     * Removes apartment from database
     */
    public void deleteApartment(Apartment apartment) {
        /* TODO: remove all roomies' relations to apartment and then delete apartment */


    }
    /***********************
     * Properties
     */


    /**
     * Fetches the name of this apartment
     *
     * @return String
     */
    public String getName() {
        return getString("name");
    }

    /**
     * Updates the name of this apartment to the given value.
     *
     * @param newName, the name to set.
     */
    public void setName(String newName) {
        put("name", newName);
    }

    /**
     * Get the relation to User class that contains members of this apartment.
     *
     * @return ParseRelation
     */
    public ParseRelation<Person> getUserRelation() {

        ParseRelation<Person> roomies = getRelation("users");
        return roomies;
    }

    /**
     * Adds the given person the relation that contains the members of this apartment.
     *
     * @param person
     */
    public void addPersonToApartment(Person person) {
        if (person == null) {
            return;
        }

        ParseRelation<Person> relation = getUserRelation();
        relation.add(person);
        incrementNumberOfResidents();
        saveInBackground();
    }

    public void removePersonFromApartment(Person person) {
        if (person == null) {
            return;
        }

        ParseRelation<Person> relation = getUserRelation();
        relation.remove(person);
        decrementNumberOfResidents();
        saveInBackground();
    }

    public void findMembers(FindCallback<Person> callback) {
        ParseQuery<Person> query = ParseQuery.getQuery(Person.class);
        query.whereEqualTo("apartment", this);

        query.findInBackground(callback);

    }

    /**
     * Returns the number of people the live in this apartment
     *
     * @return the occupancy of this apartment.
     */
    public int getNumberOfResidents() {
        return getInt("numberOfResidents");
    }

    /**
     * Increments the number of people living here and returns the new value.
     *
     * @return the new occupancy.
     */
    public int incrementNumberOfResidents() {
        increment("numberOfResidents");
        return getNumberOfResidents();
    }

    /**
     * Decrements the number of people living here and returns the updated value.
     *
     * @return the new occupancy
     */
    public int decrementNumberOfResidents() {
        if (getNumberOfResidents() > 0) {
            increment("numberOfResidents", -1);
        }

        return getNumberOfResidents();
    }


    /**
     * Sets address fields for the apartment
     */
}
