package Model;

import android.util.Log;

import com.parse.LogInCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.LogOutCallback;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import Model.Apartment;
import Model.Managers.ApartmentManager;
import Model.Managers.InventoryManager;

/**
 * Created by saiteja64 on 1/23/16.
 */
//@ParseClassName(Person.className)
public class Person extends ParseUser
{
    public final static String className = "_User";

    public Person() {
        super();
    }

    public static Person getCurrentPerson() {
        return (Person)ParseUser.getCurrentUser();
    }

    public static Person createnewPerson( String name, String email, String password) {
        Person newPerson = new Person();

        newPerson.put("name", name);
        newPerson.put("email", email);
        newPerson.put("password", password);

        return newPerson;
    }

    /**
     * Accessor to the residence of this person
     *
     * @return The apartment which they live in.
     */
    public Apartment getApartment() {
        return (Apartment) getParseObject("apartment");
    }

    /**
     * Registers this person as living in the given apartment, iff they do not already live elsewhere
     *
     * @param apartment The apartment to add them to.
     */
    public void setApartment(Apartment apartment) {
        if (getParseObject("apartment") == null) {
            Log.d("Person", "Apartment is null! User doesn't have an apartment yet.");
            put("apartment", apartment);
            saveInBackground();
        }
    }

    public void leaveApartment(SaveCallback callback) {
        remove("apartment");
        saveInBackground(callback);
    }

    public boolean hasApartment() {
        return getApartment() != null;
    }

    /**
     * Get this person's name
     *
     * @return Their name
     */
    public String getName() {
        return getString("name");
    }

    //public String getEmail() { return getString("email"); }

    @Override
    public String toString() {
        return getString("name");
    }
}
