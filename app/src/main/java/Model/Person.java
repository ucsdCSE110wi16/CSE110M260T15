package Model;

import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;

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
     * @param userName
     * @param password
     */
    public static void createUser(String userName, String password)
    {
        ParseUser.getCurrentUser().logOut();
        ParseUser newUser = new ParseUser();
        newUser.setUsername(userName);
        newUser.setPassword(password);
        newUser.signUpInBackground();
    }

    /**
     * Convenience method to login a perosn with the given username and password.
     * @param username
     * @param password
     */
    public static void loginUser(String username, String password)
    {
        ParseUser.logInInBackground(username,password);
    }


}
