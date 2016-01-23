package Model;

import com.parse.*;

/**
 * Created by satre on 1/23/16.
 */
@ParseClassName(Apartment.className)
public class Apartment extends ParseObject {
    public final static String className = "Apartment";

    /***********************
     * Properties
     */

    /**
     * Default Constructor
     */
    public Apartment() {}

    /**
     * Fetches the name of this apartment
     * @return String
     */
    public String getName() {
        return getString("name");
    }

    /**
     * Updates the name of this apartment to the given value.
     * @param String newName, the name to set.
     */
    public void setName(String newName){
        put("name", newName);
    }

    /**
     * Get the relation to User class that contains members of this apartment.
     * @return ParseRelation
     */
   public ParseRelation<ParseObject> getUserRelation() {
       return getRelation("users");
   }

    /**
     * 
     */
}
