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

    /**
     * Convenience method to sign up a new Person.
     */
    public static Person createPerson(String name, String email, String password, SignUpCallback callback) {
        Person.logoutPerson(null);

        Person person = new Person();
        person.setUsername(email);
        person.setEmail(email);
        person.setPassword(password);
        person.put("name", name);

        person.signUpInBackground(callback);

        return person;
    }

    /**
     * Convenience method to login a Person with the given username and password.
     *
     * @param username
     * @param password
     */
    public static void loginPerson(String username, String password, LogInCallback callback) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                Person person = (Person) user;
                ApartmentManager.apartmentManager.fetchApartment(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        InventoryManager.inventoryManager.fetchInventory(null);
                    }
                });
            }
        });
    }

    /**
     * Convenience method to logout a person
     */
    public static void logoutPerson(LogOutCallback callback) {
        Person person = getCurrentPerson();

        if (person != null) {
            person.logOutInBackground(callback);
        }
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
