package Model;

import com.parse.LogInCallback;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
 * Created by saiteja64 on 1/23/16.
 */
public class Person extends ParseUser
{
    public Person()
    {
        super();
    }

    /**
     * Accesor to the residence of this person
     * @return The apartment which they live in.
     */
    public ParseObject getApartment()
    {
        return ParseUser.getCurrentUser().getParseObject("apartment");
    }

    /**
     * Registers this person as living in the given apartment, iff they do not already live elsewhere
     * @param apartment The apartment to add them to.
     */
    public void setApartment(Apartment apartment)
    {
        if(ParseUser.getCurrentUser().getParseObject("apartment") == null)
        {
            ParseUser.getCurrentUser().put("apartment",apartment);
        }
    }

    /**
     * Get this person's first name
     * @return Their first name
     */
    public String getFirstName () {
        return ParseUser.getCurrentUser().getString("firstName");
    }

    /**
     * Get this person's last name
     * @return Their last name
     */
    public String getLastName() {
        return ParseUser.getCurrentUser().getString("lastName");
    }

    /**
     * Convenience method to sign up a new user.
     */
    public static void createUser(String name, String email, String password, SignUpCallback callback)
    {
        Person.logoutUser();

        ParseUser newUser = new ParseUser();
        newUser.setUsername(email);
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.put("name", name);

        newUser.signUpInBackground(callback);
    }

    /**
     * Convenience method to login a person with the given username and password.
     * @param username
     * @param password
     */
    public static void loginUser(String username, String password, LogInCallback callback)
    {
        ParseUser.logInInBackground(username,password, callback);
    }

    /**
     * Convenience method to logout a person
     */
    public static void logoutUser()
    {
        ParseUser user = ParseUser.getCurrentUser();

        if (user != null) {
            user.logOutInBackground();
        }
    }


}
