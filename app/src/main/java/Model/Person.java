package Model;

import com.parse.LogInCallback;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import Model.Apartment;

/**
 * Created by saiteja64 on 1/23/16.
 */
//@ParseClassName(Person.className)
public class Person extends ParseUser
{
    public final static String className = "_User";
    public Person()
    {
        super();
    }

    /**
     * Accesor to the residence of this person
     * @return The apartment which they live in.
     */
    public Apartment getApartment()
    {
        return (Apartment) getParseObject("apartment");
    }

    /**
     * Registers this person as living in the given apartment, iff they do not already live elsewhere
     * @param apartment The apartment to add them to.
     */
    public void setApartment(Apartment apartment)
    {
        if(getParseObject("apartment") == null)
        {
            put("apartment",apartment);
        }
    }

    /**
     * Get this person's first name
     * @return Their first name
     */
    public String getFirstName () {
        return getString("firstName");
    }

    /**
     * Get this person's last name
     * @return Their last name
     */
    public String getLastName() {
        return getString("lastName");
    }

    public static Person getCurrentPerson() {
        return (Person )ParseUser.getCurrentUser();
    }

    /**
     * Convenience method to sign up a new Person.
     */
    public static Person createPerson(String name, String email, String password, SignUpCallback callback)
    {
        Person.logoutPerson();

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
     * @param username
     * @param password
     */
    public static void loginPerson(String username, String password, LogInCallback callback)
    {
        ParseUser.logInInBackground(username, password, callback);
    }

    /**
     * Convenience method to logout a person
     */
    public static void logoutPerson()
    {
        Person person = getCurrentPerson();

        if (person != null) {
            person.logOutInBackground();
        }
    }


}
