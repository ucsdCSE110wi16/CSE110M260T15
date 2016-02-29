package Model;


import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by satre on 1/23/16.
 */
@ParseClassName(Apartment.className)
public class Apartment extends ParseObject {
    public final static String className = "Apartment";

    private ArrayList<Person> members = new ArrayList<Person>();

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

        Inventory newInventory = Inventory.createNewInventoryWithName(apartment_name + "-inventory");
        apartment.put("inventory", newInventory);

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
    public boolean addPersonToApartment(Person person) {
        if (person == null) {
            return false;
        }

        if (members.contains(person)) {
            return false;
        }

        ParseRelation<Person> relation = getUserRelation();
        relation.add(person);
        incrementNumberOfResidents();
        members.add(person);
        saveInBackground();
        return true;
    }

    public void removePersonFromApartment( Person person, final SaveCallback callback) {
        if (person == null) {
            return;
        }

        if(!members.contains(person)) {
            return;
        }

        ParseRelation<Person> relation = getUserRelation();
        relation.remove(person);
        decrementNumberOfResidents();
        members.remove(person);
        person.leaveApartment(null);
        saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                callback.done(e);
            }
        });
    }

    public void fetchMembersOfApartment(final FindCallback<Person> callback) {
        final ParseRelation<Person> memberRelation = getUserRelation();
        ParseQuery<Person> memberQuery = memberRelation.getQuery();

        memberQuery.findInBackground(new FindCallback<Model.Person>() {
            @Override
            public void done(List<Person> objects, ParseException e) {
                if (objects != null) {
                    members = new ArrayList<>(objects.size());
                    for (Person aPerson : objects) members.add(aPerson);
                }

                if (callback != null) {
                    callback.done(objects, e);
                }
            }
        });
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
    /**
     * Accessor for the inventory of this apartment.
     * @return
     */
    public Inventory getInventory() {

        return (Inventory) getParseObject("inventory");
    }

    /**
     * Getter for the people who live here.
     * @return The list of people who live in this apartment.
     */
    public ArrayList<Person> getMembers() {
        return members;
    }

    public void setStreet_1( String street_1) {
        put("street_1", street_1);
    }

    public String getStreet_1() {
        return getString("street_1");
    }

    public void setStreet_2(String street_2) {
        put("street_2", street_2);
    }

    public String getStreet_2() {
        return getString("street_2");
    }

}
