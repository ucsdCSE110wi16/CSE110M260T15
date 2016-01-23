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

    }

    public ParseObject getApartment()
    {
        ParseUser.getCurrentUser().getParseObject("apartment");
    }

    public void setApartment(Apartment apartment)
    {
        if(ParseUser.getCurrentUser().getParseObject("apartment") == null)
        {
            ParseUser.getCurrentUser().add("apartment",apartment);
        }
    }

    public String getFirstName{
        ParseUser.getCurrentUser().getString("firstName");
    }

    public String getLastName{
    ParseUser.getCurrentUser().getString("lastName");
    }


    public void createUser(String userName, String password)
    {
        ParseUser.getCurrentUser().logOut();
        ParseUser newUser = new ParseUser();
        newUser.setUsername(userName);
        newUser.setPassword(password);
        newUser.signUpInBackground();
    }

    public void loginUser(String username, String password)
    {
        ParseUser.logInInBackground(username,password);
    }


}
