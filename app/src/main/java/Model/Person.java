package Model;

import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;

/**
 * Created by saiteja64 on 1/23/16.
 */
public class Person extends ParseUser
{
    public String name;
    public String ;
    public ParseRelation apartmentRelation;


    public String getFirstName{
        ParseUser.getCurrentUser().getString("firstName");
    }

    public String getLastName{
    ParseUser.getCurrentUser().getString("lastName");
    }


    public Person()
    {

    }

    public void createUser(String userName, String password)
    {

        this.signUpInBackground();

    }

}
