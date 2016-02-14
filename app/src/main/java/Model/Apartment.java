package Model;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

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
       // put("inventory", new Inventory());
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
    public boolean addPersonToApartment( Person person) {
        if (person == null) {
            return false;
        }

        if (members.contains(person)) {
            return false;
        }

        ParseRelation<Person> relation = getUserRelation();
        relation.add(person);
        incrementNumberOfResidents();
        saveInBackground();
        return true;
    }

    public boolean removePersonFromApartment( Person person) {
        if (person == null) {
            return false;
        }

        if(!members.contains(person)) {
            return false;
        }
        ParseRelation<Person> relation = getUserRelation();
        relation.remove(person);
        decrementNumberOfResidents();
        saveInBackground();
        return true;
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
